package com.multicore;

/**
 * An java.util.concurrent.ScheduledExecutorService alternative
 * that allows you to monitor the number of submitted, failed and succeeded task, plus has the
 * ability to wait until all tasks are completed = there are no scheduled or running tasks.
 *
 * Created by vads on 9/18/16.
 * -- Reused parts from https://github.com/MatejTymes/JavaFixes
 */
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.Executors.newScheduledThreadPool;

public class Runner {
  private final AtomicInteger failedToSubmit;
  private final AtomicInteger succeeded;
  private final AtomicInteger failed;

  private final CountDownLatch latch;

  protected final ScheduledExecutorService executor;

  /**
   * Constructs a runner with specific number of executor thread.
   *
   * @param threadCount number of executor threads
   *
   */
  public Runner(int threadCount, int operationCountSize) {
    this.executor = newScheduledThreadPool(threadCount);

    latch = new CountDownLatch(operationCountSize);
    failedToSubmit = new AtomicInteger();
    succeeded = new AtomicInteger();
    failed = new AtomicInteger();
  }

  public Future<Void> run(Runnable runnable) {
    return submit(asMonitoredCallable(runnable));
  }

  private <T> Future<T> submit(Callable<T> monitoredTask) {
    try {

      return executor.submit(monitoredTask);
    } catch (RuntimeException e) {
      taskSubmitFailed();
      throw e;
    }
  }

  public Runner waitTillDone() {
    try {
      latch.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  private Callable<Void> asMonitoredCallable(Runnable task) {
    return new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        try {
          task.run();
          taskFinished();

          return null;
        } catch (Exception e) {
          BasicThread basicThread = (BasicThread) task.getClass().newInstance();
          Utils.logInfo("[Runner] Exception occurred: " + e + " class name: " +  basicThread.getOperationID() + "stackTrace: " + e.getStackTrace());
          taskFailed();
          throw e;
        } catch (Throwable t) {
          taskFailed();
          throw new RuntimeException(t);
        }
      }
    };
  }

  private void taskSubmitFailed() {
    failedToSubmit.incrementAndGet();
    latch.countDown();
  }

  private void taskFinished() {
    succeeded.incrementAndGet();
    latch.countDown();
  }

  private void taskFailed() {
    failed.incrementAndGet();
    latch.countDown();
  }

  public long toBeCompletedCount() {
    return latch.getCount();
  }

  /**
   * @return number of tasks that were not accepted by the executor - most possibly because it has been shut down
   */
  public int failedSubmissionCount() {
    return failedToSubmit.get();
  }

  /**
   * @return number of tasks that completed without throwing an exception
   */
  public int succeededCount() {
    return succeeded.get();
  }

  /**
   * @return number of tasks that completed with a thrown exception
   */
  public int failedCount() {
    return failed.get();
  }


  /**
   * Resets failedSubmission, succeeded and failed counter.
   * It doesn't reset the toBeFinished counter as it is a derived number from the number of scheduled + running tasks.
   */
  public void resetCounters() {
    failedToSubmit.set(0);
    succeeded.set(0);
    failed.set(0);
  }

  public void shutDown() {
    this.executor.shutdown();
  }

}

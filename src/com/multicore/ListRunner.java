package com.multicore;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ListRunner {

  public float startThreads(int n, BasicLinkedList list, AtomicInteger counter) {
        Runner runner = new Runner(n);
        long startTime = System.currentTimeMillis();
        List<Range> threadTasks = Utils.scheduleTasks(n);

      /*
        int k=1;
        for( Range range: threadTasks ) {
            if( range != null ) {
                System.out.println("Thread id:" + k + " tasks range:[" + range.x + "," + range.y + "]");
            }
            else {
                System.out.println("Thread id:" + k +" NOP");
            }
            k++;
        }
        */

        for(int i=1;i <= n;i++) {
          runner.run( new BasicThread(i, list, counter,threadTasks.get(i-1)) );
        }

        runner.waitTillDone();
        runner.shutDown();
        /*
        long expectedCounterValue = n * RunParameters.NUMBER_OF_CS_RUNS_PER_THREAD.value
          * RunParameters.NUMBER_TO_COUNT_IN_CS.value;
        long actualCounterValue = counter.get();
        if (expectedCounterValue != actualCounterValue) {
          Utils.log("\t\tCounter doesn't look good. Expected: " + expectedCounterValue + " Actual: " + actualCounterValue);
        }

        long endTime = System.currentTimeMillis();
        float totalTime = (float) (endTime - startTime) / 1000;

        Utils.log("\t\tExecution Time: " + totalTime + " seconds.");
        return totalTime;
        */
        return 0;
  }

  public void run(int n) {
      Utils.log("Coarse Grain List:");
      TestTestAndSet lock = new TestTestAndSet();
      BasicLinkedList list = new CoarseGrainList(lock);
      startThreads(n, list, new AtomicInteger());
  }
}

package com.multicore;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ListRunner {

  public float startThreads(int n, BasicLinkedList list, RunMode runMode, int totalOperationsCount) {
      Runner runner = new Runner(n);
      long startTime = System.currentTimeMillis();

      int i = 0;
      Random random = new Random();

      int insertEnd = runMode.percentageOfInserts;
      int deleteStart = insertEnd + 1;
      int deleteEnd = deleteStart + runMode.percentageOfDeletes;

      while (i++ < totalOperationsCount) {
        int randomInt = random.nextInt(100) + 1;
        int key = random.nextInt(RunParameters.MAX_KEY_SIZE.value) + 1;

        if (randomInt >= 1 && randomInt <= insertEnd) {
          runner.run( new BasicThread(i, list, "insert", key) );
        }
        else if (randomInt >= deleteStart && randomInt < deleteEnd) {
          runner.run( new BasicThread(i, list, "delete", key) );
        }
        else {
          runner.run( new BasicThread(i, list, "search", key) );
        }

      }

      runner.waitTillDone();
      runner.shutDown();

      /*
      long expectedCounterValue = n * RunParameters.NUMBER_OF_CS_RUNS_PER_THREAD.value
        * RunParameters.NUMBER_TO_COUNT_IN_CS.value;
      long actualCounterValue = counter.get();
      if (expectedCounterValue != actualCounterValue) {
        Utils.logInfo("\t\tCounter doesn't look good. Expected: " + expectedCounterValue + " Actual: " + actualCounterValue);
      }

      long endTime = System.currentTimeMillis();
      float totalTime = (float) (endTime - startTime) / 1000;

      Utils.logInfo("\t\tExecution Time: " + totalTime + " seconds.");
      return totalTime;
      */
        return 0;
  }

  public void run(int numberOfThreads, int totalOperationsCount) {

      // For each run, execute it for all the different modes.
      for (RunMode mode : RunMode.values()) {

        Utils.logWarning("Testing Coarse Grain List in  '" + mode.modeName + "' mode.");
        BasicLinkedList coarseGrainList = new CoarseGrainList();
        startThreads(numberOfThreads, coarseGrainList, mode, totalOperationsCount);

        Utils.logWarning("Testing Fine Grain List in  '" + mode.modeName + "' mode.");
        BasicLinkedList fineGrainList = new FineGrainList();
        startThreads(numberOfThreads, fineGrainList, mode, totalOperationsCount);

        Utils.logWarning("Testing Lock free List in  '" + mode.modeName + "' mode.");
        BasicLinkedList lockFreeList = new LockFreeList();
        startThreads(numberOfThreads, lockFreeList, mode, totalOperationsCount);
      }
  }
}

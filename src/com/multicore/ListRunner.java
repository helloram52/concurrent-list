package com.multicore;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class ListRunner {

  public void prePopulateList(BasicLinkedList list) {
    int prePopulateSize = RunParameters.MAX_KEY_SIZE.value / 2;
    int i = 0;
    Random rand = new Random();
    while (i++ < prePopulateSize) {
      int key = rand.nextInt(RunParameters.MAX_KEY_SIZE.value) + 1;
      list.insert(key);
    }
  }

  public float startThreads(int n, BasicLinkedList list, RunMode runMode, int totalOperationsCount) {
      Runner runner = new Runner(n);
      long startTime = System.currentTimeMillis();

      // Pre-populate the given list so that read-dominated mode has something to
      // test.
      prePopulateList(list);

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

      long endTime = System.currentTimeMillis();
      float totalTime = (float) (endTime - startTime) / 1000;

      return totalTime;
  }

  public void run(int numberOfThreads, int totalOperationsCount) {

      float executionTime;
      // For each run, execute it for all the different modes.
      for (RunMode mode : RunMode.values()) {

        Utils.logWarning("Testing Coarse Grain List in  '" + mode.modeName + "' mode.");
        BasicLinkedList coarseGrainList = new CoarseGrainList();
        executionTime = startThreads(numberOfThreads, coarseGrainList, mode, totalOperationsCount);
        Utils.logInfo("\t\tExecution Time: " + executionTime + " seconds.");

        Utils.logWarning("Testing Fine Grain List in  '" + mode.modeName + "' mode.");
        BasicLinkedList fineGrainList = new FineGrainList();
        executionTime = startThreads(numberOfThreads, fineGrainList, mode, totalOperationsCount);
        Utils.logInfo("\t\tExecution Time: " + executionTime + " seconds.");

        Utils.logWarning("Testing Lock free List in  '" + mode.modeName + "' mode.");
        BasicLinkedList lockFreeList = new LockFreeList();
        executionTime = startThreads(numberOfThreads, lockFreeList, mode, totalOperationsCount);
        Utils.logInfo("\t\tExecution Time: " + executionTime + " seconds.");
      }
  }
}

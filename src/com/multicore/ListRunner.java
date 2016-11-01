package com.multicore;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class ListRunner {

  public void prePopulateList(BasicLinkedList list, SequentialLinkedList sequentialLinkedList) {
    int prePopulateSize = RunParameters.MAX_KEY_SIZE.value / 2;
    int i = 0;
    Random rand = new Random();
    while (i++ < prePopulateSize) {
      int key = rand.nextInt(RunParameters.MAX_KEY_SIZE.value) + 1;
      list.insert(key);
      sequentialLinkedList.insert(key);
    }
  }

  public float startThreads(int n, BasicLinkedList list, RunMode runMode, int totalOperationsCount, SequentialLinkedList sequentialLinkedList) {
      Runner runner = new Runner(n);
      long startTime = System.currentTimeMillis();

      // Pre-populate the given list so that read-dominated mode has something to
      // test.
      prePopulateList(list, sequentialLinkedList);

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
            sequentialLinkedList.insert(key);
        }
        else if (randomInt >= deleteStart && randomInt < deleteEnd) {
            runner.run( new BasicThread(i, list, "delete", key) );
            sequentialLinkedList.delete(key);
        }
        else {
          runner.run( new BasicThread(i, list, "search", key) );
        }

      }
      //System.out.println( "THreads yet to be completed="+runner.toBeCompletedCount() );
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
          SequentialLinkedList sequentialCoarseGrainList = new SequentialLinkedList();
          executionTime = startThreads(numberOfThreads, coarseGrainList, mode, totalOperationsCount, sequentialCoarseGrainList);
          Utils.logInfo("\t\tExecution Time: " + executionTime + " seconds.");
          if (ConcurrentListTester.compareLists(coarseGrainList, sequentialCoarseGrainList)) {
              Utils.logWarning("Coarse grain List: Lists are consistent!!");
          }
          else {
              Utils.logWarning("Coarse grain List: Lists are not consistent!!");
              ConcurrentListTester.printLists(coarseGrainList, sequentialCoarseGrainList);
          }

          Utils.logWarning("Testing Fine Grain List in  '" + mode.modeName + "' mode.");

          BasicLinkedList fineGrainList = new FineGrainList();
          SequentialLinkedList sequentialFineGrainList = new SequentialLinkedList();
          executionTime = startThreads(numberOfThreads, fineGrainList, mode, totalOperationsCount, sequentialFineGrainList);
          Utils.logInfo("\t\tExecution Time: " + executionTime + " seconds.");
          if (ConcurrentListTester.compareLists(fineGrainList, sequentialFineGrainList)) {
              Utils.logWarning("Fine grain List: Lists are consistent!!");
          }
          else {
              Utils.logWarning("Fine grain List: Lists are not consistent!!");
              ConcurrentListTester.printLists(fineGrainList, sequentialFineGrainList);
          }

        Utils.logWarning("Testing Lock free List in  '" + mode.modeName + "' mode.");
        BasicLinkedList lockFreeList = new LockFreeList();
        SequentialLinkedList sequentialLockFreeList = new SequentialLinkedList();
        executionTime = startThreads(numberOfThreads, lockFreeList, mode, totalOperationsCount, sequentialLockFreeList);
        Utils.logInfo("\t\tExecution Time: " + executionTime + " seconds.");
        if (ConcurrentListTester.compareLists(lockFreeList, sequentialLockFreeList)) {
            Utils.logWarning("Lock free List: Lists are consistent!!");
        }
        else {
            Utils.logWarning("Lock free List: Lists are not consistent!!");
          ConcurrentListTester.printLists(lockFreeList, sequentialLockFreeList);
        }
     }
  }
}

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

  public int getNumberOfBatches( int tasks, int threads ) {

      if( tasks <= threads ) {
          return 1;
      }

      return ( tasks % threads == 0 )? tasks/threads:(tasks/threads) + 1;
  }
  public float startThreads(int n, BasicLinkedList list, RunMode runMode, int totalOperationsCount, SequentialLinkedList sequentialList) {

      long startTime = System.currentTimeMillis();

      // Pre-populate the given list so that read-dominated mode has something to
      // test.
      //prePopulateList(list, sequentialList);

      int numberOfBatches = getNumberOfBatches(totalOperationsCount, n);
      int count=0;
      int[] threadID = {0};

      while( count++ < numberOfBatches ) {

          Runner runner = new Runner(n);
          runThreads(threadID, runner, runMode, n, list, sequentialList);
          runner.waitTillDone();
          runner.shutDown();

      }

      long endTime = System.currentTimeMillis();
      float totalTime = (float) (endTime - startTime) / 1000;

      return totalTime;
  }

    public void runThreads( int[] threadId, Runner runner, RunMode runMode, int noOfThreads, BasicLinkedList list, BasicLinkedList SequentialList ){

      int i = 0;
      Random random = new Random();
      int insertEnd = runMode.percentageOfInserts;
      int deleteStart = insertEnd + 1;
      int deleteEnd = deleteStart + runMode.percentageOfDeletes;

      while (i++ < noOfThreads) {
        int randomInt = random.nextInt(100) + 1;
        int key = random.nextInt(RunParameters.MAX_KEY_SIZE.value) + 1;
        if (randomInt >= 1 && randomInt <= insertEnd) {
            runner.run( new BasicThread(threadId[0], list, "insert", key) );
            SequentialList.insert(key);
        }
        else if (randomInt >= deleteStart && randomInt < deleteEnd) {
            runner.run( new BasicThread(threadId[0], list, "delete", key) );
            SequentialList.delete(key);
        }
        else {
          runner.run( new BasicThread(threadId[0], list, "search", key) );
        }
        threadId[0] += i;
      }
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

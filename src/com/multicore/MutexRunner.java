/*
 * Copyright (c) 2014 Cloudvisory LLC. All rights reserved.
 */
package com.multicore;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by vads on 9/18/16.
 */
public class MutexRunner {

  public float startThreads(int n, Lock lock, AtomicInteger counter) {
    Runner runner = new Runner(n);
    long startTime = System.currentTimeMillis();

    for(int i = 1;i <= n;i++) {
      runner.run( new BasicThread(i, lock, counter) );
    }

    runner.waitTillDone();
    runner.shutDown();

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
  }

  public void run(int n) {
    Lock tournamentLock = new Tournament(n);
    Utils.log("Tournament Algorithm:");
    startThreads(n, tournamentLock, new AtomicInteger());

    Lock testAndSetLock = new TestAndSet();
    Utils.log("TestAndSet Algorithm:");
    startThreads(n, testAndSetLock, new AtomicInteger());

    Lock testtestAndSetLock = new TestTestAndSet();
    Utils.log("TestTestAndSet Algorithm:");
    startThreads(n, testtestAndSetLock, new AtomicInteger());
  }
}

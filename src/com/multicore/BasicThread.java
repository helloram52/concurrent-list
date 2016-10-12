/*
 * Copyright (c) 2014 Cloudvisory LLC. All rights reserved.
 */
package com.multicore;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by vads on 9/18/16.
 */
public class BasicThread implements Runnable {

  public int getID() {
    return ID;
  }

  private int ID;
  private AtomicInteger counter;
  private Lock lock;


  public BasicThread(int ID, Lock lock, AtomicInteger counter) {
    this.ID = ID;
    this.counter = counter;
    this.lock = lock;
  }

  private void enterCriticalSection() {
    long i = 0;
    while (i++ < RunParameters.NUMBER_TO_COUNT_IN_CS.value) {
      this.counter.incrementAndGet();
    }

//    Utils.log("Thread '" + this.ID + "', counter value: '"+ this.counter.get() + "'");
  }

  private void acquireLock() {
    lock.lock(this.ID);
  }

  private void releaseLock() {
    lock.unlock(this.ID);
  }

  public void run() {
    for (int i = 0; i < RunParameters.NUMBER_OF_CS_RUNS_PER_THREAD.value; i++) {
      acquireLock();
      enterCriticalSection();
      releaseLock();
    }
  }

}

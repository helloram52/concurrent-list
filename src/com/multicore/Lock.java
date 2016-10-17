package com.multicore;

import java.util.concurrent.atomic.AtomicInteger;

public interface Lock {
  public void lock(int threadID);
  public void unlock(int threadID);
}

class TTASLock implements Lock {

  private AtomicInteger lock;
  int counter;

  TTASLock() {
    lock = new AtomicInteger(0);
    counter = 0;
  }

  public int getCounter()
  {
    return counter;
  }

  public void enterCS() {

    counter++;
  }

  @Override
  public void lock(int threadID) {

    while(true) {

      while(lock.get() == 1);
      if( lock.getAndSet(1) != 1 ) {
        return;
      }
    }
  }

  @Override
  public void unlock(int threadID) {
    lock.set(0);
  }
}
package com.multicore;

import java.util.concurrent.atomic.AtomicBoolean;

public class TestTestAndSet implements Lock {
  private volatile AtomicBoolean lockFlag;

  public TestTestAndSet() {
    this.lockFlag = new AtomicBoolean();
  }

  @Override
  public void lock() {
    while (true) {
      while (lockFlag.get() == true);
      if (lockFlag.getAndSet(true) == false) {
        break;
      }
    }
  }

  @Override
  public void unlock() {
    lockFlag.set(false);
  }
}

package com.multicore;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by vads on 9/18/16.
 */
public class TestAndSet implements Lock {
  private volatile AtomicBoolean lockFlag;

  public TestAndSet() {
    this.lockFlag = new AtomicBoolean();
  }

  @Override
  public void lock(int threadID) {
    while ( lockFlag.getAndSet(true) == true );
  }

  @Override
  public void unlock(int threadID) {
    lockFlag.set(false);
  }
}

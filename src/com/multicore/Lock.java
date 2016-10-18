package com.multicore;

import java.util.concurrent.atomic.AtomicInteger;

public interface Lock {
  public void lock(int threadID);
  public void unlock(int threadID);
}
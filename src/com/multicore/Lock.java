package com.multicore;

import java.util.concurrent.atomic.AtomicInteger;

public interface Lock {
  public void lock();
  public void unlock();
}
package com.multicore;

public interface Lock {
  public void lock(int threadID);
  public void unlock(int threadID);
}

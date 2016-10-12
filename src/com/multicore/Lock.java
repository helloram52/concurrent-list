/*
 * Copyright (c) 2014 Cloudvisory LLC. All rights reserved.
 */

package com.multicore;

/**
 * Created by vads on 9/18/16.
 */
public interface Lock {
  public void lock(int threadID);
  public void unlock(int threadID);
}

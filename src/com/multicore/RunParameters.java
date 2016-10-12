/*
 * Copyright (c) 2014 Cloudvisory LLC. All rights reserved.
 */

package com.multicore;

/**
 * Created by vads on 9/21/16.
 */
public enum RunParameters {
  NUMBER_OF_RUNS(1),
  NUMBER_OF_CS_RUNS_PER_THREAD(1000000),
  NUMBER_TO_COUNT_IN_CS(10);

  public final int value;

  RunParameters(int value) {
    this.value = value;
  }
}

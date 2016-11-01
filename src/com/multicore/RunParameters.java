package com.multicore;

public enum RunParameters {
  NUMBER_OF_RUNS(10),
  TARGET_OPERATION_COUNT_FOR_EACH_MODE(100),
  MAX_KEY_SIZE(1000);
  public final int value;

  RunParameters(int value) {
    this.value = value;
  }
}

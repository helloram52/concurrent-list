package com.multicore;

public enum RunParameters {
  NUMBER_OF_RUNS(1),
  TARGET_OPERATION_COUNT_FOR_EACH_MODE(1000);

  public final int value;

  RunParameters(int value) {
    this.value = value;
  }
}

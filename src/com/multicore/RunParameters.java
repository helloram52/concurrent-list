package com.multicore;

public enum RunParameters {
  NUMBER_OF_RUNS(1),
  NUMBER_OF_OPERATIONS(100000),
  NUMBER_OF_CS_RUNS_PER_THREAD(1),
  NUMBER_TO_COUNT_IN_CS(1);

  public final int value;

  RunParameters(int value) {
    this.value = value;
  }
}

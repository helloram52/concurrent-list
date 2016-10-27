package com.multicore;

public enum RunParameters {
  NUMBER_OF_RUNS(1),
  NUMBER_OF_OPERATIONS(100000);

  public final int value;

  RunParameters(int value) {
    this.value = value;
  }
}

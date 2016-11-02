package com.multicore;

/**
 * Enum class describing the various modes of running
 * the experiment.
 */
public enum RunMode {
  READ_DOMINATED(9, 1, 90, "Read Dominated"),
  MIXED(20, 10, 70, "Mixed"),
  WRITE_DOMINATED(50, 50, 0, "Write Dominated");

  int percentageOfInserts, percentageOfDeletes, percentageOfSearches;
  String modeName;
  RunMode(int percentageOfInserts, int percentageOfDeletes, int percentageOfSearches, String modeName) {
    this.percentageOfInserts = percentageOfInserts;
    this.percentageOfDeletes = percentageOfDeletes;
    this.percentageOfSearches = percentageOfSearches;
    this.modeName = modeName;
  }
}

/*
 * Copyright (c) 2014 Cloudvisory LLC. All rights reserved.
 */

package com.multicore;

/**
 * Enum class describing the various modes of running
 * the experiment.
 */
public enum RunMode {
  READ_DOMINATED(9, 1, 90),
  MIXED(20, 10, 70),
  WRITE_DOMINATED(50, 50, 0);

  int percentageOfInserts, percentageOfDeletes, percentageOfSearches;
  RunMode(int percentageOfInserts, int percentageOfDeletes, int percentageOfSearches) {
    this.percentageOfInserts = percentageOfInserts;
    this.percentageOfDeletes = percentageOfDeletes;
    this.percentageOfSearches = percentageOfSearches;
  }
}

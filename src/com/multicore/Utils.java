/*
 * Copyright (c) 2014 Cloudvisory LLC. All rights reserved.
 */
package com.multicore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by vads on 9/18/16.
 */
public final class Utils {
  public static boolean debugFlag = true;
  private static Logger logger;
  private static HashMap<Integer, ArrayList<Float>> statsMap;

  public Utils() {
    statsMap = new HashMap<>();
  }

  public static void setLogger() {
    logger = Logger.getLogger("CustomLog");
    FileHandler fh;

    try {
      fh = new FileHandler("run.log");
      logger.addHandler(fh);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(new Formatter() {
        @Override
        public String format(LogRecord record) {
          SimpleDateFormat logTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
          Calendar cal = new GregorianCalendar();
          cal.setTimeInMillis(record.getMillis());

          return "[" + logTime.format(cal.getTime())
            + "] " + record.getLevel()
            + " : "
            + record.getMessage() + "\n";
        }
      });
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param msg
   */
  public static void Debug(String msg) {
    if (debugFlag)
      System.out.println("[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS").format(new Date()) + "] " + msg);
  }

  public static void log(String message) {
    logger.info(message);
  }

  public static int computeTreeDepth(int numberOfThreads) {
    if (numberOfThreads == 1) return 1;

    return (int) Math.ceil( logOfBase(numberOfThreads, 2) );
  }

  public static double logOfBase(int num, int base) {
    return Math.log(num) / Math.log(base);
  }

  public static int nearestPowerOfTwo(int n) {
    // Base cases
    if (n == 0) return 1;
    if (n == 1) return 2;

    // n already a power of 2, return it.
    if ( ( n & (n-1) ) == 0 ) return n;

    int i = 1;
    while (i < n) i <<= 1;

    return i;
  }

  public static void gatherStatistics(int algorithmIndex, float executionTime) {
    ArrayList<Float> temp = statsMap.get(algorithmIndex);
    if(temp != null) {
      temp.add(executionTime);
      statsMap.put(algorithmIndex, temp);
    }
    else {
      ArrayList<Float> floatArrayList = new ArrayList<>(1);
      floatArrayList.add(executionTime);
      statsMap.put(algorithmIndex, floatArrayList);
    }
  }

  public static void printStatistics() {
  }
}

package com.multicore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class Utils {

    static final int SEARCH = 0;
    static final int INSERT = 1;
    static final int DELETE = 2;
    static final String INSERT_STRING = "insert";
    static final String SEARCH_STRING = "search";
    static final String DELETE_STRING = "delete";
    static List<int[]> taskList = new ArrayList<>();

    static int[] getTask(int index) {
        return taskList.get(index);
    }

    static int getTaskID(String task) {
        if( task.equals(INSERT_STRING) ) {
            return INSERT;
        }
        else if( task.equals(DELETE_STRING) ) {
            return DELETE;
        }
        else if( task.equals(SEARCH_STRING) ) {
            return SEARCH;
        }
        return -1;
    }

    static String getTaskName(int taskId) {

        if( taskId == INSERT ) {
            return INSERT_STRING;
        }
        else if( taskId == DELETE ) {
            return DELETE_STRING;
        }
        else if( taskId == SEARCH ) {
            return SEARCH_STRING;
        }

        return "";
    }

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

    public static List<int[]> getTaskList() {
        return taskList;
    }

  public static void loadTasks(String filename) throws IOException{

      BufferedReader reader;

      try {
          reader=new BufferedReader(new FileReader(filename));
          String line=reader.readLine();
          while( line != null ) {
              String[] operationInput = line.split("\\s");

              if( getTaskID( operationInput[0]) == -1 ) {
                  System.out.println("INFO: Illegal Operation:"+line);
                  System.exit(2);
              }

              int[] task = new int[2];
              task[0] = getTaskID(operationInput[0]);
              task[1] = Integer.parseInt(operationInput[1]);
              taskList.add(task);

              line=reader.readLine();
          }

      }
      catch( IOException e ) {
          e.printStackTrace();
      }
  }

    public static void printTasks() {

        for( int[] task : getTaskList() ) {
            System.out.println(task[0]+" "+task[1]);
        }
    }

    public static List<Range> scheduleTasks(int numberOfThreads) {

        List<Range> scheduledTasksList = new ArrayList<>();
        int numberOfTasks = getTaskList().size();

        if( numberOfThreads > numberOfTasks ) {

            for( int i=0; i<numberOfThreads; i++ ) {
                if( i >= numberOfTasks ) {
                    scheduledTasksList.add(null);
                }
                else {
                    scheduledTasksList.add(new Range(i, i));
                }
            }
        }
        else {
            int tasksPerThread = (numberOfTasks%numberOfThreads != 0)? (numberOfTasks/numberOfThreads)+1: numberOfTasks/numberOfThreads;
            int startPos = 0, endPos=tasksPerThread-1;

            for( int i=0; i<numberOfThreads; i++ ) {

                if( endPos >= numberOfTasks ) {
                    endPos = numberOfTasks-1;
                }

                if( startPos <= endPos ) {
                    scheduledTasksList.add(new Range(startPos, endPos));
                }
                else {
                    scheduledTasksList.add(null);
                }
                startPos = endPos + 1;
                endPos = endPos + tasksPerThread;
            }
        }
        return scheduledTasksList;
    }
}


package com.multicore;

import java.util.concurrent.atomic.AtomicInteger;

public class BasicThread implements Runnable {

  private int operationID;
  private String operationName;
  private int key;
  private BasicLinkedList list;

  public int getOperationID() {
    return operationID;
  }

  public BasicThread(int operationID, BasicLinkedList list, String operationName, int key) {
    this.operationID = operationID;
    this.list = list;
    this.operationName = operationName;
    this.key = key;
  }

  public void run() {

    boolean result;
    String threadname = Thread.currentThread().getName();
    switch (operationName) {
      case "insert":

        Utils.logInfo(threadname + " : Invoking OperationID: " + operationID );
        result = list.insert(key);
        Utils.logInfo(threadname + " : OperationID: " + operationID +  " Insert(" + key + ") : " + result);
        break;

      case "delete":

        Utils.logInfo(threadname + " : Invoking OperationID: " + operationID );
        result = list.delete(key);
        Utils.logInfo(threadname + " : OperationID: " + operationID +  " Delete(" + key + ") : " + result);
        break;

      case "search":

        Utils.logInfo(threadname + " : Invoking OperationID: " + operationID );
        result = list.search(key);
        Utils.logInfo(threadname + " : OperationID: " + operationID +  " Search(" + key + ") : " + result);
        break;

      default:
        Utils.logInfo(threadname + ": Invalid operation: " + operationName);

    }
  }

}

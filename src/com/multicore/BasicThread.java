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
    switch (operationName) {
      case "insert":

        Utils.logInfo("Invoking OperationID: " + operationID );
        result = list.insert(key);
        Utils.logInfo("OperationID: " + operationID +  " Insert(" + key + ") : " + result);
        break;

      case "delete":

        Utils.logInfo("Invoking OperationID: " + operationID );
        result = list.delete(key);
        Utils.logInfo("OperationID: " + operationID +  " Delete(" + key + ") : " + result);
        break;

      case "search":

        Utils.logInfo("Invoking OperationID: " + operationID );
        result = list.search(key);
        Utils.logInfo("OperationID: " + operationID +  " Search(" + key + ") : " + result);
        break;

      default:
        Utils.logInfo("Invalid operation: " + operationName);

    }
  }

}

package com.multicore;

public class CoarseGrainList implements BasicLinkedList {
  private Lock lock;
  private Node head;
  private Node tail;

  CoarseGrainList() {
    head = new Node(Integer.MIN_VALUE);
    tail = new Node(Integer.MAX_VALUE);

    head.next = tail;
    tail.next = null;
    this.lock = new TestTestAndSet();
  }

  public Node getHead() {
    return this.head;
  }

  @Override
  public boolean insert(int insertKey) {

    lock.lock();
    try {
      Node pred, curr;
      pred = head;
      curr = head.next;
      // Traverse till we find the first key
      // higher than or equal to tthe given key.
      while (curr.key < insertKey ) {
        pred = curr;
        curr = pred.next;
      }

      // We don't allow duplicate key inserts. So, just return.
      if (curr.key == insertKey) {
        return false;
      }

      Node newNode = new Node(insertKey);
      newNode.next = curr;
      pred.next = newNode;

      return true;

    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean delete(int deleteKey) {
    lock.lock();
    try {
      Node pred, curr;
      pred = head;
      curr = head.next;

      // Traverse till we find the first key
      // higher than or equal to tthe given key.
      while (curr.key < deleteKey) {
        pred = curr;
        curr = pred.next;
      }

      if (curr.key == deleteKey) {
        pred.next = curr.next;
        return true;
      }

      return false;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean search(int searchKey) {
//    lock.lock();
//    try {
      Node pred, curr;
      pred = head;
      curr = head.next;

      // Traverse till we find the first key
      // higher than or equal to tthe given key.
      while (curr.key < searchKey) {
        pred = curr;
        curr = pred.next;
      }

      // Key found, return true.
      if (curr.key == searchKey) {
        return true;
      }

      return false;
//    } finally {
//      lock.unlock();
//    }
  }

}

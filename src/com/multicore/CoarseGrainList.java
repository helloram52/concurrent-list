package com.multicore;

public class CoarseGrainList implements BasicLinkedList {
  private Lock lock;
  private Node head;
  private Node tail;

  CoarseGrainList() {

    // Set the sentinels to integer min/max values
    // so that traversal is easy during insert &
    // other operations.
    head = new Node(Integer.MIN_VALUE);
    tail = new Node(Integer.MAX_VALUE);

    head.next = tail;
    tail.next = null;

    this.lock = new TestTestAndSet();;
  }

  @Override
  public boolean insert(int insertKey) {
    lock.lock();

    Node pred, curr;

    try {
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

    } finally {
      lock.unlock();
    }

    return true;
  }

  @Override
  public boolean delete(int deleteKey) {
    lock.lock();
    Node pred, curr;
    try {
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
  }
}

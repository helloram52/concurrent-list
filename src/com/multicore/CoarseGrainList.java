package com.multicore;

public class CoarseGrainList extends SequentialLinkedList {
  private Lock lock;
  private Node head;
  private Node tail;

  CoarseGrainList() {
    super();
    this.lock = new TestTestAndSet();
  }

  @Override
  public boolean insert(int insertKey) {
    lock.lock();

    try {
      return super.insert(insertKey);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean delete(int deleteKey) {
    lock.lock();
    try {
      return super.delete(deleteKey);
    } finally {
      lock.unlock();
    }
  }
}

package com.multicore;

public class CoarseGrainList extends BasicLinkedList {
  CoarseGrainList() {
    super();
  }

  Lock lock;

  CoarseGrainList(Lock lock) {
    this.lock = lock;
  }

  @Override
  public void insert(int key) {
    lock.lock();
    lock.unlock();
  }

  @Override
  public void delete(int key) {
    lock.lock();
    lock.unlock();
  }

  @Override
  public void search(int key) {

  }
}

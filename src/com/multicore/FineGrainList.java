package com.multicore;

public class FineGrainList extends BasicLinkedList {

  FineGrainList() {
    super();
  }

  @Override
  public boolean insert(int key) {
    return true;
  }

  @Override
  public boolean delete(int key) {
    return true;
  }

  @Override
  public boolean search(int key) {
    return true;
  }
}

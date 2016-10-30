package com.multicore;

public class FineGrainList {
  FineGrainNode head;
  FineGrainNode tail;

  FineGrainList() {
    head = new FineGrainNode(Integer.MIN_VALUE, null);
    tail = new FineGrainNode(Integer.MAX_VALUE, null);

    head.next = tail;
    tail.next = null;
  }

  public boolean insert(int key) {
    return true;
  }

  public boolean delete(int key) {
    return true;
  }

  public boolean search(int key) {
    return true;
  }
}

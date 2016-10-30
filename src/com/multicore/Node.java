package com.multicore;

public class Node {
  public int key;
  public Node next;

  Node(int key) {
    this.setKey(key);
    this.setNext(null);
  }

  public int getKey() {
    return key;
  }

  public void setKey(int key) {
    this.key = key;
  }

  public com.multicore.Node getNext() {
    return next;
  }

  public void setNext(com.multicore.Node next) {
    this.next = next;
  }
}

class FineGrainNode extends Node {
  boolean marked;
  Lock lock;

  FineGrainNode(int key, Lock lock) {
    super(key);
    this.setKey(key);
    this.setNext(null);
    this.setLock(lock);
    this.setMarked(false);
  }

  public boolean isMarked() {
    return marked;
  }

  public void setMarked(boolean marked) {
    this.marked = marked;
  }

  public Lock getLock() {
    return lock;
  }

  public void setLock(Lock lock) {
    this.lock = lock;
  }

}

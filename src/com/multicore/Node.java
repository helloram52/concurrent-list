package com.multicore;

import java.util.concurrent.atomic.AtomicMarkableReference;

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
  FineGrainNode next;

  FineGrainNode(int key) {
    super(key);
    this.setLock();
    this.setNext(null);
    this.setMarked(false);
  }

  public boolean isMarked() {
    return marked;
  }

  public void setNext(FineGrainNode node ) {
    this.next = node;
  }

  public FineGrainNode getNext() {
    return this.next;
  }
  public boolean getMarked() {
    return this.marked;
  }

  public void setMarked(boolean marked) {
    this.marked = marked;
  }

  public Lock getLock() {
    return lock;
  }

  public void setLock() {
    this.lock = new TestTestAndSet();
  }

}

class LockFreeNode extends Node {
  public int key;
  public volatile AtomicMarkableReference<LockFreeNode> next;

  LockFreeNode(int key) {
    super(key);
    this.setNext(new AtomicMarkableReference<LockFreeNode>(null, false));
  }

  public int getKey() {
    return key;
  }

  public void setKey(int key) {
    this.key = key;
  }

  public LockFreeNode getNext() {
    return next.getReference();
  }

  public AtomicMarkableReference<LockFreeNode> getNextAtomicReference() {
    return next;
  }

  public void setNext(AtomicMarkableReference<LockFreeNode> next) {
    this.next = next;
  }

}

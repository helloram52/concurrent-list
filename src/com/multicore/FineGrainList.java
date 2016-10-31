package com.multicore;

public class FineGrainList implements BasicLinkedList {
  private FineGrainNode head;
  private FineGrainNode tail;

  FineGrainList() {
    head = new FineGrainNode(Integer.MIN_VALUE);
    tail = new FineGrainNode(Integer.MAX_VALUE);

    head.next = tail;
    tail.next = null;
  }

  public FineGrainNode getHead() {
    return head;
  }

  public boolean validate(FineGrainNode pred, FineGrainNode curr ) {
    return !pred.getMarked() && !curr.getMarked() && pred.getNext() == curr;
  }

  public boolean insert(int key) {
    while(true) {
      FineGrainNode pred = head;
      FineGrainNode curr = head.getNext();
      while( curr.key < key ) {
        pred = curr;
        curr = curr.getNext();
      }
      pred.getLock().lock();
      try {
        curr.getLock().lock();
        try {
          if( validate(pred, curr) ) {
            if( curr.getKey() == key ) {
              return false;
            }
            else {
              FineGrainNode newNode = new FineGrainNode(key);
              newNode.setNext(curr);
              pred.setNext(newNode);
              return true;
            }
          }
        }
        finally {
          curr.getLock().unlock();
        }
      }
      finally {
        pred.getLock().unlock();
      }
    }
  }

  public boolean delete(int key) {
    while(true) {
      FineGrainNode pred = head;
      FineGrainNode curr = head.getNext();
      while( curr.getKey() == key ) {
        pred = curr;
        curr = curr.getNext();
      }
      pred.getLock().lock();
      try {
        curr.getLock().lock();
        try {
          if(validate(pred, curr)) {
            if( curr.key != key ) {
              return false;
            }
            else {
              curr.setMarked(true);
              pred.setNext(curr.next);
              return true;
            }
          }
        }
        finally {
          curr.getLock().unlock();
        }
      }
      finally {
        pred.getLock().unlock();
      }
    }
  }

  public boolean search(int key) {
    FineGrainNode curr = head;
    while( curr.key < key ) {
      curr = curr.getNext();
    }
    return curr.key == key && !curr.getMarked();
  }
  
}

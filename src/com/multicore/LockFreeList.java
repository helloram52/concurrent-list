package com.multicore;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicMarkableReference;

class LockFreeList implements BasicLinkedList {
  private LockFreeNode head;
  private LockFreeNode tail;


  LockFreeList() {
    head = new LockFreeNode(Integer.MIN_VALUE);
    tail = new LockFreeNode(Integer.MAX_VALUE);

    head.next = new AtomicMarkableReference<>(tail, false);
    tail.next = new AtomicMarkableReference<>(null, false);
  }

  public LockFreeNode getHead() {
    return head;
  }

  public Window find(LockFreeNode head, int key) {

    LockFreeNode pred = null, curr = null, succ = null;
    boolean marked[] = {false};
    boolean result;

    fromBeginning: while(true) {
      pred = head;
      curr = pred.getNext();

      while (true) {

        succ = curr.getNextAtomicReference().get(marked);

        while( marked[0] ) {

          result = pred.getNextAtomicReference().compareAndSet(curr, succ, false, false);

          if( !result ) {
            continue fromBeginning;
          }

          curr = succ;
          succ = curr.getNextAtomicReference().get(marked);

        }

        if( curr.getKey() >= key ) {
          return new Window(pred, curr);
        }

        pred = curr;
        curr = succ;
      }

    }

  }

  public boolean insert(int key) {
    while(true) {

      Window window = find(head, key);
      LockFreeNode pred = window.pred, curr = window.curr;

      if( curr.getKey() == key ) {
        return false;
      }

      else {

        LockFreeNode node = new LockFreeNode(key);
        node.setNext(new AtomicMarkableReference<>(curr, false));

        if( pred.getNextAtomicReference().compareAndSet(curr, node, false, false) ) {
          return true;
        }

      }
    }
  }


  public boolean delete(int key) {
    boolean result;

    while(true) {

      Window window = find(head, key);
      LockFreeNode pred = window.pred, curr = window.curr;

      if( curr.getKey() != key ) {
        return false;
      }

      else {

        LockFreeNode succ = curr.getNext();
        result = curr.getNextAtomicReference().attemptMark(succ, true);

        if( !result ) {
          continue;
        }

        pred.getNextAtomicReference().compareAndSet(curr, succ, false, false);
        return true;
      }

    }
  }

  public boolean search(int key) {
    boolean[] marked = {false};
    LockFreeNode curr = head;

    while( curr.getKey() < key ) {
      curr = curr.getNext();
      curr.getNextAtomicReference().get(marked);
    }

    return curr.getKey() == key && !marked[0];
  }

}

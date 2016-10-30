package com.multicore;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeList extends BasicLinkedList {
  LockFreeList() {
    super();
  }

  public Window find(LockFreeNode head, int key) {
    LockFreeNode pred = null, curr = null, succ = null;
    boolean marked[] = {false};
    boolean result = true;
    while(true) {
      pred = head;
      curr = pred.getNext();
      while (true) {
        succ = curr.getNextAtomicReference().get(marked);
        while( marked[0] ) {
          result = pred.getNextAtomicReference().compareAndSet(curr, succ, false, false);
          if( !result ) {
            break;
          }
          curr = succ;
          succ = curr.getNextAtomicReference().get(marked);
        }
        if( !result ) {
          break;
        }
        if( curr.getKey() >= key ) {
          return new Window(pred, curr);
        }
        pred = curr;
        curr = succ;
      }
    }
  }

  @Override
  public boolean insert(int key) {

    while(true) {
      Window window = find(head, key);
      LockFreeNode pred = window.pred, curr = window.curr;
      if( curr.getKey() == key ) {
        return false;
      }
      else {
        LockFreeNode node = new LockFreeNode(key);
        node.setNext(new AtomicMarkableReference<LockFreeNode>(curr, false));
        if( pred.getNextAtomicReference().compareAndSet(curr, ) )
      }
    }

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

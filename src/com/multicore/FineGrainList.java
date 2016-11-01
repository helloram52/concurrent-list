package com.multicore;

import java.util.concurrent.atomic.AtomicInteger;

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

    //loop until key is inserted
    while(true) {

      FineGrainNode pred = head;
      FineGrainNode curr = head.getNext();

      //get to the pred and curr nodes between which the new node is inserted
      while( curr.key < key ) {
        pred = curr;
        curr = curr.getNext();
      }

      //acquire lock for pred node
      pred.getLock().lock();

      try {

        //acquire lock for curr node
        curr.getLock().lock();
        try {

          //check if the pred and curr are still valid and pred.next == curr
          if( validate(pred, curr) ) {

            //if key exists then return false
            if( curr.getKey() == key ) {
              return false;
            }
            //insert key
            else {
              FineGrainNode newNode = new FineGrainNode(key);
              newNode.setNext(curr);
              pred.setNext(newNode);
              return true;
            }

          }

        } finally {
          curr.getLock().unlock();
        }

      } finally {
        pred.getLock().unlock();
      }

    }
  }

  public boolean delete(int key) {

    while(true) {

      FineGrainNode pred = head;
      FineGrainNode curr = head.getNext();

      //get the greatest key less than given key and least key greater than the given key
      while( curr.getKey() < key ) {
        pred = curr;
        curr = curr.getNext();
      }

      //lock pred node
      pred.getLock().lock();
      try {

        //lock the current node
        curr.getLock().lock();
        try {

          //check if pred and curr are valid and pred.next == curr
          if(validate(pred, curr)) {

            if( curr.key != key ) {
              return false;
            }

            //remove the key from the list
            else {
              curr.setMarked(true);
              pred.setNext(curr.getNext());
              return true;
            }

          }

        } finally {
          curr.getLock().unlock();
        }

      } finally {
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

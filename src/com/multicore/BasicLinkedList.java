package com.multicore;

public abstract class BasicLinkedList {
  public Node head;
  public Node tail;

  BasicLinkedList() {
    // Set the sentinels to integer min/max values
    // so that traversal is easy during insert &
    // other operations.
    head = new Node(Integer.MIN_VALUE);
    tail = new Node(Integer.MAX_VALUE);

    head.next = tail;
    tail.next = null;
  }

  public abstract boolean insert(int key);
  public abstract boolean delete(int key);
  public abstract boolean search(int key);
}

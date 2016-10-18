package com.multicore;

public abstract class BasicLinkedList {
  private Node head;
  private Node tail;

  BasicLinkedList() {
    head = new Node(Integer.MIN_VALUE);
    tail = new Node(Integer.MAX_VALUE);

    head.next = tail;
    tail.next = null;
  }

  public abstract void insert(int key);
  public abstract void delete(int key);
  public abstract void search(int key);
}

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

  public abstract void insert();
  public abstract void delete();
  public abstract void search();
}

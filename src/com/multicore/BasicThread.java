package com.multicore;

import java.util.concurrent.atomic.AtomicInteger;

public class BasicThread implements Runnable {

  public int getID() {
    return ID;
  }

  private int ID;
  private AtomicInteger counter;
  private Range range;
  private BasicLinkedList list;

  public Range getRange() {
    return range;
  }

  public int getStartTaskPos(){
    return range.x;
  }

  public int getEndTaskPos() {
    return range.y;
  }

  public BasicThread(int ID, BasicLinkedList list, AtomicInteger counter, Range range) {
    this.ID = ID;
    this.counter = counter;
    this.range=range;
    this.list = list;
  }

  private void enterCriticalSection() {
    long i = 0;
    while (i++ < RunParameters.NUMBER_TO_COUNT_IN_CS.value) {
      this.counter.incrementAndGet();
    }

//    Utils.log("Thread '" + this.ID + "', counter value: '"+ this.counter.get() + "'");
  }


  public void run() {
    for (int i = 0; i < RunParameters.NUMBER_OF_CS_RUNS_PER_THREAD.value; i++) {

      if( getRange() != null ) {
        //System.out.println("Thread id="+getID()+" startTaskPos="+startTaskPos+" endTaskPos="+endTaskPos);
        for( int taskPos=getStartTaskPos(); taskPos<=getEndTaskPos();taskPos++ ) {

          int[] task= Utils.getTask(taskPos);

          if( task[0] == Utils.INSERT ) {
            list.insert(task[1]);
            System.out.println(getID()+" insert ="+task[1]);
          }
          else if( task[0] == Utils.DELETE ){
            list.delete(task[1]);
            System.out.println(getID()+" delete ="+task[1]);
          }
          else if ( task[0] == Utils.SEARCH ) {
            list.search(task[1]);
            System.out.println(getID()+" search ="+task[1]);
          }
          else {
            Utils.log("Thread id="+getID()+" Illegal operation:"+ task[0]);
          }
        }
      }
    }
  }

}

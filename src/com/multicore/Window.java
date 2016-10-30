package com.multicore;

/**
 * Created by ram on 10/30/16.
 */
public class Window {
    LockFreeNode pred;
    LockFreeNode curr;
    Window( LockFreeNode pred, LockFreeNode curr ) {
        this.pred = pred;
        this.curr = curr;
    }
}

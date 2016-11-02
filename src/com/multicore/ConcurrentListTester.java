package com.multicore;

/**
 * Created by ram on 10/30/16.
 */
public final class ConcurrentListTester {

    public static boolean validateList(BasicLinkedList list) {
        Node head = null;

        if( list instanceof CoarseGrainList ) {
            head = ((CoarseGrainList) list).getHead();
        }
        else if( list instanceof FineGrainList ) {
            head = ((FineGrainList) list).getHead();
        }
        else if( list instanceof LockFreeList ) {
            head = ((LockFreeList) list).getHead();
        }

        Node temp = head, pred;
        while (temp != null && temp.next != null) {
            pred = temp.next;
            if (temp.key >= pred.key) {
                return false;
            }
            temp = temp.next;
        }

        return true;
    }
}

package com.multicore;

/**
 * Created by ram on 10/30/16.
 */
public class ConcurrentListTester {

    ConcurrentListTester() {
    }

    public static boolean compareLists( BasicLinkedList list1, BasicLinkedList list2  ) {

        if( list1 instanceof CoarseGrainList || list2 instanceof CoarseGrainList ) {

            if( list1 instanceof  CoarseGrainList ) {
                Node currNodeListOne = ((CoarseGrainList) list1).getHead();
                Node currNodeListTwo = ((SequentialLinkList) list2).getHead();
            }
            else {
                Node currNodeListOne = ((SequentialLinkList) list1).getHead();
                Node currNodeListTwo = ((CoarseGrainList) list2).getHead();
            }

            while( currNodeListOne != null && currNodeListTwo != null ) {
                if( currNodeListOne.getKey() != currNodeListTwo.getKey() ) {
                    return false;
                }
                currNodeListOne = currNodeListOne.getNext();
                currNodeListTwo = currNodeListTwo.getNext();
            }

            return currNodeListOne == null && currNodeListTwo == null;

        }
        else if( list1 instanceof FineGrainList || list2 instanceof FineGrainList ) {

            if( list1 instanceof  CoarseGrainList ) {
                FineGrainNode currNodeListOne = ((FineGrainList) list1).getHead();
                Node currNodeListTwo = ((SequentialLinkList) list2).getHead();
            }
            else {
                Node currNodeListOne = ((SequentialLinkList) list1).getHead();
                FineGrainNode currNodeListTwo = ((FineGrainList) list1).getHead();
            }

            while (currNodeListOne != null && currNodeListTwo != null) {
                if (currNodeListOne.getKey() != currNodeListTwo.getKey()) {
                    return false;
                }
                currNodeListOne = currNodeListOne.getNext();
                currNodeListTwo = currNodeListTwo.getNext();
            }

            return currNodeListOne == null && currNodeListTwo == null;
        }
        else if( list1 instanceof LockFreeList && list2 instanceof LockFreeList ) {

            if (list1 instanceof FineGrainList && list2 instanceof FineGrainList) {

                FineGrainNode currNodeListOne = ((FineGrainList) list1).getHead();
                FineGrainNode currNodeListTwo = ((FineGrainList) list2).getHead();

                while (currNodeListOne != null && currNodeListTwo != null) {
                    if (currNodeListOne.getKey() != currNodeListTwo.getKey()) {
                        return false;
                    }
                    currNodeListOne = currNodeListOne.getNext();
                    currNodeListTwo = currNodeListTwo.getNext();
                }

                return currNodeListOne == null && currNodeListTwo == null;
            }

        }
        return false;
    }

}

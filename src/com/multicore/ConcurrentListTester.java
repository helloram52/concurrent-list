package com.multicore;

/**
 * Created by ram on 10/30/16.
 */
public class ConcurrentListTester {

    ConcurrentListTester() {
    }

    public static boolean compareLists( BasicLinkedList list1, BasicLinkedList list2  ) {

        Node currNodeListOne = null;
        Node currNodeListTwo = null;

        if( list1 instanceof SequentialLinkedList ) {

            currNodeListOne = ((SequentialLinkedList) list1).getHead();

            if( list2 instanceof CoarseGrainList ) {
                currNodeListTwo = ((CoarseGrainList) list2).getHead();
            }
            else if( list2 instanceof FineGrainList ) {
                currNodeListTwo = ((FineGrainList) list2).getHead();
            }
            else if( list2 instanceof LockFreeList ) {
                currNodeListTwo = ((LockFreeList) list2).getHead();
            }
        }
        else {

            currNodeListTwo = ((SequentialLinkedList) list2).getHead();

            if( list1 instanceof CoarseGrainList ) {
                currNodeListOne = ((CoarseGrainList) list1).getHead();
            }
            else if( list1 instanceof FineGrainList ) {
                currNodeListOne = ((FineGrainList) list1).getHead();
            }
            else if( list1 instanceof LockFreeList ) {
                currNodeListOne = ((LockFreeList) list1).getHead();
            }
        }

        if( currNodeListOne == null || currNodeListTwo == null ) {
            return false;
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

    public static void printLists( BasicLinkedList list1, BasicLinkedList list2 ) {

        if( list1 == null || list2 == null ) {
            Utils.logInfo("List1 or List2 is empty");
        }

        Utils.logWarning("List1:");
        printList(list1);
        Utils.logWarning("List2:");
        printList(list2);
    }

    public static void printList( BasicLinkedList list ) {

        String SPACE = " ";
        Node currNode = null;

        if( list instanceof SequentialLinkedList ) {

            currNode = ((SequentialLinkedList) list).getHead();
        }
        else if( list instanceof CoarseGrainList ) {
            currNode = ((CoarseGrainList) list).getHead();
        }
        else if( list instanceof FineGrainList ) {
            currNode = ((FineGrainList) list).getHead();
        }
        else if( list instanceof LockFreeList ) {
            currNode = ((LockFreeList) list).getHead();
        }

        StringBuilder stringBuilder = new StringBuilder();
        while( currNode != null ) {
            stringBuilder.append( currNode.getKey() );
            stringBuilder.append(SPACE);
            currNode = currNode.getNext();
        }
        Utils.logWarning(stringBuilder.toString().trim());
    }

}

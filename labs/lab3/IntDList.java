/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Linda Deng (1/26/2022), Ishani Basaks
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        int count = 0;
        DNode current = _front;
        while (current != null) {
            count++;
            current = current._next;
        }
        return count;
    }

    /**
     * @param index index of node to return,
     *              where index = 0 returns the first node,
     *              index = 1 returns the second node, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return The node at index index
     */
    private DNode getNode(int index) {
        DNode current = _front;
        for (int i = 0; i < index; i++) {
            current = current._next;
        }
        return current;
    }

    /**
     * @param index index of element to return,
     *              where index = 0 returns the first element,
     *              index = 1 returns the second element,and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return The integer value at index index
     */
    public int get(int index) {
        return getNode(index)._val;
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        DNode newDNode = new DNode(d);
        if (_front == null) {
            _front = _back = newDNode;
        } else {
            newDNode._next = _front;
            _front._prev = newDNode;
            _front = newDNode;
        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        DNode newDNode = new DNode(d);
        if (_front == null) {
            _front = _back = newDNode;
        } else {
            newDNode._prev = _back;
            _back._next = newDNode;
            _back = newDNode;
        }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position, and so onh.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size.
     */
    public void insertAtIndex(int d, int index) {
        DNode insert = new DNode(d);
        if (_front == null) {
            _front = _back = new DNode(d);
        } else if (index == 0) {
            DNode after = _front;
            insert._next = after;
            after._prev = insert;
            _front = insert;
        } else {
            int i = 0;
            DNode before = _front;
            while (i < index - 1) {
                before = before._next;
                i++;
            }
            if (before == _back) {
                insert._prev = before;
                before._next = insert;
                _back = insert;
            } else {
                DNode after = before._next;
                before._next = insert;
                insert._prev = before;
                insert._next = after;
                after._prev = insert;
            }
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     * Assume `deleteFront` is never called on an empty IntDList.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        if (_front == null) {
            return 0;
        } else {
            int delVal = _front._val;
            if (_front._next == null) {
                _front = _back = null;
            } else {
                DNode newFront = _front._next;
                newFront._prev = null;
                _front = newFront;
            }
            return delVal;
        }
    }

    /**
     * Removes the last item in the IntDList and returns it.
     * Assume `deleteBack` is never called on an empty IntDList.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        if (_front == null) {
            return 0;
        } else {
            int delVal = _back._val;
            if (_back._prev == null) {
                _front = _back = null;
            } else {
                DNode newBack = _back._prev;
                newBack._next = null;
                _back = newBack;
            }
            return delVal;
        }
    }

    /**
     * @param index index of element to be deleted,
     *              where index = 0 returns the first element,
     *              index = 1 will delete the second element, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        int delInt = _front._val;
        if (_front == _back) {
            _front = _back = null;
            return delInt;
        } else if (index == 0) {
            DNode deletion = _front;
            _front = deletion._next;
            _front._prev = null;
            return deletion._val;
        } else {
            int i = 0;
            DNode before = _front;
            while (i < index - 1) {
                before = before._next;
                i++;
            }
            DNode deletion = before._next;
            delInt = deletion._val;
            if (deletion == _back) {
                before._next = null;
                _back = before;
                return delInt;
            } else {
                DNode after = deletion._next;
                before._next = after;
                after._prev = before;
                return delInt;
            }
        }
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        if (size() == 0) {
            return "[]";
        }
        String str = "[";
        DNode curr = _front;
        for (; curr._next != null; curr = curr._next) {
            str += curr._val + ", ";
        }
        str += curr._val + "]";
        return str;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /**
         * Previous DNode.
         */
        protected DNode _prev;
        /**
         * Next DNode.
         */
        protected DNode _next;
        /**
         * Value contained in DNode.
         */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }
}


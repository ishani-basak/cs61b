/**
 * Simple Red-Black tree implementation, where the keys are of type T.
 @author Ishani Basak
 */
public class RedBlackTree<T extends Comparable<T>> {

    /** Root of the tree. */
    private RBTreeNode<T> root;

    /**
     * Empty constructor.
     */
    public RedBlackTree() {
        root = null;
    }

    /**
     * Constructor that builds this from given BTree (2-3-4) TREE.
     *
     * @param tree BTree (2-3-4 tree).
     */
    public RedBlackTree(BTree<T> tree) {
        BTree.Node<T> btreeRoot = tree.root;
        root = buildRedBlackTree(btreeRoot);
    }

    /**
     * Builds a RedBlack tree that has isometry with given 2-3-4 tree rooted at
     * given node r, and returns the root node.
     *
     * @param r root of the 2-3-4 tree.
     * @return root of the Red-Black tree for given 2-3-4 tree.
     */
    RBTreeNode<T> buildRedBlackTree(BTree.Node<T> r) {
        // OPTIONAL: YOUR CODE HERE
        return null;
    }

    /**
     * Rotates the (sub)tree rooted at given NODE to the right, and returns the
     * new root of the (sub)tree. If rotation is not possible somehow,
     * immediately return the input NODE.
     *
     * @param node root of the given (sub)tree.
     * @return new root of the (sub)tree.
     */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        RBTreeNode<T> newRoot = node.left;
        RBTreeNode<T> newRight = node;
        RBTreeNode<T> temp = newRoot.right;
        newRight.left = temp;
        newRoot.right = newRight;
        boolean tempColor = newRoot.isBlack;
        newRoot.isBlack = newRight.isBlack;
        newRight.isBlack = tempColor;
        return newRoot;
    }

    /**
     * Rotates the (sub)tree rooted at given NODE to the left, and returns the
     * new root of the (sub)tree. If rotation is not possible somehow,
     * immediately return the input NODE.
     *
     * @param node root of the given (sub)tree.
     * @return new root of the (sub)tree.
     */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        RBTreeNode<T> newRoot = node.right;
        RBTreeNode<T> newLeft = node;
        RBTreeNode<T> temp = newRoot.left;
        newLeft.right = temp;
        newRoot.left = newLeft;
        boolean tempColor = newRoot.isBlack;
        newRoot.isBlack = newLeft.isBlack;
        newLeft.isBlack = tempColor;
        return newRoot;
    }

    /**
     * Flips the color of NODE and its children. Assume that NODE has
     * both left and right children.
     *
     * @param node tree node
     */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    /**
     * Returns whether a given NODE is red. Null nodes (children of leaves) are
     * automatically considered black.
     *
     * @param node node
     * @return node is red.
     */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    /**
     * Insert given item into this tree.
     *
     * @param item item
     */
    void insert(T item) {
        root = insert(root, item);
        root.isBlack = true;
    }

    /**
     * Recursively insert ITEM into this tree. Returns the (new) root of the
     * subtree rooted at given NODE after insertion. NODE == null implies that
     * we are inserting a new node at the bottom.
     *
     * @param node node
     * @param item item
     * @return (new) root of the subtree rooted at given node.
     */
    private RBTreeNode<T> insert(RBTreeNode<T> node, T item) {

        // FIXME: if tree is empty, let NODE be the root with black color

        // Insert (return) new red leaf node.
        if (node == null) {
            node = new RBTreeNode<T>(false, item);
            return node;
        }

        // Handle normal binary search tree insertion.
        int comp = item.compareTo(node.item);
        if (comp == 0) {
            return node; // do nothing.
        } else if (comp < 0) {
            node.left = insert(node.left, item);
        } else {
            node.right = insert(node.right, item);
        }

        // handle case C and "Right-leaning" situation.
        if (isRed(node.right) && !isRed(node.left)) {
            node = rotateLeft(node);
        }

        // handle case B
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }

        // handle case A
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }
        return node;
    }

    /** Public accesser method for the root of the tree.*/
    public RBTreeNode<T> graderRoot() {
        return root;
    }


    /**
     * RedBlack tree node.
     *
     * @param <T> type of item.
     */
    static class RBTreeNode<T> {

        /** Item. */
        protected final T item;

        /** True if the node is black. */
        protected boolean isBlack;

        /** Pointer to left child. */
        protected RBTreeNode<T> left;

        /** Pointer to right child. */
        protected RBTreeNode<T> right;

        /**
         * A node that is black iff BLACK, containing VALUE, with empty
         * children.
         */
        RBTreeNode(boolean black, T value) {
            this(black, value, null, null);
        }

        /**
         * Node that is black iff BLACK, contains VALUE, and has children
         * LFT AND RGHT.
         */
        RBTreeNode(boolean black, T value,
                   RBTreeNode<T> lft, RBTreeNode<T> rght) {
            isBlack = black;
            item = value;
            left = lft;
            right = rght;
        }
    }

}

package treePractice;

/**
 * A class to represent an AVL binary search tree.
 * AVL trees self-balance to maintain a heights that only differ by at most 1 between the left and right child
 *
 * @author Bret Jackson, Mayank Jaiswal
 */
public class AVLTree<E extends Comparable<E>> extends BinarySearchTree<E> {

    // Inner class that extends Node to add a heightFromLeaf property to calculate the balance factor
    protected static class AVLNode<E> extends Node<E>{
        public int heightFromLeaf;

        public AVLNode(E data) {
            super(data);
            heightFromLeaf = 1; // nodes are created at the leaves so start with a height of 1
        }
    }

    /**
     * Convenience funtion to check for null and convert nodes to AVLNodes when getting the heightfromLeaf
     * @param node
     * @return
     */
    private int heightFromLeaf(Node<E> node){
        if (node == null){
            return 0;
        }
        return ((AVLNode<E>)node).heightFromLeaf;
    }

    /**
     * Calculates the balance factor for node
     * @param node
     * @return
     */
    private int getBalance(Node<E> node) {
        if (node == null){
            return 0;
        }
        return heightFromLeaf(node.left) - heightFromLeaf(node.right);
    }

    /**
     * Left rotation of x around the pivot node
     *    pivot                      x
     *    /  \                     /  \
     *   T1   x        ----->  pivot  T3
     *       / \                /  \
     *     T2  T3              T1  T2
     * @param pivot
     * @return
     */
    private AVLNode<E> rotateLeft(AVLNode<E> pivot) {
        if (pivot.right != null) {
            Node<E> newPivot = pivot.right;
            Node<E> leftHolder = newPivot.left;
            newPivot.parent = pivot.parent;
            newPivot.left = pivot;
            pivot.right = leftHolder;
            if (leftHolder != null) {
                leftHolder.parent = pivot;
            }
            pivot.parent = newPivot;

            pivot.heightFromLeaf = Math.max(heightFromLeaf(pivot.left), heightFromLeaf(pivot.right)) + 1;
            AVLNode<E> x = (AVLNode)newPivot;

            x.heightFromLeaf = Math.max(heightFromLeaf(x.left), heightFromLeaf(x.right)) + 1;

            return x; // new root
        }

        return pivot; // same root, no rotation
    }

    /**
     * Right rotation of x around the pivot node
     *         pivot                 x
     *         /  \                /  \
     *        x   T3    ----->   T1   pivot
     *       / \                      /  \
     *     T1  T2                    T2  T3
     * @param pivot
     * @return
     */
    private AVLNode<E> rotateRight(AVLNode<E> pivot) {
        if (pivot.left != null) {
            Node<E> newPivot = pivot.left;
            Node<E> rightHolder = newPivot.right;
            newPivot.parent = pivot.parent;
            newPivot.right = pivot;
            pivot.left = rightHolder;
            if (rightHolder != null) {
                rightHolder.parent = pivot;
            }
            pivot.parent = newPivot;

            pivot.heightFromLeaf = Math.max(heightFromLeaf(pivot.left), heightFromLeaf(pivot.right)) + 1;
            AVLNode<E> x = (AVLNode)newPivot; // fix me. You may need to cast a Node<E> reference to an AVLNode<E>

            x.heightFromLeaf = Math.max(heightFromLeaf(x.left), heightFromLeaf(x.right)) + 1;

            return x;
        }
       
        return pivot; // same root, no rotation
    }


    /**
     * Starter method add.
     *
     * @param item The object being inserted
     * @return true if the object is inserted, false
     * if the object already exists in the tree
     * @pre The object to insert must implement the
     * Comparable interface.
     */
    @Override
    public boolean add(E item) {
        root = add((AVLNode<E>) root, null, item);
        return addReturn;
    }

    /**
     * Recursive add method.
     *
     * @param localRoot The local root of the subtree
     * @param item      The object to be inserted
     * @return The new local root that now contains the
     * inserted item
     * @post The data field addReturn is set true if the item is added to
     * the tree, false if the item is already in the tree.
     */
    private AVLNode<E> add(AVLNode<E> localRoot, AVLNode<E> parent, E item) {
        /* 1. Perform the normal BST insertion */
        if (localRoot == null){
            addReturn = true;
            AVLNode<E> newNode = new AVLNode<E>(item);
            newNode.parent = parent;
            return newNode;
        } else if (item.compareTo(localRoot.data) == 0) {
            // item is equal to localRoot.data
            addReturn = false;
            return localRoot;
        } else if (item.compareTo(localRoot.data) < 0) {
            // item is less than localRoot.data
            localRoot.left = add((AVLNode<E>)localRoot.left, localRoot, item);
        } else {
            // item is greater than localRoot.data
            localRoot.right = add((AVLNode<E>)localRoot.right, localRoot, item);
        }

        /* 2. Update height of this ancestor node */
        localRoot.heightFromLeaf = 1 + Math.max(heightFromLeaf(localRoot.left), heightFromLeaf(localRoot.right));

        /* 3. Get the balance factor of this ancestor node to check whether this node became unbalanced */
        int balance = getBalance(localRoot);

        // If this node becomes unbalanced, then there are 4 cases

        // Left Left Case
        if (balance > 1 && item.compareTo(localRoot.left.data) < 0) {
            return rotateRight(localRoot);
        }

        // Right Right Case
        if (balance < -1 && item.compareTo(localRoot.right.data) > 0)
            return rotateLeft(localRoot);

        // Left Right Case
        if (balance > 1 && item.compareTo(localRoot.left.data) > 0)
        {
            localRoot.left = rotateLeft((AVLNode<E>) localRoot.left);
            return rotateRight(localRoot);
        }

        // Right Left Case
        if (balance < -1 && item.compareTo(localRoot.right.data) < 0)
        {
            localRoot.right = rotateRight((AVLNode<E>) localRoot.right);
            return rotateLeft(localRoot);
        }

        return localRoot;
    }

    /**
     * Starter method delete.
     *
     * @param target The object to be deleted
     * @return The object deleted from the tree
     * or null if the object was not in the tree
     * @throws ClassCastException if target does not implement
     *                            Comparable
     * @post The object is not in the tree.
     */
    @Override
    public E delete(E target) {
        root = delete((AVLNode<E>)root, target);
        return deleteReturn;
    }

    /**
     * Recursive delete method.
     *
     * @param localRoot The root of the current subtree
     * @param item      The item to be deleted
     * @return The modified local root that does not contain
     * the item
     * @post The item is not in the tree;
     * deleteReturn is equal to the deleted item
     * as it was stored in the tree or null
     * if the item was not found.
     */
    private AVLNode<E> delete(AVLNode<E> localRoot, E item) {
        if (localRoot == null) {
            // item is not in the tree.
            deleteReturn = null;
            return localRoot;
        }

        // Search for item to delete.
        int compResult = item.compareTo(localRoot.data);
        if (compResult < 0) {
            // item is smaller than localRoot.data.
            localRoot.left = delete((AVLNode<E>)localRoot.left, item);
            if (localRoot.left != null) {
                localRoot.left.parent = localRoot;
            }
        } else if (compResult > 0) {
            // item is larger than localRoot.data.
            localRoot.right = delete((AVLNode<E>)localRoot.right, item);
            if (localRoot.right != null) {
                localRoot.right.parent = localRoot;
            }
        } else {
            // item is at local root.
            deleteReturn = localRoot.data;
            // node with only one child or no child
            if ((localRoot.left == null) || (localRoot.right == null)) {
                AVLNode<E> temp = null;
                if (temp == localRoot.left) {
                    temp = (AVLNode<E>)localRoot.right;
                }
                else {
                    temp = (AVLNode<E>)localRoot.left;
                }

                // No child case
                if (temp == null) {
                    temp = localRoot;
                    localRoot = null;
                }
                else { // One child case
                    localRoot = temp; // Copy the contents of the non-empty child
                }
            }
            else
            {
                // Search for the inorder predecessor (ip) and
                // replace deleted node's data with ip.
                Node<E> temp = findLargestChild(localRoot.left);

                // Copy the inorder predecessors's data to this node
                localRoot.data = temp.data;

                // Delete the inorder successor
                localRoot.right = delete((AVLNode<E>)localRoot.right, temp.data);
            }

        }

        // If the tree had only one node then return
        if (localRoot == null)
            return localRoot;

        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
        localRoot.heightFromLeaf = Math.max(heightFromLeaf(localRoot.left), heightFromLeaf(localRoot.right)) + 1;

        // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
        // this node became unbalanced)
        int balance = getBalance(localRoot);

        // If this node becomes unbalanced, then there are 4 cases
        // Left Left Case
        if (balance > 1 && getBalance(localRoot.left) >= 0)
            return rotateRight(localRoot);

        // Left Right Case
        if (balance > 1 && getBalance(localRoot.left) < 0)
        {
            localRoot.left = rotateLeft((AVLNode<E>)localRoot.left);
            return rotateRight(localRoot);
        }

        // Right Right Case
        if (balance < -1 && getBalance(localRoot.right) <= 0)
            return rotateLeft(localRoot);

        // Right Left Case
        if (balance < -1 && getBalance(localRoot.right) > 0)
        {
            root.right = rotateRight((AVLNode<E>)localRoot.right);
            return rotateLeft(localRoot);
        }

        return localRoot;
    }

    /**
     * Find the node that is the
     * inorder predecessor
     *
     * @param parent The parent of possible inorder
     *               predecessor (ip)
     * @return The data in the ip
     */
    private Node<E> findLargestChild(Node<E> parent) {
        Node<E> current = parent;

        /* loop down to find the leftmost leaf */
        while (current.right != null)
            current = current.right;

        return current;
    }

}

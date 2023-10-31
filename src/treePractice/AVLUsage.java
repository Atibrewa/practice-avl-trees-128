package treePractice;

public class AVLUsage {
    public static void main(String[] args){
        AVLTree<Integer> avlTree = new AVLTree<>();
        /* Constructing tree given in the above figure */
        avlTree.add(9);
        avlTree.add(5);
        avlTree.add(10);
        avlTree.add(0);
        avlTree.add(6);
        avlTree.add(11);
        avlTree.add(-1);
        avlTree.add(1);
        avlTree.add(2);

        /* The constructed AVL Tree would be
                9
               / \
              1  10
            / \   \
           0  5   11
          /  / \
        -1  2  6
        */
        System.out.println("Preorder traversal: "+avlTree.toString());

        avlTree.delete(10);

        /* The AVL Tree after deletion of 10
              1
            /  \
           0    9
          /    / \
        -1    5  11
            / \
           2  6
        */
        System.out.println("Deleted 10...");
        System.out.println("Preorder traversal after deletion of 10 : " + avlTree.toString());
    }
}

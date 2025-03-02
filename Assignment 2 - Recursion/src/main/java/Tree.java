import java.util.*;

public class Tree<E extends Comparable<? super E>> {
    private BinaryTreeNode root;  // Root of tree
    private String name;     // Name of tree

    /**
     * Create an empty tree
     *
     * @param label Name of tree
     */
    public Tree(String label) {
        name = label;
    }

    /**
     * Create BST from ArrayList
     *
     * @param arr   List of elements to be added
     * @param label Name of tree
     */
    public Tree(ArrayList<E> arr, String label) {
        name = label;
        for (E key : arr) {
            insert(key);
        }
    }

    /**
     * Create BST from Array
     *
     * @param arr   List of elements to be added
     * @param label Name of  tree
     */
    public Tree(E[] arr, String label) {
        name = label;
        for (E key : arr) {
            insert(key);
        }
    }

    /**
     * Return a string containing the tree contents as a tree with one node per line
     */
    public String toString() {
        return String.format("%s: ", name) + "\n" + toString(root, null, 0);
    }

    public String toString(BinaryTreeNode node, BinaryTreeNode parentNode, int depth) {
        String returnString = "";
        if (node.right != null) {
            returnString += toString(node.right, node, depth + 1);
        }

        String parentString = parentNode != null ? parentNode.key.toString() : "no parent";
        returnString += String.format("%s %s [%s]\n", "\t".repeat(depth), node.key, parentString);

        if (node.left != null) {
            returnString += toString(node.left, node, depth + 1);
        }

        return returnString;
    }

    /**
     * Return a string containing the tree contents as a single line
     */
    public String inOrderToString() {
        StringBuilder returnString = new StringBuilder(String.format("%s: ", name));
        for (BinaryTreeNode child : inOrderChildren()) {
            returnString.append(child.key).append(" ");
        }
        return returnString.deleteCharAt(returnString.length() - 1).toString();
    }

    /**
     * reverse left and right children recursively
     */
    public void flip() {
        flip(root);
    }

    private void flip(BinaryTreeNode node) {
        BinaryTreeNode temp = node.right;
        node.right = node.left;
        node.left = temp;
        if (node.left != null) {
            flip(node.left);
        }
        if (node.right != null) {
            flip(node.right);
        }
    }

    /**
     * Returns the in-order successor of the specified node
     * @param node node from which to find the in-order successor
     */
    public BinaryTreeNode inOrderSuccessor(BinaryTreeNode node) {
        // In order successor will be on the right of the current node if it exists
        if (node.right != null) {
            node = node.right;
            // It is the leftmost right node
            while (node.left != null) {
                node = node.left;
            }
            return node;
        }

        // Otherwise it will be an ancestor such that the previous ancestor is the left child
        BinaryTreeNode parent = node.parent;
        while (parent != null) {
            // If the parent's left child is the node
            if (parent.left.key.compareTo(node.key) == 0) {
                return parent;
            }

            node = parent;
            parent = node.parent;
        }
        return parent;
    }

    /**
     * Counts number of nodes in specified level
     *
     * @param level Level in tree, root is zero
     * @return count of number of nodes at specified level
     */
    public int nodesInLevel(int level) {
        return nodesInLevel(level, 0, root);
    }

    public int nodesInLevel(int targetLevel, int currentLevel, BinaryTreeNode currentNode) {
        if (currentNode == null || currentNode.key == null) {
            return 0;
        }
        if (targetLevel == currentLevel) {
            return 1;
        }

        return nodesInLevel(targetLevel, currentLevel + 1, currentNode.right) + nodesInLevel(targetLevel, currentLevel + 1, currentNode.left);

    }

    /**
     * Print all paths from root to leaves
     */
    public void printAllPaths() {
        printAllPaths(root, new ArrayList<>());
    }

    private void printAllPaths(BinaryTreeNode node, List<E> path) {
        if (node == null) {
            return;
        }
        path.add(node.key);
        if (node.left == null && node.right == null) {
            for (E value : path) {
                System.out.print(value + " ");
            }
            System.out.println();
            path.remove(node.key);
        }
        else {
            printAllPaths(node.left, path);
            printAllPaths(node.right, path);
            path.remove(node.key);
        }
    }

    /**
     * Counts all non-null binary search trees embedded in tree
     *
     * @return Count of embedded binary search trees
     */
    public int countBST() {
        return countBST(root);
    }

    private int countBST(BinaryTreeNode node) {
        int count = isBST(node) ? 1 : 0;
        if (node.left != null) {
            count += countBST(node.left);
        }
        if (node.right != null) {
            count += countBST(node.right);
        }
        return count;
    }

    private boolean isBST(BinaryTreeNode node) {
        if (node == null) {
            return true;
        }
        if (node.left != null && maxValue(node.left).compareTo(node.key) > 0) {
            return false;
        }

        if (node.right != null && maxValue(node.right).compareTo(node.key) < 0) {
            return false;
        }

        return isBST(node.left) && isBST(node.right);
    }

    private E maxValue(BinaryTreeNode target) {
        E max = target.key;

        if (target.left != null && target.left.key.compareTo(max) > 0) {
            max = target.left.key;
        }
        if (target.right != null && target.right.key.compareTo(max) > 0) {
            max = target.right.key;
        }

        if (target.left != null) {
            E maxLeft = maxValue(target.left);
            if (maxLeft.compareTo(max) > 0) {
                max = maxLeft;
            }
        }

        if (target.right != null) {
            E maxLeft = maxValue(target.right);
            if (maxLeft.compareTo(max) > 0) {
                max = maxLeft;
            }
        }
        return max;
    }

    private E minValue(BinaryTreeNode target) {
        E min = target.key;
        if (target.left != null && target.left.key.compareTo(min) < 0) {
            min = target.left.key;

        }
        if (target.right != null && target.right.key.compareTo(min) < 0) {
            min = target.right.key;

        }

        if (target.left != null) {
            E minLeft = minValue(target.left);

            if (minLeft.compareTo(min) < 0) {
                min = minLeft;
            }
        }

        if (target.right != null) {
            E minRight = minValue(target.right);

            if (minRight.compareTo(min) < 0) {
                min = minRight;
            }
        }

        return min;
    }

    /**
     * Insert into a bst tree; duplicates are allowed
     *
     * @param x the item to insert.
     */
    public void insert(E x) {
        root = insert(x, root, null);
    }

    public BinaryTreeNode getByKey(E key) {
        return getByKey(root, key);
    }

    private BinaryTreeNode getByKey(BinaryTreeNode node, E key) {
        if (node.key == key) {
            return node;
        }

        if (node.key.compareTo(key) < 0) {
            return getByKey(node.right, key);
        }

        return getByKey(node.left, key);
    }

    /**
     * Balance the tree
     */
    public void balanceTree() {
        ArrayList<BinaryTreeNode> inOrderChildren = inOrderChildren();
        root = balanceTree(inOrderChildren, 0, inOrderChildren.size() - 1);
    }

    private BinaryTreeNode balanceTree(ArrayList<BinaryTreeNode> nodes, int leftIndex, int rightIndex) {
        if (leftIndex > rightIndex) {
            return null;
        }

        int middle = (rightIndex + leftIndex) / 2;
        E value = nodes.get(middle).key;
        BinaryTreeNode node = new BinaryTreeNode(value);

        node.left = balanceTree(nodes, leftIndex, middle - 1);
        node.right = balanceTree(nodes, middle + 1, rightIndex);


        return node;

    }

    /**
     * Internal method to insert into a subtree.
     * In tree is balanced, this routine runs in O(log n)
     *
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private BinaryTreeNode insert(E x, BinaryTreeNode t, BinaryTreeNode parent) {
        if (t == null) return new BinaryTreeNode(x, null, null, parent);

        int compareResult = x.compareTo(t.key);
        if (compareResult < 0) {
            t.left = insert(x, t.left, t);
        } else {
            t.right = insert(x, t.right, t);
        }

        return t;
    }

    private ArrayList<BinaryTreeNode> inOrderChildren() {
        return inOrderChildren(root);
    }

    private ArrayList<BinaryTreeNode> inOrderChildren(BinaryTreeNode node) {
        if (node == null) {
            return new ArrayList<>();
        }

        ArrayList<BinaryTreeNode> children = inOrderChildren(node.left);

        children.add(node);

        children.addAll(inOrderChildren(node.right));
        return children;
    }


    /**
     * Internal method to find an item in a subtree.
     * This routine runs in O(log n) as there is only one recursive call that is executed and the work
     * associated with a single call is independent of the size of the tree: a=1, b=2, k=0
     *
     * @param x is item to search for.
     * @param t the node that roots the subtree.
     *          SIDE EFFECT: Sets local variable curr to be the node that is found
     * @return node containing the matched item.
     */
    private boolean contains(E x, BinaryTreeNode t) {
        if (t == null)
            return false;

        int compareResult = x.compareTo(t.key);

        if (compareResult < 0)
            return contains(x, t.left);
        else if (compareResult > 0)
            return contains(x, t.right);
        else {
            return true;    // Match
        }
    }

    // Basic node stored in unbalanced binary trees
    public class BinaryTreeNode {
        E key;            // The data/key for the node
        BinaryTreeNode left;   // Left child
        BinaryTreeNode right;  // Right child
        BinaryTreeNode parent; //  Parent node

        // Constructors
        BinaryTreeNode(E theElement) {
            this(theElement, null, null, null);
        }

        BinaryTreeNode(E theElement, BinaryTreeNode lt, BinaryTreeNode rt, BinaryTreeNode pt) {
            key = theElement;
            left = lt;
            right = rt;
            parent = pt;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Node:");
            sb.append(key);
            if (parent == null) {
                sb.append("<>");
            } else {
                sb.append("<");
                sb.append(parent.key);
                sb.append(">");
            }

            return sb.toString();
        }
    }
}

public class PriorityQueue<E extends Comparable<E>> {

    private Node root;
    public PriorityQueue() {
    }

    /**
     * Insert a value into the Priority Queue
     */
    public void enqueue(E value) {
        root = merge(root, new Node(value));
    }

    /**
     * Remove the minimum value according to comparable
     * @return The value based at the root of the priority queue
     */
    public E dequeue() {
        E returnValue = root.value;

        root = merge(root.left, root.right);

        return returnValue;
    }

    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Leftist tree node
     * */
    public final class Node {
        /** Left Child*/
        public Node left;

        /** Right Child */
        public Node right;

        public E value;

        /** Null Path Length */
        public int npl;

        public Node(E value) {
            this(value, null, null);
        }

        public Node(E value, Node left, Node right) {
            this.left = left;
            this.right = right;
            this.value = value;
            this.npl = 0;
        }

        /**
         * Swap the left and right children of the node
         */
        private void swapkids() {
            Node temp = this.left;

            this.left = this.right;
            this.right = temp;
        }

        /**
         * Calculate the null path length of the node and update it accordingly
         */
        private void setNullPathLength() {
            if (right == null || left == null) {
                npl = 0;
            } else {
                npl = Math.min(right.npl, left.npl) + 1;
            }
        }
    }


    /**
     * Merge two Leftist Nodes together. Implementation by Prof. Dean Matthias
     * */
    private Node merge(Node t1, Node t2) {
        if (t1 == null) return t2;
        if (t2 == null) return t1;

        Node small;

        if (t1.value.compareTo(t2.value) < 0) {
            t1.right = merge(t1.right, t2);
            small = t1;
        } else {
            t2.right = merge(t2.right, t1);
            small = t2;
        }

        // Left subtree must always have a greater NPL
        int leftNpl = small.left != null ? small.left.npl : -1;
        int rightNpl = small.right != null ? small.right.npl : -1;
        if (leftNpl < rightNpl) {
            small.swapkids();
        }

        small.setNullPathLength();

        return small;
    }
}

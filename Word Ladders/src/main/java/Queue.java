public class Queue<E> {
    private Node<E> head;
    private Node<E> tail;

    int totalEnqueues = 0;
    private static class Node<E> {
        Node<E> next;
        E data;

        Node(E data) {
            this.data = data;
            this.next = null;
        }

        public void printList() {
            System.out.print("[");
            printList(this);
            System.out.print("]");
        }
        private void printList(Node<E> currentNode) {
            if (currentNode == null)
                return;
            System.out.print(currentNode.data);
            System.out.print(" ");
            printList(currentNode.next);
        }
    }

    @SafeVarargs
    public Queue(E... items) {
        for (E item : items) {
            enqueue(item);
        }
    }

    public void enqueue(E value){
        Node<E> temp = tail;
        tail = new Node<>(value);
        tail.next = null;
        if (isEmpty()) {
            head = tail;
        } else {
            temp.next = tail;
        }
        totalEnqueues++;
    }

    public E dequeue() {
        if (head == null) {
            throw new NullPointerException("No data exists");
        }
        E data = head.data;
        head = head.next;
        if (isEmpty()) {
            tail = null;
        }
        return data;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void printQueue() {
        if (head == null) {
            return;
        }
        head.printList();
    }
}

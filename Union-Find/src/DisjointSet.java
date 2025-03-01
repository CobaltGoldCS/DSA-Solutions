import java.util.Arrays;

public class DisjointSet {

    int[] setArray;
    public DisjointSet(int size) {
        setArray = new int[size];
        Arrays.fill(setArray, -1);
    }

    public void union(int node1, int node2) {
        node1 = find(node1);
        node2 = find(node2);

        if (node1 == node2) {
            return;
        }

        if (setArray[node1 - 1] < setArray[node2 - 1]) {
            setArray[node1 - 1] += setArray[node2 - 1];
            setArray[node2 - 1] = node1;
        } else {
            setArray[node2 - 1] += setArray[node1 - 1];
            setArray[node1 - 1] = node2;
        }
    }

    public int find(int node) {
        // If the node is positive, the node points to a parent node
        int prevNode = node;
        if (setArray[node - 1] > 0) {
            node = find(setArray[node - 1]);
        }
        // Connect node to the root node
        if (prevNode != node) {
            setArray[prevNode - 1] = node;
        }
        return node;
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();
        for (int i = 0; i < setArray.length; i++) {
            if (setArray[i] < -1) {
                returnString.append(i + 1).append(" size ").append(setArray[i] * -1).append(", ");
            }
        }
        return returnString.toString();
    }
}

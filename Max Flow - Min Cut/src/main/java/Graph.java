import java.util.*;

public class Graph {
    private final GraphNode[] vertices;  // Adjacency list for graph.
    private ArrayList<GraphNode.EdgeInfo> flowingEdges;
    private boolean isResidual = false;
    private final String name;  //The file from which the graph was created.

    public Graph(String name, int vertexCount) {
        this.name = name;

        vertices = new GraphNode[vertexCount];
        for (int vertex = 0; vertex < vertexCount; vertex++) {
            vertices[vertex] = new GraphNode(vertex);
        }
    }

    public boolean addEdge(int source, int destination, int capacity) {
        // A little bit of validation
        if (source < 0 || source >= vertices.length) return false;
        if (destination < 0 || destination >= vertices.length) return false;

        // This adds the actual requested edge, along with its capacity
        vertices[source].addEdge(source, destination, capacity);
        vertices[destination].addEdge(destination, source, 0);

        return true;
    }

    /**
     * Algorithm to find max-flow in a network
     */
    public int findMaxFlow(int s, int t, boolean report) {
        int totalFlow = 0;
        ArrayList<GraphNode.EdgeInfo> capacityEdges = new ArrayList<>();
        flowingEdges = new ArrayList<>();

        if (report) {
            System.out.printf("-- Max Flow: %s --\n", name);
        }

        while (hasAugmentingPath(s,t)) {
            int currentVertex = t;
            int pathFlow = Integer.MAX_VALUE;

            ArrayList<GraphNode.EdgeInfo> minPath = new ArrayList<>();
            ArrayList<GraphNode.EdgeInfo> reverseEdges = new ArrayList<>();

            while (currentVertex != s) {
                // Build minPath
                GraphNode current = vertices[currentVertex];
                GraphNode parent = vertices[current.parent];

                for (GraphNode.EdgeInfo edge : parent.successor) {
                    if (edge.to == current.id) {
                        minPath.add(edge);
                        flowingEdges.add(edge);
                        // Find path flow
                        pathFlow = Math.min(pathFlow, edge.capacity);
                        break;
                    }
                }

                for (GraphNode.EdgeInfo edge : current.successor) {
                    if (edge.to == parent.id) {
                        reverseEdges.add(edge);
                        if (report && !capacityEdges.contains(edge)) {
                            capacityEdges.add(edge);
                        }
                    }
                }

                currentVertex = parent.id;
            }

            Collections.reverse(minPath);

            if (report) {
                System.out.printf("Flow %d:", pathFlow);
                System.out.print(" " + minPath.get(0).from);
                for (GraphNode.EdgeInfo edge : minPath) {
                    System.out.print(" " + edge.to);
                }
                System.out.println();
            }

            for (int i = 0; i < minPath.size(); i++) {
                // Build residual graph
                minPath.get(i).capacity -= pathFlow;
                reverseEdges.get(i).capacity += pathFlow;
            }

            totalFlow += pathFlow;
        }

        isResidual = true;

        if (report) {
            System.out.println();
            //capacityEdges.sort(Comparator.comparingInt(e -> e.to));
            for (GraphNode.EdgeInfo edge : capacityEdges) {
                if (edge.capacity == 0) {
                    continue;
                }
                System.out.printf("Edge(%d, %d) transports %d items\n", edge.to, edge.from, edge.capacity);
            }
        }

        return totalFlow;
    }

    /**
     * Algorithm to find an augmenting path in a network
     */
    private boolean hasAugmentingPath(int s, int t) {
        Queue<Integer> queue = new LinkedList<>();

        for (GraphNode vertex : vertices) {
            vertex.visited = false;
            vertex.parent = -1;
        }

        queue.add(s);
        vertices[s].visited = true;

        while (!queue.isEmpty()) {
            GraphNode current = vertices[queue.remove()];
            for (GraphNode.EdgeInfo edge : current.successor) {
                GraphNode successor = vertices[edge.to];
                if (edge.capacity == 0 || successor.visited) {
                    continue;
                }
                successor.visited = true;
                successor.parent = current.id;

                if (successor.id == t) {
                    return true;
                }
                queue.add(successor.id);
            }
        }
        return false;
    }

    private void generateResidual(int s, int t) {
        if (isResidual) {
            return;
        }
        flowingEdges = new ArrayList<>();
        while (hasAugmentingPath(s,t)) {
            int currentVertex = t;
            int pathFlow = Integer.MAX_VALUE;

            ArrayList<GraphNode.EdgeInfo> minPath = new ArrayList<>();
            ArrayList<GraphNode.EdgeInfo> reverseEdges = new ArrayList<>();

            while (currentVertex != s) {
                // Recreate the path and get the flow
                GraphNode current = vertices[currentVertex];
                GraphNode parent = vertices[current.parent];

                for (GraphNode.EdgeInfo edge : parent.successor) {
                    if (edge.to == current.id) {
                        minPath.add(edge);
                        flowingEdges.add(edge);
                        pathFlow = Math.min(pathFlow, edge.capacity);
                        break;
                    }
                }

                for (GraphNode.EdgeInfo edge : current.successor) {
                    if (edge.to == parent.id) {
                        reverseEdges.add(edge);
                    }
                }

                currentVertex = parent.id;
            }

            Collections.reverse(minPath);

            for (int i = 0; i < minPath.size(); i++) {
                // Turn the graph into a residual graph
                minPath.get(i).capacity -= pathFlow;
                reverseEdges.get(i).capacity += pathFlow;
            }
        }
        isResidual = true;
    }

    /**
     * Algorithm to find the min-cut edges in a network
     */
    public void findMinCut(int s) {
        if (!isResidual) {
            int t = -1;
            for (GraphNode node : vertices) {
                if (node.successor.stream().allMatch(e -> e.capacity == 0)) {
                    // Looking for a node that only receives edges, and doesn't transport flow
                    t = node.id;
                    break;
                }
            }
            if (t == -1) {
                throw new RuntimeException("Cannot construct residual graph");
            }
            generateResidual(s,t);
        }

        for (GraphNode node : vertices) {
            node.visited = false;
        }
        Queue<Integer> nodes = new LinkedList<>();
        ArrayList<Integer> reachableNodes = new ArrayList<>();

        nodes.add(s);
        vertices[s].visited = true;
        reachableNodes.add(s);

        while (!nodes.isEmpty()) {
            GraphNode currentReachableNode = vertices[nodes.remove()];

            for (GraphNode.EdgeInfo edges : currentReachableNode.successor) {
                GraphNode successor = vertices[edges.to];
                if (edges.capacity == 0 || successor.visited) {
                    continue;
                }

                successor.visited = true;
                nodes.add(successor.id);
                reachableNodes.add(successor.id);

            }
        }

        //ArrayList<GraphNode.EdgeInfo> cutEdges = new ArrayList<>();

        System.out.printf("-- Min Cut: %s --\n", name);
        for (int nodeId : reachableNodes) {
            GraphNode node = vertices[nodeId];
            for (GraphNode.EdgeInfo edge : node.successor) {
                // If the edge is not a flowing edge, there is no need to cut it
                if (!reachableNodes.contains(edge.to) && flowingEdges.contains(edge)) {
                    //cutEdges.add(edge);
                    int cutEdgeFlow = 0;
                    for (GraphNode.EdgeInfo reverseEdge : vertices[edge.to].successor) {
                        if (reverseEdge.to == node.id && reverseEdge.capacity != 0) {
                            cutEdgeFlow = reverseEdge.capacity;
                            break;
                        }
                    }
                    System.out.printf("Min Cut Edge: (%d, %d): %d\n", edge.from, edge.to, cutEdgeFlow);
                }
            }
        }

        System.out.println();


    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("The Graph ").append(name).append(" \n");
        for (var vertex : vertices) {
            sb.append((vertex.toString()));
        }
        return sb.toString();
    }
}

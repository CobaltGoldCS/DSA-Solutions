Line 26 in the code creates an edge pointing from the destination to the source in the addEdge function whenever a new edge is added. This is because the graph used for network flow is *undirected*. 

We can have augmenting paths that go "backwards" in essence, and they are
still perfectly valid. My own code modifies the edges (both "forwards" and "backwards") in order to represent the residual graph. 

These backwards paths are necessary, because without them, the algorithm has no way to properly find valid paths.

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.DirectedGraph;

import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.graph.util.EdgeType;

import org.apache.commons.collections15.Factory;

//No other imports allowed
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Class that represented a directed graph.
 * @param <V> v
 */
class ThreeTenGraph<V extends Comparable<V>> implements Graph<V,Destination<V>>, DirectedGraph<V,Destination<V>> {



    /** 
   * adj heap map.
   */
    private LinkedHashMap<V,MinHeap<Destination<V>>> adjHeap = null;
    //Might need to add a check null case for all of the moethod
    /**
   * Returns a view of all vertices in this graph. In general, this
   * obeys the Collection contract, and therefore makes no guarantees 
   * about the ordering of the vertices within the set.
   * @return a Collection view of all vertices in this graph
   */
    public Collection<V> getVertices() {
        //O(n) where n is the number of nodes in the graph

        //Make a copy of this with LinkedList below don't directly return anything here
      
        //IMPORTANT: You must NOT _directly_ return a reference to anything
        //still in use by the graph class (e.g. the adjHeap key set).
        //If you pass that back, it could be edited from outside this class!
      
        //Linked list has a "copy constructor" you can use to make copies
        //of things you want to return that are still in use:
		      //https://docs.oracle.com/javase/9/docs/api/java/util/LinkedList.html#LinkedList-java.util.Collection-
		
		      //replace this!
        if (adjHeap == null) {
            return null;
        }
        //Return a linkedlist of vertex
        return new LinkedList<V>(adjHeap.keySet());
	}
	
    /**
   * Returns a view of all edges in this graph. In general, this
   * obeys the Collection contract, and therefore makes no guarantees 
   * about the ordering of the vertices within the set.
   * @return a Collection view of all edges in this graph
   */
    public Collection<Destination<V>> getEdges() {
        //O(n+e) where e is the number of edges in the graph and n is the number
        //of nodes in the graph
        
        //Big-O hint: O(n+e) with the above constraints just means you
        //should be using iterators for your hash-map and your heaps
        
        //Hint: the return type for this is a Collection. Java's LinkedList
        //_is a_ type of collection (and that is imported for you).
        
        //replace this!
        //ArrayList<V> temp = new ArrayList<V>(adjHeap.keySet());
        //for(Map.Entry<V, MinHeap<Destination<V>>> entry :adjHeap.entrySet()){
        // V v = entry.getKey();
        if (adjHeap == null) {
            return null;
        }  
        //loop through the whole adjHeap and add all the edge in a linked list
        LinkedList<Destination<V>> temp = new LinkedList<>();
        for(V key : adjHeap.keySet()){
            MinHeap<Destination<V>> value = adjHeap.get(key);
            // while(value.iterator().hasNext()){
            //   temp.add(value.iterator().next());
            //   //value.iterator().next();
            // }
            for(Destination<V> edge: value){
                temp.add(edge);
            }
        }
		return temp;
	}
    
    /**
   * Returns the number of vertices in this graph.
   * @return the number of vertices in this graph
   */
    public int getVertexCount() {
        //O(1)
        
        //note: this runtime is not a mistake, think about how
        //you could find out the number of nodes *without*
        //looking at each one
        
        //replace this!
        if (adjHeap == null) {
            return 0;
        }
        //Get the size of the set since set is unique
        LinkedList<V> temp = new LinkedList<>(adjHeap.keySet());
  //This one is pretty weird
		return temp.size();
	}
    
    /**
   * Returns the number of edges in this graph.
   * @return the number of edges in this graph
   */
    public int getEdgeCount() {
        //O(n) where n is the number of nodes in the graph
        //note: this is NOT O(n+e), just O(n)
        
        //note: this runtime is not a mistake, think about how
        //you could find out the number of edges *without*
        //looking at each one
        
        //replace this!
        // for(V key : adjHeap.keySet()){
        //   MinHeap<Destination<V>> value = adjHeap.get(key);
        //   while(value.iterator().hasNext()){
        //     temp.add(value.iterator().next());
        //     //value.iterator().next();
        //   }
        // }
        if (adjHeap == null) {
            return 0;
        }
        LinkedList<V> temp = new LinkedList<>(adjHeap.keySet());
        int sum = 0;
        //loop through all the vertex and get their heap size
        for(V key : adjHeap.keySet()){
            MinHeap<Destination<V>> value = adjHeap.get(key);//Might need to worry about null
            sum += value.size();
        }
		return sum;
	}

    /**
   * Returns true if this graph's vertex collection contains vertex.
   * Equivalent to getVertices().contains(vertex).
   * @param vertex the vertex whose presence is being queried
   * @return true iff this graph contains a vertex vertex
   */
    public boolean containsVertex(V vertex) {
        //O(1), NOT O(n)!
        
        //note: this runtime is not a mistake, there is a "quick" way
        //and a slow way to find out if a node is in the graph, look at
        //the storage overview in the project description for ideas
        
        //replace this!
        if (adjHeap == null) {
            return false;
        }
        //Contain or not?
        return adjHeap.containsKey(vertex);
	}
    
    /**
   * Returns a Collection view of the successors of vertex 
   * in this graph.  A successor of vertex is defined as a vertex v 
   * which is connected to 
   * vertex by an edge e, where e is an incoming edge of 
   * v and an outgoing edge of vertex.
   * @param vertex    the vertex whose predecessors are to be returned
   * @return  a Collection view of the successors of vex in ertthis graph
   */
    public Collection<V> getSuccessors(V vertex) {
        //O(n lg n) where n is the number of nodes in the graph
        //this MUST return successors in SORTED ORDER (smallest to largest)
        //this may NOT use a non-heap sorting algorithm, you must use heaps!
        //Good news is, you already have those... and can copy them...
        //LinkedList<Destination<V>> temp = new LinkedList<>();
        if (adjHeap == null) {
            return null;
        }
        //loop through vertex and add every heap of it in a llist
        LinkedList<V> temp = new LinkedList<>();
        for(Destination<V> edge:adjHeap.get(vertex)){
            temp.add(edge.node);//Might need to check Null but idk?
        }
		//replace this!
		return temp;
	}
	
    /**
   * Returns a Collection view of the predecessors of vertex 
   * in this graph.  A predecessor of vertex is defined as a vertex v 
   * which is connected to 
   * vertex by an edge e, where e is an outgoing edge of 
   * v and an incoming edge of vertex.
   * @param vertex    the vertex whose predecessors are to be returned
   * @return  a Collection view of the predecessors of vertex in this graph
   */
    public Collection<V> getPredecessors(V vertex) {
        //O(n+e) where e is the number of edges in the graph and n is the number of nodes
        if (adjHeap == null) {
            return null;
        }
        //loop through the whole adjheap and if the edge node value is vertext then add it in
        LinkedList<V> temp = new LinkedList<>();
        for(V key : adjHeap.keySet()){
            MinHeap<Destination<V>> value = adjHeap.get(key);
            for(Destination<V> edge: value){
                if(edge.node.equals(vertex)){
                    temp.add(key);
                }
            }
        }
		//replace this!
		return temp;
	}

    /**
   * Returns an edge that connects v1 to v2.
   * If this edge is not uniquely
   * defined (that is, if the graph contains more than one edge connecting 
   * v1 to v2), any of these edges 
   * may be returned.  findEdgeSet(v1, v2) may be 
   * used to return all such edges.
   * Returns null if either of the following is true:
   * <ul>
   * <li/>v1 is not connected to v2
   * <li/>either v1 or v2 are not present in this graph
   * </ul> 
   * for purposes of this method, v1 is only considered to be connected to
   * v2 via a given <i>directed</i> edge e if
   * v1 == e.getSource() && v2 == e.getDest() evaluates to true.
   * (v1 and v2 are connected by an undirected edge u if 
   * u is incident to both v1 and v2.)
   * @param v1 vertex1
   * @param v2 vertex2
   * @return  an edge that connects v1 to v2, or null if no such edge exists (or either vertex is not present)
   * @see Hypergraph#findEdgeSet(Object, Object) 
   */
    public Destination<V> findEdge(V v1, V v2) {
        //O(n) where n is the number of nodes in the graph
        //Need fixing!!!!!!!!!!!!!
        if (adjHeap == null) {
            return null;
        }
        if(!adjHeap.containsKey(v1) || !adjHeap.containsKey(v2)){
            return null;
        }
        MinHeap<Destination<V>> value = adjHeap.get(v1);
        if(value == null){
            return null;
        }
        //Loop through vertex1 to find if it point to v2 or not
        for(Destination<V> node: value){
            if(node.node.equals(v2)){
                //directly return??
                return node;
                //return new Destination<V>(node.node, node.priority);
            }
        }
        return null;
	}
	
    /**
   * Returns the endpoints of edge as a Pair.
   * @param edge the edge whose endpoints are to be returned
   * @return the endpoints (incident vertices) of edge or null if the edge does not exist
   */
    public Pair<V> getEndpoints(Destination<V> edge) {
        //O(n+e) where e is the number of edges in the graph and n is the number of nodes
        //first part of the pair should be the source node, the second the destination node.
        if (edge == null || adjHeap == null) {
            return null;
        }
        for(V key: adjHeap.keySet()){
            MinHeap<Destination<V>> value = adjHeap.get(key);
            for(Destination<V> found: value){
                if(found.equals(edge)){
                    return new Pair<V>(key, found.node);
                }
            }
        }
        //You can create a pair with "new Pair<V>(source, dest)" when you find it.
        return null;
    }
    
    /**
   * Adds vertex to this graph.
   * Fails if vertex is null or already in the graph.
   * 
   * @param vertex    the vertex to add
   * @return true if the add is successful, and false otherwise
   * @throws IllegalArgumentException if vertex is null
   */
    public boolean addVertex(V vertex) {
        //O(1) -- NOT O(n)!
        
        //NOTE: a node in the graph should have a non-null linked
        //list in the adjacency matrix, a node not in the graph
        //should have a null reference at that same spot
        
        //replace this!
        //adjHeap.put(vertex, new MinHeap<Destination<V>()>());
        if(vertex == null || adjHeap.containsKey(vertex)){
            return false;
        }
        //Add the vertex into adjheap
        adjHeap.put(vertex, new MinHeap<Destination<V>>());
        return true;
	}

    /**
   * Adds edge e to this graph such that it connects 
   * vertex v1 to v2.
   * Equivalent to addEdge(e, new Pair(v1, v2)).
   * If this graph does not contain v1, v2, 
   * or both, implementations may choose to either silently add 
   * the vertices to the graph or throw an IllegalArgumentException.
   * If this graph assigns edge types to its edges, the edge type of
   * e will be the default for this graph.
   * See Hypergraph.addEdge() for a listing of possible reasons
   * for failure.
   * @param e the edge to be added
   * @param v1 the first vertex to be connected
   * @param v2 the second vertex to be connected
   * @return true if the add is successful, false otherwise
   * @see Hypergraph#addEdge(Object, Collection)
   * @see #addEdge(Object, Object, Object, EdgeType)
   */
    public boolean addEdge(Destination<V> e, V v1, V v2) {
        //O(n+e) where e is the number of edges in the graph and n is the number
        //of nodes in the graph
        
        //note: you need to make sure there isn't a different edge connecting v1 and v2,
        //that the destination object isn't being used already in the graph, and
        //you need to assign the node in destination to the correct endpoint
        if (adjHeap == null) {
            return false;
        }
        if(!adjHeap.containsKey(v1) || !adjHeap.containsKey(v2)){
            throw new IllegalArgumentException();
        }    
        if(findEdge(v1, v2) != null){
            return false;
        }
        //Add the new node with value v2 into vertex1 heap
        MinHeap<Destination<V>> value = adjHeap.get(v1);
        e.node = v2;
        value.offer(e);
        // for(V key: adjHeap.keySet()){
        //   MinHeap<Destination<V>> value = adjHeap.get(key);
        //   if(key.equals(v1)){
        //     e.node = v2;
        //     value.offer(e);
        //   }
        // }
        //replace this!

        return true;
	}
    
    /**
   * Removes vertex from this graph.
   * As a side effect, removes any edges e incident to vertex if the 
   * removal of vertex would cause e to be incident to an illegal
   * number of vertices.  (Thus, for example, incident hyperedges are not removed, but 
   * incident edges--which must be connected to a vertex at both endpoints--are removed.) 
   * 
   * <p>Fails under the following circumstances:
   * <ul>
   * <li/>vertex is not an element of this graph
   * <li/>vertex is null
   * </ul>
   * 
   * @param vertex the vertex to remove
   * @return true if the removal is successful, false otherwise
   */
    public boolean removeVertex(V vertex) {
        //O(n+e) where e is the number of edges in the graph and n is the number of nodes
        
        //remember to remove edges that point _to_ this node!
        if(adjHeap == null|| vertex == null || !adjHeap.containsKey(vertex)){
            return false;
        }
        //remove vertex and remove edge that have vertex as node
        adjHeap.remove(vertex);
        for(V key: adjHeap.keySet()){
            MinHeap<Destination<V>> value = adjHeap.get(key);
            for(Destination<V> edge: value){
                if(edge.node.equals(vertex)){
                    value.remove(edge);
                }
            }
        }
        return true;
	}

    /**
   * Removes edge from this graph.
   * Fails if edge is null, or is otherwise not an element of this graph.
   * 
   * @param edge the edge to remove
   * @return true if the removal is successful, false otherwise
   */
    public boolean removeEdge(Destination<V> edge) {
        //O(n+e) where e is the number of edges in the graph and n is the number
        //of nodes in the graph
        
        //don't over think this, find the heap containing the Destination<V> object
        //after this method is over that object shouldn't be in the heap associated with the source node
        //you also may want to look at the optional methods you could implement as part of the Queue interface...
        if(edge == null || adjHeap == null){
            return false;
        }
        //loop through and check if the edge provided is in or not? if it is then rm it
        for(V key: adjHeap.keySet()){
            MinHeap<Destination<V>> value = adjHeap.get(key);
            for(Destination<V> find: value){
                if(find.equals(edge)){
                    value.remove(edge);
                    return true;
                }
            }
        }
        //replace this!
        return false;
	}
	
	//********************************************************************************
	//   testing code goes here... edit this as much as you want!
	//********************************************************************************
	
	/**
	 *  {@inheritDoc}
	 */
	public String toString() {
		return super.toString();
	}
	/**
   * main.
   * @param args main
   */
	public static void main(String[] args) {
        //create a set of nodes and edges to test with
        String[] nodes = {"X", "G", "Hat", "A!"};
        
        //constructs a graph
        ThreeTenGraph<String> graph = new ThreeTenGraph<>();
        for(String n : nodes) {
            graph.addVertex(n);
        }
        
        Destination<String> e1 = new Destination<String>("G",1);
        Destination<String> e2 = new Destination<String>("A!",7);
        Destination<String> e3 = new Destination<String>("X",7);
        
        graph.addEdge(e1, "X", "G");
        graph.addEdge(e2, "X", "A!");
        graph.addEdge(e3, "Hat", "X");
        
        if(graph.getVertexCount() == 4 && graph.getEdgeCount() == 3) {
            System.out.println("Yay 1");
        }
        
        if(graph.containsVertex("X") && graph.containsEdge(e2)) {
            System.out.println("Yay 2");
        }
        
        if(graph.getSuccessors("X").contains("G") && graph.getSuccessors("X").contains("A!")) {
            System.out.println("Yay 3");
        }
      

        //lot more testing here...
        
        //If your graph "looks funny" you probably want to check:
        //getVertexCount(), getVertices(), getInEdges(vertex),
        //getOutEdges(), and getIncidentVertices(incomingEdge) first.
        //These are used by the layout class.
	}
	
	//********************************************************************************
	//   YOU MAY, BUT DON'T NEED TO EDIT THINGS IN THIS SECTION
	//********************************************************************************

 /**
   * construct.
   */
	@SuppressWarnings("unchecked")
	public ThreeTenGraph() {
		adjHeap = new LinkedHashMap<>();
	}
	
    /**
     * Returns true if vertex and edge 
     * are incident to each other.
     * Equivalent to getIncidentEdges(vertex).contains(edge) and to
     * getIncidentVertices(edge).contains(vertex).
     * @param vertex vertex
     * @param edge edge
     * @return true if vertex and edge are incident to each other
     */
    public boolean isIncident(V vertex, Destination<V> edge) {
		return getIncidentEdges(vertex).contains(edge);
	}
    
    /**
     * Returns true if v1 is a predecessor of v2 in this graph.
     * Equivalent to v1.getPredecessors().contains(v2).
     * @param v1 the first vertex to be queried
     * @param v2 the second vertex to be queried
     * @return true if v1 is a predecessor of v2, and false otherwise.
     */
    public boolean isPredecessor(V v1, V v2) {
		return getPredecessors(v1).contains(v2);
	}
    
    /**
     * Returns true if v1 is a successor of v2 in this graph.
     * Equivalent to v1.getSuccessors().contains(v2).
     * @param v1 the first vertex to be queried
     * @param v2 the second vertex to be queried
     * @return true if v1 is a successor of v2, and false otherwise.
     */
    public boolean isSuccessor(V v1, V v2) {
		return getSuccessors(v1).contains(v2);
	}
	
    /**
     * Returns a Collection view of the incoming edges incident to vertex
     * in this graph.
     * @param vertex    the vertex whose incoming edges are to be returned
     * @return  a Collection view of the incoming edges incident to vertex in this graph
     */
    public Collection<Destination<V>> getInEdges(V vertex) {
		LinkedList<Destination<V>> ret = new LinkedList<>();
		for(V pred : getPredecessors(vertex)) {
			ret.add(findEdge(pred, vertex));
		}
		return ret;
	}
    
    /**
     * Returns a Collection view of the outgoing edges incident to vertex
     * in this graph.
     * @param vertex    the vertex whose outgoing edges are to be returned
     * @return  a Collection view of the outgoing edges incident to vertex in this graph
     */
    public Collection<Destination<V>> getOutEdges(V vertex) {
		LinkedList<Destination<V>> ret = new LinkedList<>();
		for(V pred : getSuccessors(vertex)) {
			ret.add(findEdge(vertex, pred));
		}
		return ret;
	}

    /**
     * Returns true if v1 and v2 share an incident edge.
     * Equivalent to getNeighbors(v1).contains(v2).
     * 
     * @param v1 the first vertex to test
     * @param v2 the second vertex to test
     * @return true if v1 and v2 share an incident edge
     */
    public boolean isNeighbor(V v1, V v2) {
		return (findEdge(v1, v2) != null);
	}
    
    /**
     * Returns true if this graph's edge collection contains edge.
     * Equivalent to getEdges().contains(edge).
     * @param edge the edge whose presence is being queried
     * @return true iff this graph contains an edge edge
     */
    public boolean containsEdge(Destination<V> edge) {
		return (getEndpoints(edge) != null);
	}
    
    /**
     * Returns the number of incoming edges incident to vertex.
     * Equivalent to getInEdges(vertex).size().
     * @param vertex    the vertex whose indegree is to be calculated
     * @return  the number of incoming edges incident to vertex
     */
    public int inDegree(V vertex) {
		return getInEdges(vertex).size();
	}
    
    /**
     * Returns the number of outgoing edges incident to vertex.
     * Equivalent to getOutEdges(vertex).size().
     * @param vertex    the vertex whose outdegree is to be calculated
     * @return  the number of outgoing edges incident to vertex
     */
    public int outDegree(V vertex) {
		return getOutEdges(vertex).size();
	}
    
    /**
     * Returns the collection of edges in this graph which are of type edge_type.
     * @param edgeType the type of edges to be returned
     * @return the collection of edges which are of type edge_type, or null if the graph does not accept edges of this type
     * @see EdgeType
     */
    public Collection<Destination<V>> getEdges(EdgeType edgeType) {
		if(edgeType == EdgeType.DIRECTED) {
			return getEdges();
		}
		return null;
	}
    
    /**
     * If directedEdge is a directed edge in this graph, returns the source; 
     * otherwise returns null. 
     * The source of a directed edge d is defined to be the vertex for which  
     * d is an outgoing edge.
     * directedEdge is guaranteed to be a directed edge if 
     * its EdgeType is DIRECTED. 
     * @param directedEdge edge
     * @return  the source of directedEdge if it is a directed edge in this graph, or null otherwise
     */
    public V getSource(Destination<V> directedEdge) {
		return getEndpoints(directedEdge).getFirst();
	}

    /**
     * If directedEdge is a directed edge in this graph, returns the destination; 
     * otherwise returns null. 
     * The destination of a directed edge d is defined to be the vertex 
     * incident to d for which  
     * d is an incoming edge.
     * directedEdge is guaranteed to be a directed edge if 
     * its EdgeType is DIRECTED. 
     * @param directedEdge edge
     * @return  the destination of directedEdge if it is a directed edge in this graph, or null otherwise
     */
    public V getDest(Destination<V> directedEdge) {
		return getEndpoints(directedEdge).getSecond();
	}

    /**
     * Returns the number of edges of type edge_type in this graph.
     * @param edgeType the type of edge for which the count is to be returned
     * @return the number of edges of type edge_type in this graph
     */
    public int getEdgeCount(EdgeType edgeType) {
		if(edgeType == EdgeType.DIRECTED) {
			return getEdgeCount();
		}
		return 0;
	}
    
    /**
     * Returns the number of edges incident to vertex.  
     * Special cases of interest:
     * <ul>
     * <li/> Incident self-loops are counted once.
     * <li> If there is only one edge that connects this vertex to
     * each of its neighbors (and vice versa), then the value returned 
     * will also be equal to the number of neighbors that this vertex has
     * (that is, the output of getNeighborCount).
     * <li> If the graph is directed, then the value returned will be 
     * the sum of this vertex's indegree (the number of edges whose 
     * destination is this vertex) and its outdegree (the number
     * of edges whose source is this vertex), minus the number of
     * incident self-loops (to avoid double-counting).
     * </ul>
     * Equivalent to getIncidentEdges(vertex).size().
     * 
     * @param vertex the vertex whose degree is to be returned
     * @return the degree of this node
     * @see Hypergraph#getNeighborCount(Object)
     */
    public int degree(V vertex) {
		return inDegree(vertex)+outDegree(vertex);
	}
    
    /**
     * Returns the number of vertices that are adjacent to vertex
     * (that is, the number of vertices that are incident to edges in vertex's
     * incident edge set).
     * 
     * <p>Equivalent to getNeighbors(vertex).size().
     * @param vertex the vertex whose neighbor count is to be returned
     * @return the number of neighboring vertices
     */
    public int getNeighborCount(V vertex) {
		return getNeighbors(vertex).size();
	}
    
    /**
     * Returns the collection of edges in this graph which are connected to vertex.
     * 
     * @param vertex the vertex whose incident edges are to be returned
     * @return  the collection of edges which are connected to vertex, or null if vertex is not present
     */
    public Collection<Destination<V>> getIncidentEdges(V vertex) {
		//O(n) where n is the number of nodes in the graph
		
		LinkedList<Destination<V>> ret = new LinkedList<>();
		
		ret.addAll(getInEdges(vertex));
		ret.addAll(getOutEdges(vertex));
		
		return ret;
	}
    
    /**
     * Returns the collection of vertices which are connected to vertex
     * via any edges in this graph.
     * If vertex is connected to itself with a self-loop, then 
     * it will be included in the collection returned.
     * 
     * @param vertex the vertex whose neighbors are to be returned
     * @return  the collection of vertices which are connected to vertex, or null if vertex is not present
     */
    public Collection<V> getNeighbors(V vertex) {
		LinkedList<V> ret = new LinkedList<>();
		
		Collection<V> fetch = getPredecessors(vertex);
		if(fetch != null) ret.addAll(fetch);
		fetch = getSuccessors(vertex);
		if(fetch != null) ret.addAll(fetch);
		
		return ret;
	}
    
    /**
     * Returns the collection of vertices in this graph which are connected to edge.
     * Note that for some graph types there are guarantees about the size of this collection
     * (i.e., some graphs contain edges that have exactly two endpoints, which may or may 
     * not be distinct).  Implementations for those graph types may provide alternate methods 
     * that provide more convenient access to the vertices.
     * 
     * @param edge the edge whose incident vertices are to be returned
     * @return  the collection of vertices which are connected to edge, or null if edge is not present
     */
    public Collection<V> getIncidentVertices(Destination<V> edge) {
		
		Pair<V> p = getEndpoints(edge);
		if(p == null) return null;
		
		LinkedList<V> ret = new LinkedList<>();
		ret.add(p.getFirst());
		ret.add(p.getSecond());
		return ret;
	}

    /**
     * Returns the number of predecessors that vertex has in this graph.
     * Equivalent to vertex.getPredecessors().size().
     * @param vertex the vertex whose predecessor count is to be returned
     * @return  the number of predecessors that vertex has in this graph
     */
    public int getPredecessorCount(V vertex) {
		return inDegree(vertex);
	}
    
    /**
     * Returns the number of successors that vertex has in this graph.
     * Equivalent to vertex.getSuccessors().size().
     * @param vertex the vertex whose successor count is to be returned
     * @return  the number of successors that vertex has in this graph
     */
    public int getSuccessorCount(V vertex) {
		return outDegree(vertex);
	}
    
    /**
     * Returns the vertex at the other end of edge from vertex.
     * (That is, returns the vertex incident to edge which is not vertex.)
     * @param vertex the vertex to be queried
     * @param edge the edge to be queried
     * @return the vertex at the other end of edge from vertex
     */
    public V getOpposite(V vertex, Destination<V> edge) {
		Pair<V> p = getEndpoints(edge);
		if(p.getFirst().equals(vertex)) {
			return p.getSecond();
		}
		else {
			return p.getFirst();
		}
	}
    
    /**
     * Returns all edges that connects v1 to v2.
     * If this edge is not uniquely
     * defined (that is, if the graph contains more than one edge connecting 
     * v1 to v2), any of these edges 
     * may be returned.  findEdgeSet(v1, v2) may be 
     * used to return all such edges.
     * Returns null if v1 is not connected to v2.
     * <br/>Returns an empty collection if either v1 or v2 are not present in this graph.
     *  
     * <p><b>Note</b>: for purposes of this method, v1 is only considered to be connected to
     * v2 via a given <i>directed</i> edge d if
     * v1 == d.getSource() && v2 == d.getDest() evaluates to true.
     * (v1 and v2 are connected by an undirected edge u if 
     * u is incident to both v1 and v2.)
     * @param v1 vertext 1
     * @param v2 vertext 2
     * @return  a collection containing all edges that connect v1 to v2, or null if either vertex is not present
     * @see Hypergraph#findEdge(Object, Object) 
     */
    public Collection<Destination<V>> findEdgeSet(V v1, V v2) {
		Destination<V> edge = findEdge(v1, v2);
		if(edge == null) {
			return null;
		}
		
		LinkedList<Destination<V>> ret = new LinkedList<>();
		ret.add(edge);
		return ret;
		
	}
	
    /**
     * Returns true if vertex is the source of edge.
     * Equivalent to getSource(edge).equals(vertex).
     * @param vertex the vertex to be queried
     * @param edge the edge to be queried
     * @return true iff vertex is the source of edge
     */
    public boolean isSource(V vertex, Destination<V> edge) {
		return getSource(edge).equals(vertex);
	}
    
    /**
     * Returns true if vertex is the destination of edge.
     * Equivalent to getDest(edge).equals(vertex).
     * @param vertex the vertex to be queried
     * @param edge the edge to be queried
     * @return true iff vertex is the destination of edge
     */
    public boolean isDest(V vertex, Destination<V> edge) {
		return getDest(edge).equals(vertex);
	}
    
    /**
     * Adds edge e to this graph such that it connects 
     * vertex v1 to v2.
     * Equivalent to addEdge(e, new Pair(v1, v2)).
     * If this graph does not contain v1, v2, 
     * or both, implementations may choose to either silently add 
     * the vertices to the graph or throw an IllegalArgumentException.
     * If edgeType is not legal for this graph, this method will
     * throw IllegalArgumentException.
     * See Hypergraph.addEdge() for a listing of possible reasons
     * for failure.
     * @param e the edge to be added
     * @param v1 the first vertex to be connected
     * @param v2 the second vertex to be connected
     * @param edgeType the type to be assigned to the edge
     * @return true if the add is successful, false otherwise
     * @see Hypergraph#addEdge(Object, Collection)
     * @see #addEdge(Object, Object, Object)
     */
    public boolean addEdge(Destination<V> e, V v1, V v2, EdgeType edgeType) {
		//NOTE: Only directed edges allowed
		
		if(edgeType == EdgeType.UNDIRECTED) {
			throw new IllegalArgumentException();
		}
		
		return addEdge(e, v1, v2);
	}
    
 /**
     * Adds edge to this graph.
     * Fails under the following circumstances:
     * <ul>
     * <li/>edge is already an element of the graph 
     * <li/>either edge or vertices is null
     * <li/>vertices has the wrong number of vertices for the graph type
     * <li/>vertices are already connected by another edge in this graph,
     * and this graph does not accept parallel edges
     * </ul>
     * 
     * @param edge edge
     * @param vertices vettices
     * @return true if the add is successful, and false otherwise
     * @throws IllegalArgumentException if edge or vertices is null, or if a different vertex set in this graph is already connected by edge, or if vertices are not a legal vertex set for edge 
     */
	@SuppressWarnings("unchecked")
    public boolean addEdge(Destination<V> edge, Collection<? extends V> vertices) {
		if(edge == null || vertices == null || vertices.size() != 2) {
			return false;
		}
		
		V[] vs = (V[])vertices.toArray();
		return addEdge(edge, vs[0], vs[1]);
	}

 /**
     * Adds edge to this graph with type edge_type.
     * Fails under the following circumstances:
     * <ul>
     * <li/>edge is already an element of the graph 
     * <li/>either edge or vertices is null
     * <li/>vertices has the wrong number of vertices for the graph type
     * <li/>vertices are already connected by another edge in this graph,
     * and this graph does not accept parallel edges
     * <li/>edge_type is not legal for this graph
     * </ul>
     * 
     * @param edge edge
     * @param vertices ver
     * @param edgeType type
     * @return true if the add is successful, and false otherwise
     * @throws IllegalArgumentException if edge or vertices is null, or if a different vertex set in this graph is already connected by edge, or if vertices are not a legal vertex set for edge 
     */
	@SuppressWarnings("unchecked")
    public boolean addEdge(Destination<V> edge, Collection<? extends V> vertices, EdgeType edgeType) {
		if(edge == null || vertices == null || vertices.size() != 2) {
			return false;
		}
		
		V[] vs = (V[])vertices.toArray();
		return addEdge(edge, vs[0], vs[1], edgeType);
	}
	
	//********************************************************************************
	//   DO NOT EDIT ANYTHING BELOW THIS LINE
	//********************************************************************************
	
	/**
     * Returns a {@code Factory} that creates an instance of this graph type.
     * @param <V> the vertex type for the graph factory
     * @param <E> e
     * @return new graph
     */
	public static <V,E> Factory<DirectedGraph<V,E>> getFactory() { 
		return new Factory<DirectedGraph<V,E>> () {
			@SuppressWarnings("unchecked")
			public DirectedGraph<V,E> create() {
				return (DirectedGraph<V,E>) new ThreeTenGraph();
			}
		};
	}
    
    /**
     * Returns the edge type of edge in this graph.
     * @param edge edge
     * @return the EdgeType of edge, or null if edge has no defined type
     */
    public EdgeType getEdgeType(Destination<V> edge) {
		return EdgeType.DIRECTED;
	}
    
    /**
     * Returns the default edge type for this graph.
     * 
     * @return the default edge type for this graph
     */
    public EdgeType getDefaultEdgeType() {
		return EdgeType.DIRECTED;
	}
    
    /**
     * Returns the number of vertices that are incident to edge.
     * For hyperedges, this can be any nonnegative integer; for edges this
     * must be 2 (or 1 if self-loops are permitted). 
     * 
     * <p>Equivalent to getIncidentVertices(edge).size().
     * @param edge the edge whose incident vertex count is to be returned
     * @return the number of vertices that are incident to edge.
     */
    public int getIncidentCount(Destination<V> edge) {
		return 2;
	}
}

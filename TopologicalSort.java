import java.io.*;
import java.util.Scanner;

import java.util.Collection;
import java.util.LinkedList;
import java.util.HashSet;
/** 
 * javadoc.
*/
class TopologicalSort {
 /**
  * Get graph from file provided.
  * @param filename file
  * @return the graph
  * @throws IOException ex
  */
	public static ThreeTenGraph<String> getGraph(String filename) throws IOException {
		ThreeTenGraph<String> graph = new ThreeTenGraph<>();
		
		try(Scanner r = new Scanner(new File(filename))) {
			int numNodes = Integer.parseInt(r.nextLine());
			for(int i = 0; i < numNodes; i++) {
				String nodeName = r.nextLine().trim();
				graph.addVertex(nodeName);
			}
			
			int numEdges = Integer.parseInt(r.nextLine());
			for(int i = 0; i < numEdges; i++) {
				String[] fromToPriority = r.nextLine().trim().split(",");
				graph.addEdge(
					new Destination<String>(fromToPriority[1], Integer.parseInt(fromToPriority[2])),
					fromToPriority[0],
					fromToPriority[1]
				);
			}
		}
		
		return graph;
	}
	
	
	//uses DFS to create a topological sort
	//accepts a graph
	//throws IllegalArgumentException for invalid inputs for a topological sort
	/**
	 * sorting.
	 * @param <T> t
	 * @param graph graph to sort
	 * @param startNode node to start
	 * @return sorted llist
	 */
	public static <T extends Comparable<T>> LinkedList<T> topologicalSort(ThreeTenGraph<T> graph, T startNode) {
		//error checking on inputs, requires graph and a valid starting node
		
		if(graph == null || startNode == null) {
			throw new IllegalArgumentException("Graph does not contain starting node");
		}
		
		//YOUR CODE HERE: Add a check to make sure the start node is in the graph.
		//If not, throw the same exception as above.
		if(!graph.containsVertex(startNode)){
			throw new IllegalArgumentException("Graph does not contain starting node");
		}
		//YOUR CODE HERE: Create two empty HashSets, one called "started" and one called "finished"
		//these will help us track what we've already seen
		HashSet<T> started = new HashSet<>();
		HashSet<T> finished = new HashSet<>();
		//YOUR CODE HERE: Create an empty linked list called "sortedOrder" use as our stack
		LinkedList<T> sortedOrder = new LinkedList<>(); //this is our stack in order to pop when topological sort
		//initialize DFS
		boolean dfsDone = false;
		
		while(!dfsDone) {
			//visit the start node
			if(!visit(graph, startNode, started, finished, sortedOrder)) {
				//graph has no topological sorting
				throw new IllegalArgumentException("Graph contains a cycle.");
			}
			
			//make sure all the nodes have been visited
			dfsDone = true;
			for(T v : graph.getVertices()) {
				if(!finished.contains(v)) {
					startNode = v;
					dfsDone = false;
					break;
				}
			}
		}
		
		//return the topological sorting
		return sortedOrder;
	}
	//Sorting but reverse
	// /**
	//  * sorting.
	//  * @param <T> t
	//  * @param llist list to ret
	//  * @param size size of llsit
	//  */
	// private static <T extends Comparable<T>> void bubbleSortButBigFirst(LinkedList<T> llist, int size){
	// 	int swap = 0;
	// 	for(int i = 0; i < size - 1; i++){
	// 		swap = 0;
	// 		for(int j = 0; j < (size - i - 1); j++){
	// 			//if element at j is smaller than element at j+1
	// 			if(llist.get(j).compareTo(llist.get(j+1)) > 0){
	// 				//swap
	// 				T store;
	// 				store = llist.get(j);
	// 				//llist.(j) = llist.get(j+1);
	// 				//llist[j+1] = store;
	// 				llist.set(j, llist.get(j+1));
	// 				llist.set(j+1, store);
	// 				swap = 1;
	// 			}
	// 		}
	// 		if(swap == 0){
	// 			break;
	// 		}
	// 	}
	// }

	//returns false if there was a cycle
	/**
	 * VISIT TO CHECK IF IT CYCLE.
	 * @param <T> T
	 * @param graph GRAPH
	 * @param currentNode current node
	 * @param started node that has been started process
	 * @param finished node done with process
	 * @param sortedOrder stack
	 * @return true or false
	 */
	private static <T extends Comparable<T>> boolean visit(ThreeTenGraph<T> graph, T currentNode, HashSet<T> started, HashSet<T> finished, LinkedList<T> sortedOrder) {
		//YOUR CODE HERE: This node is "started" so add it to the started set
		started.add(currentNode);
		//YOUR CODE HERE: get the neighbors of the current node in sorted order,
		//reverse them, and store them in a linked list called "revNeighbors"
		//get all of neightbor of vertex of current Node
		Collection<T> temp = graph.getSuccessors(currentNode);//current node is not vertex??
		LinkedList<T> revNeighbors = new LinkedList<>();
		// for(T value: temp){
		// 	revNeighbors.add(value);
		// }
		//bubbleSortButBigFirst(revNeighbors, revNeighbors.size());
		//int length = revNeighbors.size()-1;
		//get all of element and add them into rev in reverse order (1st=last)
		for(T ele: temp){
			revNeighbors.addFirst(ele);
		}
		//sorting and reversing
		//revNeighbors.addFirst(currentNode);


		// Collections.sort(revNeighbors);
		//Need to do heapSort probably
		//Collection.sort(revNeighbors);


		//visit each neighbor who hasn't been visited before
		for(T neighbor : revNeighbors) {
			//if we find a node that has been started but not finished,
			//then we have a cycle and cannot do a topological sort
			if(started.contains(neighbor) && !finished.contains(neighbor)) {
				return false;
			}
			
			//if the neighbor hasn't been started, we should start them!
			if(!started.contains(neighbor)) {
				//YOUR CODE HERE: visit the neighbor and return false if there was a cycle detected
				//on that visit
				if(!visit(graph, neighbor, started, finished, sortedOrder)) {
					//graph has no topological sorting
					//throw new IllegalArgumentException("Graph contains a cycle.");
					return false;
				}
			}
			
		}
		
		//YOUR CODE HERE: This node is "finished" so add it to the finished set
		finished.add(currentNode);
		//YOUR CODE HERE: Push this node onto our state (the front of the LinkedList
		//called "sortedOrder"
		sortedOrder.addFirst(currentNode);//?? looks weird
		//sortedOrder.add(currentNode);
		return true; //leave this, it means no loop was detected
	}
}
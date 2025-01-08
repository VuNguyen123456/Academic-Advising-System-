//a (node,priority) to store in the adjacency-heap
//note: source is indicated by the key in the adj-heap
/**
 * Destination(node).
 * @param <V> v
 */
class Destination<V> implements Comparable<Destination<V>> {
	//these are directly accessable because they are package-visible
	//don't remove/rename these!
	/**
	 * node.
	 */
	V node;
	/**
	 * prio/weight.
	 */
	Integer priority;
	/**
	 * Constructor.
	 * @param n n
	 * @param e e
	 */
	Destination(V n, Integer e) { this.node = n; this.priority = e; }
	
	
	/** 
	 * compareTo but it use the pro instead of val.
	 * @param other des
	 * @return int
	 */
	@Override
	public int compareTo(Destination<V> other) {
		return this.priority.compareTo(other.priority);
	}
	
	//You may add _additional_ constructors to this class and private helpers
	//if you want, but you may not remove/change anything that is already here
	//You may override additional methods defined in Object if that's useful to you (such as toString())
	//do NOT override .equals() -- the default implementation is important for this project
}
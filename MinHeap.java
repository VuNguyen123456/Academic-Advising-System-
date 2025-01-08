//no additional imports! 
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
/**
* Priority queue represented as a balanced binary heap: the two
* children of queue[n] are queue[2*n+1] and queue[2*(n+1)].  The
* priority queue is ordered by the elements' natural ordering.
* @param <E> e
*/
public class MinHeap<E extends Comparable<E>> implements Queue<E> {
	//size of a queue when empty, should never shrink below this size
	/**
	 * Queue default size, cannot be shrink below this.
	 */
	private static final int DEFAULT_INITIAL_CAPACITY = 11;
	
	//you may not have any other instance variables, only these TWO
	//if you make more instance variables your MinHeap class will
	//receive a 0, no matter how well it works
	/**
	 * queue of types comparable to use for the priority Queue.
	 */
	private Comparable<E>[] queue;
	/**
	 * size to keep track of the queue size.
	 */
	private int size = 0;
	/**
	 * Constructor of the Min Heap.
	 */
	@SuppressWarnings("unchecked")
	public MinHeap() {
		//Initialize the queue here.
		
		//You cannot use the diamond syntax when creating an array, but you can cast to it. So you use this format:
		//ClassWithGeneric<T>[] items = (ClassWithGeneric<T>[]) new ClassWithGeneric[10];
		queue = (Comparable<E>[]) new Comparable[DEFAULT_INITIAL_CAPACITY];
	}
	/**
	 * Another constructor but it has a parameter type MinHeap.
	 * @param other heap
	 */
	@SuppressWarnings("unchecked")
	public MinHeap(MinHeap<E> other) {
		//Initialize the queue here to be a copy of the other queue
		queue = (Comparable<E>[]) new Comparable[other.size];
		size = other.size;
		for(int i = 0; i < other.size; i++){
			//queue[i] = other.queue[i];
			queue[i] = deepCopy(other.queue[i]);
		}
	}
	
				//You will need additional methods defined in the Queue interface!
    @Override
	public boolean addAll(Collection<? extends E> c){
		//if empty
		if(c == null || c.isEmpty()){
			throw new NullPointerException();
			
		}
		//if empty
		else if(this.size == 0){
			//Add all the stuff in c in and fix the queue
			int i = 0;
			for(E val: c){
				this.add(val);
			} 
			//build
			buildHeap();
		}
		//if not empty
		else{
			//add in
			Iterator<? extends E> iter = c.iterator();
			while(iter.hasNext()){
				E value = iter.next();
				this.offer(value); //add all value in c into heap
			}
			return true;
		}
		return false;
	}

	// private void doubleSize(Comparable<E>[] queue) {
	// 	Comparable<E>[] temp = (Comparable<E>[]) new Comparable[queue.length * 2];
	// 	System.arraycopy(queue, 0, temp, 0, queue.length);
	// 	queue = temp;
	// }
	/**
	 * double Size.
	 * @param queue to up
	 * @return queue
	 */
	private Comparable<E>[] doubleSize(Comparable<E>[] queue) {
		@SuppressWarnings("unchecked")
		Comparable<E>[] temp = (Comparable<E>[]) new Comparable[queue.length * 2];
		System.arraycopy(queue, 0, temp, 0, queue.length);
		return temp; // Return the new resized array
	}

	/** 
	 * Add inside heap.
	 * @param e to add
	 * @return boolean
	 */
	@Override
	public boolean add(E e){
		if(e == null){
			throw new NullPointerException();
		}
		if ((e instanceof Comparable) == false) {
			throw new ClassCastException();
		}
		if(size == queue.length){
			//resize if needed for the heap
			// Comparable<E>[] temp = (Comparable<E>[]) new Comparable[queue.length * 2];
			// System.arraycopy(queue, 0, temp, 0, size);
			// queue = temp;
			this.queue = doubleSize(this.queue);
		}
		queue[size] = e;
		size++;
		siftUp(size - 1, e);
		return true;
	}
	
	
	/** 
	 * get 1st val.
	 * @return 1st val
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E element(){
		if(queue == null){
			throw new NoSuchElementException();
		}
		return (E) queue[0];//idk 
	}

	//make the build On thing
	/**
	 * heapify the thing(fix the thing downward).
	 * @param index to fix
	 */
	@SuppressWarnings("unchecked")
	private void heapify(int index){
		int smallest = index;
		int leftChildIndex = 2*index +1;
		int rightChildIndex = 2*index +2;
		//Compare and check for validity
		//If prio of left is smaller than smallest (compare parent to left child) 
		if(leftChildIndex < size && queue[leftChildIndex].compareTo((E) queue[smallest]) < 0){
			//Assgin if parent bigger becasue it's min heap
			smallest = leftChildIndex;
		}
		//If prio of right is smaller and smallest (compare parent to right child)
		if(rightChildIndex < size && queue[rightChildIndex].compareTo((E) queue[smallest]) < 0){
			smallest = rightChildIndex;
		}
		//Change/swap element around if parent is bigger
		if(smallest != index){
			//Swap parent with child
			Comparable<E> temp = queue[index];
			queue[index] = queue[smallest];
			queue[smallest] = temp;
			//Check when you shift because shifting it self might cause others to lose it properties
			heapify(smallest);
		}
	}
	/**
	 * build head.
	 */
	private void buildHeap(){
		//for only the node that's not leaves node 
		for(int i = size/2 - 1; i >= 0; i--){
			//Heapify the whole heap so that all of it is heap valid
			heapify(i);
		}
	}

	//Fix the heap after insertion
	/**
	 * Siftup/bubble up for adding value.
	 * @param index to add
	 * @param addedValue to add
	 */
	@SuppressWarnings("unchecked")
	private void siftUp(int index, E addedValue){
		//Loop until root
		while(index > 0){
			//get parent val and compare it to value wanted to add in
			int parent = (index-1)/2; //index - 1 floor 2
			Comparable<E> temp = queue[parent]; 
			//if added value is bigger => correct then get out and assign the value into heap
			if(addedValue.compareTo((E) temp) >= 0){//If the parent is smaller than added val
				break;
			}
			//else then
			queue[index] = temp; //move parent down
			index = parent; //change index into its parent (moving it self up)
		}
		queue[index] = addedValue; //Assign when the corerct value reached(parent that's smaller than value)
	}
	
	
	/** 
	 * added value in retaning heap property.
	 * @param e ele to add
	 * @return boolean
	 */
	@Override
	public boolean offer(E e){
		if(e == null){
			return false;
		}
		if(size == queue.length){
			//resize if needed for the heap
			this.queue = doubleSize(this.queue);
		}
		queue[size] = e;
		size++;
		//Fix the array after adding.
		siftUp(size - 1, e);
		return true;
	}

	
	/** 
	 * get first val of heap.
	 * @return E
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E peek(){
		if(queue == null){
			return  null;
		}
		return (E) queue[0];//idk 
	}
	
	
	/** 
	 * remove the 1st.
	 * @return E
	 */
	@Override
	public E remove(){
		//remove root with the last element in queue
		//heapify
		if(queue == null){
			throw new NoSuchElementException();
		}
		//remove root with the last element in queue
		@SuppressWarnings("unchecked")
		E retNode = (E) queue[0];
		queue[0] = queue[size - 1];
		queue[size - 1] = null;
		size--;
		//Fix the value (last value) just swapped to it correct place
		heapify(0);
		return retNode;
	}

	
	/** 
	 * remove a specific object.
	 * @param o obj
	 * @return boolean
	 */
	@Override
    public boolean remove(Object o) {
        if(queue == null || size == 0){
			return false;
		}
		//E retNode = null;
		//Get index of value wanted to removeit
		int retIndex = -1;
		for(int i = 0; i < size; i++){
			if(queue[i].equals(o)){
				//retNode = (E) queue[i];
				retIndex = i;
				break;
			}
		}
		if(retIndex == -1){
			return false;
		}
		//swaped value wanted to remove with last value.
		queue[retIndex] = queue[size - 1];
		queue[size - 1] = null;
		size--;
		//fix at value just removed
		heapify(retIndex);
		return true;
    }

	
	/** 
	 * basically remove.
	 * @return E
	 */
	@Override
	public E poll(){
		//remove root with the last element in queue
		//heapify
		if(queue == null){
			return null;
		}
		//remove root with the last element in queue
		@SuppressWarnings("unchecked")
		E retNode = (E) queue[0];
		queue[0] = queue[size - 1];
		queue[size - 1] = null;
		size--;
		heapify(0);
		return retNode;
	}
	/**
	 * clear everything in heap.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void clear(){
		queue = (Comparable<E>[]) new Comparable[DEFAULT_INITIAL_CAPACITY];
		size = 0;
	}

	
	/** 
	 * get size of heap.
	 * @return int
	 */
	@Override
	public int size(){
		if(size > Integer.MAX_VALUE){
			return Integer.MAX_VALUE;
		}
		else{
			return this.size;
		}
	}

	
	/** 
	 * is heap empty or not.
	 * @return boolean
	 */
	@Override
	public boolean isEmpty(){
		if(size == 0){
			return true;
		}
		return false;
	}
	
	
	/** 
	 * Make a deep copy of the comparable.
	 * @param valToCpy node
	 * @return Comparable ret
	 */
	@SuppressWarnings("unchecked")
	private Comparable<E> deepCopy(Comparable<E> valToCpy) {
    	if (valToCpy instanceof Cloneable) {
									//So that the element can be handled
        	try {
            	// clone the stuff to make a actual deep copy
            	return (Comparable<E>) valToCpy.getClass().getMethod("clone").invoke(valToCpy);
        	} catch (Exception e) {
            	throw new IllegalStateException();
        	}
    	}
		return valToCpy;
	}
	
	
	/** 
	 * make a deep copy of current heap.
	 * @return Comparable ret
	 */
	@Override
	public Comparable<E>[] toArray(){
		@SuppressWarnings("unchecked")
		Comparable<E>[] temp = (Comparable<E>[]) new  Comparable[queue.length];
		// for(int i = 0; i < size; i++){
		// 	if(i == temp.length){
		// 		temp = doubleSize(temp);
		// 	}
		// 	temp[i] = queue[i];
		// }
		for (int i = 0; i < size; i++) {
			temp[i] = deepCopy(queue[i]);  // Deep copy of valid elements
		}
		return temp;
	}
	
	/** 
	 * make a deep copy of given arr as this.
	 * @return T[]
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a){
		//If this collection fits in the specified array with room to spare
		if(a.length >= size){
			for(int i = 0 ; i < a.length; i++){
				if(i < size){
					a[i] = (T) deepCopy(queue[i]);
				}
				else{
					a[i] = null;
				}
			}
			return a;
		}
		//if size of given arr is smaller than this
		else{
			//T[] temp = (T[]) new  T[queue.length];
			T[] temp = (T[]) new Object[queue.length];
			for (int i = 0; i < size; i++) {
				temp[i] = (T) deepCopy(queue[i]);  // Deep copy of valid elements
			}
			return temp;
		}
	}
	//This is an iterator for the queue, complete it!
	/**
	 * Needed to complete this/ fill it out.
	 * @return a new Iterator.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	//@SuppressWarnings("unchecked")
	@Override
	public Iterator<E> iterator() {
		return new Iterator() {
			private int index = 0;
            @Override
			public boolean hasNext() {
				if(index < size){
					return true;
				}
				return false;
			}
			
            @Override
			public E next() {
				if(hasNext() == false){
					throw new NoSuchElementException();
					//return null;
				}
				else{
					//i += 1;
					return (E) queue[index++];
				}
			}
		};
	}


    
	/** 
	 * nothing.
	 * @param o obj
	 * @return boolean
	 */
	@Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
				/** nothing.
	 * @param c collec
	 */
    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
				/** nothing.
	 * @param c col
	 */
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
				/** nothing.
	 * @param c col
	 */
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
	/**
	 * main.
	 * @param args main
	 */
	public static void main(String[] args) {
		Arrays hi = null;
		System.out.print(hi);
	}
	
}
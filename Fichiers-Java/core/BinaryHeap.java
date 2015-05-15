package core;

//
//******************PUBLIC OPERATIONS*********************
//void insert( x )       --> Insert x
//Comparable deleteMin( )--> Return and remove smallest item
//Comparable findMin( )  --> Return smallest item
//boolean isEmpty( )     --> Return true if empty; else false
//******************ERRORS********************************
//Throws RuntimeException for findMin and deleteMin when empty

import java.util.* ;

/**
* Implements a binary heap.
* Note that all "matching" is based on the compareTo method.
* @author Mark Allen Weiss
* @author DLB
*/
public class BinaryHeap<E extends Comparable<E>> {

 private int currentSize; // Number of elements in heap

 // Java genericity does not work with arrays.
 // We have to use an ArrayList
 private ArrayList<E> array; // The heap array
 private HashMap<E,Integer> map;

 /**
  * Construct the binary heap.
  */
 public BinaryHeap() {
     this.currentSize = 0;
     this.array = new ArrayList<E>() ;
     this.map = new HashMap<E,Integer>();
 }

 // Constructor used for debug.
 private BinaryHeap(BinaryHeap<E> heap) {
	this.currentSize = heap.currentSize ;
	this.array = new ArrayList<E>(heap.array) ;
	this.map = new HashMap<E,Integer>();
 }

 // Sets an element in the array
 private void arraySet(int index, E value) {
	 map.put(value,index);
	if (index == this.array.size()) {
	    this.array.add(value) ;
	}
	else {
	    this.array.set(index, value) ;
	}
 }

 /**
  * Test if the heap is logically empty.
  * @return true if empty, false otherwise.
  */
 public boolean isEmpty() { return this.currentSize == 0; }
 
 /**
  * Returns size.
  * @return current size.
  */
 public int size() { return this.currentSize; }
     
 
 /**
  * Returns index of parent.
  */
 private int index_parent(int index) {
	return (index - 1) / 2 ;
 }

 /**
  * Returns index of left child.
  */
 private int index_left(int index) {
	return index * 2 + 1 ;
 }

 /**
  * Insert into the heap.
  * @param x the item to insert.
  */
 public void insert(E x) {
	int index = this.currentSize++ ;
	this.arraySet(index, x) ;
	this.percolateUp(index) ;
 }

 /**
  * Internal method to percolate up in the heap.
  * @param index the index at which the percolate begins.
  */
 private void percolateUp(int index) {
	E x = this.array.get(index) ;

     for( ; index > 0 && x.compareTo(this.array.get(index_parent(index)) ) < 0; index = index_parent(index) ) {
	    E moving_val = this.array.get(index_parent(index)) ;
         this.arraySet(index, moving_val) ;
	}

     this.arraySet(index, x) ;
 }

 /**
  * Internal method to percolate down in the heap.
  * @param index the index at which the percolate begins.
  */
 private void percolateDown(int index) {
	int ileft = index_left(index) ;
	int iright = ileft + 1 ;

	if (ileft < this.currentSize) {
	    E current = this.array.get(index) ;
	    E left = this.array.get(ileft) ;
	    boolean hasRight = iright < this.currentSize ;
	    E right = (hasRight)?this.array.get(iright):null ;
	    
	    if (!hasRight || left.compareTo(right) < 0) {
		// Left is smaller
		if (left.compareTo(current) < 0) {
		    this.arraySet(index, left) ;
		    this.arraySet(ileft, current) ;
		    this.percolateDown( ileft ) ;
		}
	    }
	    else {
		// Right is smaller
		if (right.compareTo(current) < 0) {
		    this.arraySet(index, right) ;
		    this.arraySet(iright, current) ;
		    this.percolateDown( iright ) ;
		}		
	    }
	}
 }

 /**
  * Find the smallest item in the heap.
  * @return the smallest item.
  * @throws Exception if empty.
  */
 public E findMin( ) {
     if( isEmpty() )
         throw new RuntimeException( "Empty binary heap" );
     return this.array.get(0);
 }
 
 /**
  * Remove the smallest item from the heap.
  * @return the smallest item.
  * @throws Exception if empty.
  */
 public E deleteMin( ) {
     E minItem = findMin( );
	E lastItem = this.array.get(--this.currentSize) ;
	map.remove(minItem);
     this.arraySet(0, lastItem) ;
     this.percolateDown( 0 );
     return minItem;
 }
 
 /**
  * Prints the heap
  */
 public void print() {
	System.out.println() ;
	System.out.println("========  HEAP  (size = " + this.currentSize + ")  ========") ;
	System.out.println() ;

	for (int i = 0 ; i < this.currentSize ; i++) {
	    System.out.println(this.array.get(i).toString()) ;
	}

	System.out.println() ;
	System.out.println("--------  End of heap  --------") ;
	System.out.println() ;
 }

 /**
  * Prints the elements of the heap according to their respective order.
  */
 public void printSorted() {

	BinaryHeap<E> copy = new BinaryHeap<E>(this) ;

	System.out.println() ;
	System.out.println("========  Sorted HEAP  (size = " + this.currentSize + ")  ========") ;
	System.out.println() ;

	while (!copy.isEmpty()) {
	    System.out.println(copy.deleteMin()) ;
	}

	System.out.println() ;
	System.out.println("--------  End of heap  --------") ;
	System.out.println() ;
	}

public void reorganizeFrom(E e) {
	percolateUp(map.get(e));
}

public boolean contains(E e) {
	return map.containsKey(e);
}
}

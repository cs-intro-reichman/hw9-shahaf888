/**
 * Represents a list of Nodes. 
 */
public class LinkedList {
	
	private Node first; // pointer to the first element of this list
	private Node last;  // pointer to the last element of this list
	private int size;   // number of elements in this list
	
	/**
	 * Constructs a new list.
	 */ 
	public LinkedList () {
		first = null;
		last = first;
		size = 0;
	}
	
	/**
	 * Gets the first node of the list
	 * @return The first node of the list.
	 */		
	public Node getFirst() {
		return this.first;
	}

	/**
	 * Gets the last node of the list
	 * @return The last node of the list.
	 */		
	public Node getLast() {
		return this.last;
	}
	
	/**
	 * Gets the current size of the list
	 * @return The size of the list.
	 */		
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Gets the node located at the given index in this list. 
	 * 
	 * @param index
	 *        the index of the node to retrieve, between 0 and size
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than the list's size
	 * @return the node at the given index
	 */		
	public Node getNode(int index) {
		if (index < 0 || index > size || size == 0) {
			throw new IllegalArgumentException(
					"index must be between 0 and size");
		}
		Node current = first;
		int counter = 0;
		while(counter < index){
			current = current.next;
			counter++;
		}
		return current;
	}
	
	/**
	 * Creates a new Node object that points to the given memory block, 
	 * and inserts the node at the given index in this list.
	 * <p>
	 * If the given index is 0, the new node becomes the first node in this list.
	 * <p>
	 * If the given index equals the list's size, the new node becomes the last 
	 * node in this list.
     * <p>
	 * The method implementation is optimized, as follows: if the given 
	 * index is either 0 or the list's size, the addition time is O(1). 
	 * 
	 * @param block
	 *        the memory block to be inserted into the list
	 * @param index
	 *        the index before which the memory block should be inserted
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than the list's size
	 */
	public void add(int index, MemoryBlock block) {
		if (block == null){
			return;
		}
		Node n1 = new Node (block);
		if(index < 0 || index > size) {
			throw new IllegalArgumentException(
					"index must be between 0 and size");
		}
		else if (index == 0 || size == 0){

			addFirst(block);
			
		}
		else if (index == size){
			addLast(block);
		}
		else{
			Node current = getNode(index-1);
			Node helper = current.next;
			current.next = n1;
			n1.next = helper;
			size++;

		}

	}
	

	/**
	 * Creates a new node that points to the given memory block, and adds it
	 * to the end of this list (the node will become the list's last element).
	 * 
	 * @param block
	 *        the given memory block
	 */
	public void addLast(MemoryBlock block) {
		if (block == null){
			return;
		}
		Node newNode = new Node(block);
		if (size == 0){
			addFirst(block);
		}
		else{
			this.last.next = newNode;
			this.last = newNode;
			size++;

		}
	}
	
	/**
	 * Creates a new node that points to the given memory block, and adds it 
	 * to the beginning of this list (the node will become the list's first element).
	 * 
	 * @param block
	 *        the given memory block
	 */
	public void addFirst(MemoryBlock block) {
		if (block == null){
			return;
		}
		Node newNode = new Node(block);
		newNode.next = first;
		first = newNode;
		//if the list is empty
		if (size == 0){
			last = newNode;
		}
		size++;
	}

	/**
	 * Gets the memory block located at the given index in this list.
	 * 
	 * @param index
	 *        the index of the retrieved memory block
	 * @return the memory block at the given index
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than or equal to size
	 */
	public MemoryBlock getBlock(int index) {
		MemoryBlock mb = null;
		Node n1 = getNode(index);
		if (n1 != null){
			mb = n1.block;
		}
		return mb;
	}	

	/**
	 * Gets the index of the node pointing to the given memory block.
	 * 
	 * @param block
	 *        the given memory block
	 * @return the index of the block, or -1 if the block is not in this list
	 */
	public int indexOf(MemoryBlock block) {
		int counter = 0;
		Node n1 = new Node(null);
		for (int i = 0; i < size; i++){
			//i am not sure that this is the best imp. way
			n1 = getNode(i);
			if (n1.block.equals(block)){
				return i;
			}
		}
		return -1;
	}

	/**
	 * Removes the given node from this list.	
	 * 
	 * @param node
	 *        the node that will be removed from this list
	 */
	public void remove(Node node) {
		Node temp = null;
		if (node == null){
			throw  new NullPointerException ("ERROR NullPointerException!");
		}

		MemoryBlock mb = node.block;
		int index = this.indexOf(mb);
		if (index < 0){
			return;
		}

		if (index == 0){
			first = first.next;
			if (first == null){
				last = null;
			}
			size--; 
		}
		else if (index == size - 1){
			temp = getNode(size - 2);
			temp.next = null;
			last = temp;
			size--;
		}
		else {
			Node prev = getNode(index - 1);
			Node after = getNode(index).next;
			prev.next = after;
			size--;
		}


	}

	/**
	 * Removes from this list the node which is located at the given index.
	 * 
	 * @param index the location of the node that has to be removed.
	 * @throws IllegalArgumentException
	 *         if index is negative or greater than or equal to size
	 */
	public void remove (int index){
		if (index < 0 || index > size) {
			throw new IllegalArgumentException(
					"index must be between 0 and size");
		}
		Node n1 = getNode(index);
		remove(n1);
	}
	/**
	 * Removes from this list the node pointing to the given memory block.
	 * 
	 * @param block the memory block that should be removed from the list
	 * @throws IllegalArgumentException
	 *         if the given memory block is not in this list
	 */

	public void remove(MemoryBlock block){
		int index = indexOf(block);
		remove(index);
	}

	/**
	 * Returns an iterator over this list, starting with the first element.
	 */
	public ListIterator iterator(){
		return new ListIterator(first);
	}
	
	/**
	 * A textual representation of this list, for debugging.
	 */
	//maybe do i want to use list uterator
	public String toString() {
		//String ans = "(size : " + size + ")";
		String ans = "";
		for (int i = 0 ; i < size; i++){
			Node n1 = getNode(i);
			if (n1 != null){
				ans += n1.block.toString() + " ";
				//ans += " ----> ";
			}
		
		}

		return ans;
	}
}
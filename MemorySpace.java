
//import java.lang.classfile.components.ClassPrinter;

/**
 * Represents a managed memory space. The memory space manages a list of allocated 
 * memory blocks, and a list free memory blocks. The methods "malloc" and "free" are 
 * used, respectively, for creating new blocks and recycling existing blocks.
 */
public class MemorySpace {
	
	// A list of the memory blocks that are presently allocated
	private LinkedList allocatedList;

	// A list of memory blocks that are presently free
	private LinkedList freeList;

	/**
	 * Constructs a new managed memory space of a given maximal size.
	 * 
	 * @param maxSize
	 *            the size of the memory space to be managed
	 */
	public MemorySpace(int maxSize) {
		// initiallizes an empty list of allocated blocks.
		allocatedList = new LinkedList();
	    // Initializes a free list containing a single block which represents
	    // the entire memory. The base address of this single initial block is
	    // zero, and its length is the given memory size.
		freeList = new LinkedList();
		freeList.addLast(new MemoryBlock(0, maxSize));
	}

	/**
	 * Allocates a memory block of a requested length (in words). Returns the
	 * base address of the allocated block, or -1 if unable to allocate.
	 * 
	 * This implementation scans the freeList, looking for the first free memory block 
	 * whose length equals at least the given length. If such a block is found, the method 
	 * performs the following operations:
	 * 
	 * (1) A new memory block is constructed. The base address of the new block is set to
	 * the base address of the found free block. The length of the new block is set to the value 
	 * of the method's length parameter.
	 * 
	 * (2) The new memory block is appended to the end of the allocatedList.
	 * 
	 * (3) The base address and the length of the found free block are updated, to reflect the allocation.
	 * For example, suppose that the requested block length is 17, and suppose that the base
	 * address and length of the the found free block are 250 and 20, respectively.
	 * In such a case, the base address and length of of the allocated block
	 * are set to 250 and 17, respectively, and the base address and length
	 * of the found free block are set to 267 and 3, respectively.
	 * 
	 * (4) The new memory block is returned.
	 * 
	 * If the length of the found block is exactly the same as the requested length, 
	 * then the found block is removed from the freeList and appended to the allocatedList.
	 * 
	 * @param length
	 *        the length (in words) of the memory block that has to be allocated
	 * @return the base address of the allocated block, or -1 if unable to allocate
	 */

	public int malloc(int length){
		ListIterator lt1 = freeList.iterator();
		//System.out.println("we are here");
		int address = -1;
		int savedLength = -1;
		int min = -1;
		while (lt1.hasNext()){
			int nextLength = lt1.current.block.length;
			if (nextLength >= length){
				if(min == -1)
				{
					min = nextLength - length;
					address = lt1.current.block.baseAddress;
					savedLength = nextLength;
				}
				else{
					if (min > nextLength - length){
						min = nextLength - length;
						address = lt1.current.block.baseAddress;
						savedLength = nextLength;
					}
				}
			}
			lt1.current = lt1.current.next;
		}
		//System.out.println("address - " + address);
		if (address != -1){
			MemoryBlock newMB = new MemoryBlock(address, length);
			MemoryBlock oldMB = new MemoryBlock(address, savedLength);
			allocatedList.addLast(newMB);

			int helpIndex = freeList.indexOf(oldMB);
			freeList.remove(helpIndex);

			MemoryBlock updated = new MemoryBlock(address + length , savedLength - length);
			if (this.freeList.getSize() != 0 || savedLength - length != 0 )
			{
				freeList.add(helpIndex, updated);
			}
			//System.out.println("free");
			//System.out.println(freeList.toString());
			//System.out.println("allocated");
			//System.out.println(allocatedList.toString());
			//System.out.println("################################################################");
		}

		return address;
	} 
	

	/**
	 * Frees the memory block whose base address equals the given address.
	 * This implementation deletes the block whose base address equals the given 
	 * address from the allocatedList, and adds it at the end of the free list. 
	 * 
	 * @param baseAddress
	 *            the starting address of the block to freeList
	 */
	public void free(int address) {
		ListIterator lt1 = new ListIterator(allocatedList.getFirst());
		if (allocatedList.getSize() == 0){
			throw new IllegalArgumentException ("index must be between 0 and size");
		}
		while (lt1.hasNext() && lt1.current.block.baseAddress != address){
			lt1.next();
		}
		if (lt1.current != null){
			if (lt1.current.block.baseAddress == address){
				MemoryBlock mb = new MemoryBlock (address, lt1.current.block.length);
				allocatedList.remove(mb);
				freeList.addLast(mb);
			}
		}

	}
	
	/**
	 * A textual representation of the free list and the allocated list of this memory space, 
	 * for debugging purposes.
	 */
	public String toString() {
		return freeList.toString() + "\n" + allocatedList.toString();		
	}
	
	/**
	 * Performs defragmantation of this memory space.
	 * Normally, called by malloc, when it fails to find a memory block of the requested size.
	 * In this implementation Malloc does not call defrag.
	 */
	public void defrag() {
		ListIterator mainLit = freeList.iterator();
		ListIterator secondLit = freeList.iterator();
		while (mainLit.hasNext()){
			MemoryBlock mb = mainLit.next();
			int sum = mb.baseAddress + mb.length;
			while (secondLit.hasNext()){
				MemoryBlock mb2 = secondLit.next();
				if (sum == mb2.baseAddress){
					//adding the values to the memory block
					mb.length = mb.length + mb2.length;
					freeList.remove(mb2);
					defrag();
				}
			}
		}
	}
}

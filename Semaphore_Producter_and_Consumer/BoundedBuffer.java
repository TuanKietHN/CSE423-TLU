package Semaphore_Producter_and_Consumer;

/**
 * Bounded buffer implementation using semaphores for producer-consumer pattern
 * Based on "Concurrent and Distributed Computing in Java" by V. K. Garg
 */
public class BoundedBuffer {
    private final int size;
    private final Object[] buffer;
    private int inIndex = 0, outIndex = 0;
    private final CountingSemaphore empty;
    private final CountingSemaphore full;
    private final BinarySemaphore mutex;

    /**
     * Create a new bounded buffer
     * @param size maximum capacity of the buffer
     */
    public BoundedBuffer(int size) {
        this.size = size;
        this.buffer = new Object[size];
        this.empty = new CountingSemaphore(size);  // Initially all slots are empty
        this.full = new CountingSemaphore(0);      // Initially no slots are full
        this.mutex = new BinarySemaphore(true);    // Initially unlocked
    }

    /**
     * Deposit an item into the buffer
     * @param item the item to deposit
     */
    public void deposit(Object item) {
        empty.P();        // Wait if buffer is full
        mutex.P();        // Enter critical section
        
        buffer[inIndex] = item;
        inIndex = (inIndex + 1) % size;
        System.out.println("Producer deposited: " + item);
        
        mutex.V();        // Exit critical section
        full.V();         // Signal that a new item is available
    }

    /**
     * Remove an item from the buffer
     * @return the removed item
     */
    public Object fetch() {
        full.P();         // Wait if buffer is empty
        mutex.P();        // Enter critical section

        Object item = buffer[outIndex];
        outIndex = (outIndex + 1) % size;
        System.out.println("Consumer fetched: " + item);
        
        mutex.V();        // Exit critical section
        empty.V();        // Signal that a new slot is empty
        
        return item;
    }
}

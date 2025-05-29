package Semaphore_Reader_and_Writer;

/**
 * Binary semaphore implementation for concurrent programming
 * Based on "Concurrent and Distributed Computing in Java" by V. K. Garg
 */
public class BinarySemaphore {
    private boolean value;

    /**
     * Create a new binary semaphore
     * @param initValue initial value of the semaphore (true = unlocked, false = locked)
     */
    public BinarySemaphore(boolean initValue) {
        this.value = initValue;
    }

    /**
     * P operation (acquire/wait) - traditional semaphore terminology
     */
    public synchronized void P() {
        while (!value) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        value = false;
    }

    /**
     * V operation (release/signal) - traditional semaphore terminology
     */
    public synchronized void V() {
        value = true;
        notify();
    }
}

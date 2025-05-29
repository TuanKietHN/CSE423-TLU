package Semaphore_Producter_and_Consumer;

/**
 * Counting semaphore implementation for concurrent programming
 * Based on "Concurrent and Distributed Computing in Java" by V. K. Garg
 */
public class CountingSemaphore {
    private int value;

    /**
     * Create a new counting semaphore
     * @param initValue initial value of the semaphore (permits available)
     */
    public CountingSemaphore(int initValue) {
        this.value = initValue;
    }

    /**
     * P operation (acquire/wait) - traditional semaphore terminology
     */
    public synchronized void P() {
        while (value == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        value--;
    }

    /**
     * V operation (release/signal) - traditional semaphore terminology
     */
    public synchronized void V() {
        value++;
        notify();
    }
}

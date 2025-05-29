package Semaphore_Dining_Philosopher;

/**
 * Dining Philosophers implementation using semaphores
 * Based on "Concurrent and Distributed Computing in Java" by V. K. Garg
 */
public class DiningPhilosopher {
    private final int numPhilosophers;
    private final BinarySemaphore[] forks;
    private final BinarySemaphore mutex;  // For preventing deadlock

    /**
     * Create a new dining philosophers setup
     * @param numPhilosophers number of philosophers at the table
     */
    public DiningPhilosopher(int numPhilosophers) {
        this.numPhilosophers = numPhilosophers;
        this.forks = new BinarySemaphore[numPhilosophers];
        this.mutex = new BinarySemaphore(true);
        
        // Initialize all forks as available
        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new BinarySemaphore(true);
        }
    }

    /**
     * Get the ID of the left fork for a philosopher
     * @param philosopherId philosopher identifier
     * @return fork ID
     */
    private int leftFork(int philosopherId) {
        return philosopherId;
    }

    /**
     * Get the ID of the right fork for a philosopher
     * @param philosopherId philosopher identifier
     * @return fork ID
     */
    private int rightFork(int philosopherId) {
        return (philosopherId + 1) % numPhilosophers;
    }

    /**
     * Philosopher tries to pick up forks
     * @param philosopherId philosopher identifier
     */
    public void takeForks(int philosopherId) {
        mutex.P();  // Enter critical region to avoid deadlock
        
        System.out.println("Philosopher " + philosopherId + " is trying to take forks");
        
        // Take left fork
        forks[leftFork(philosopherId)].P();
        System.out.println("Philosopher " + philosopherId + " takes left fork");
        
        // Take right fork
        forks[rightFork(philosopherId)].P();
        System.out.println("Philosopher " + philosopherId + " takes right fork");
        
        mutex.V();  // Exit critical region
    }

    /**
     * Philosopher puts down forks
     * @param philosopherId philosopher identifier
     */
    public void putForks(int philosopherId) {
        System.out.println("Philosopher " + philosopherId + " is putting down forks");
        
        // Release right fork
        forks[rightFork(philosopherId)].V();
        System.out.println("Philosopher " + philosopherId + " puts down right fork");
        
        // Release left fork
        forks[leftFork(philosopherId)].V();
        System.out.println("Philosopher " + philosopherId + " puts down left fork");
    }
}

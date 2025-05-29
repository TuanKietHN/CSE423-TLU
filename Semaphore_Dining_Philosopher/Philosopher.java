package Semaphore_Dining_Philosopher;

/**
 * Philosopher class representing a philosopher in the dining philosophers problem
 * Based on "Concurrent and Distributed Computing in Java" by V. K. Garg
 */
public class Philosopher implements Runnable {
    private final int id;
    private final DiningPhilosopher table;
    
    /**
     * Create a new philosopher
     * @param id philosopher identifier
     * @param table reference to the dining philosopher manager
     */
    public Philosopher(int id, DiningPhilosopher table) {
        this.id = id;
        this.table = table;
    }
    
    /**
     * Simulate thinking activity
     */
    private void think() {
        System.out.println("Philosopher " + id + " is thinking");
        try {
            Thread.sleep((long)(Math.random() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Simulate eating activity
     */
    private void eat() {
        System.out.println("Philosopher " + id + " is eating");
        try {
            Thread.sleep((long)(Math.random() * 1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Run the philosopher's lifecycle
     */
    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {  // Each philosopher tries to eat 3 times
            think();
            
            table.takeForks(id);
            eat();
            table.putForks(id);
        }
    }
}
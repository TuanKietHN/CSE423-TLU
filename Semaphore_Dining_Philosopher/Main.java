package Semaphore_Dining_Philosopher;

/**
 * Main class demonstrating the dining philosophers problem using semaphores
 * Based on "Concurrent and Distributed Computing in Java" by V. K. Garg
 */
public class Main {
    public static void main(String[] args) {
        final int NUM_PHILOSOPHERS = 5;
        final DiningPhilosopher table = new DiningPhilosopher(NUM_PHILOSOPHERS);
        final Thread[] philosophers = new Thread[NUM_PHILOSOPHERS];
        
        System.out.println("Dining Philosophers demonstration started");
        
        // Create philosophers and start their activities
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            philosophers[i] = new Thread(new Philosopher(i, table));
            philosophers[i].start();
        }
        
        // Alternative approach using Java 8 lambda instead of implementing Runnable
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            final int philosopherId = i;
            Thread lambdaPhilosopher = new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    // Think
                    System.out.println("Lambda Philosopher " + philosopherId + " is thinking");
                    try {
                        Thread.sleep((long)(Math.random() * 1000));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    // Eat
                    table.takeForks(philosopherId);
                    System.out.println("Lambda Philosopher " + philosopherId + " is eating");
                    try {
                        Thread.sleep((long)(Math.random() * 1000));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    table.putForks(philosopherId);
                }
            });
            
            // Uncomment to use lambda-based philosophers instead
            // lambdaPhilosopher.start();
        }
        
        // Wait for all philosophers to complete
        try {
            for (Thread philosopher : philosophers) {
                philosopher.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Dining Philosophers demonstration completed");
    }
}

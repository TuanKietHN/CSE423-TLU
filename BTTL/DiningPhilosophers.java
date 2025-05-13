import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class DiningPhilosophers {
    private static final int NUM_PHILOSOPHERS = 5;
    private static final Semaphore[] forks = new Semaphore[NUM_PHILOSOPHERS];
    
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_PHILOSOPHERS);
        
        // Initialize forks
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            forks[i] = new Semaphore(1);
        }
        
        // Philosopher using lambda
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            final int philosopherId = i;
            Runnable philosopher = () -> {
                try {
                    while (true) {
                        System.out.println("Philosopher " + philosopherId + " is thinking");
                        Thread.sleep((long) (Math.random() * 1000));
                        
                        // Pick up forks
                        forks[philosopherId].acquire();
                        forks[(philosopherId + 1) % NUM_PHILOSOPHERS].acquire();
                        
                        System.out.println("Philosopher " + philosopherId + " is eating");
                        Thread.sleep((long) (Math.random() * 1000));
                        
                        // Put down forks
                        forks[philosopherId].release();
                        forks[(philosopherId + 1) % NUM_PHILOSOPHERS].release();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };
            executor.submit(philosopher);
        }
        
        // Run for 10 seconds then shutdown
        try {
            Thread.sleep(10000);
            executor.shutdownNow();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
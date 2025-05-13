import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ReaderWriter {
    private static final Semaphore readLock = new Semaphore(1);
    private static final Semaphore writeLock = new Semaphore(1);
    private static int sharedData = 0;
    
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        // Reader using lambda
        Runnable reader = () -> {
            try {
                while (true) {
                    readLock.acquire();
                    System.out.println("Reader read: " + sharedData);
                    readLock.release();
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        
        // Writer using lambda
        Runnable writer = () -> {
            try {
                while (true) {
                    writeLock.acquire();
                    sharedData++;
                    System.out.println("Writer wrote: " + sharedData);
                    writeLock.release();
                    Thread.sleep(1500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        
        // Start multiple readers and writers
        executor.submit(reader);
        executor.submit(reader);
        executor.submit(writer);
        executor.submit(writer);
        
        // Run for 10 seconds then shutdown
        try {
            Thread.sleep(10000);
            executor.shutdownNow();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
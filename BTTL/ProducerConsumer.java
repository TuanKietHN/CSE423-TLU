import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerConsumer {
    private static final int BUFFER_SIZE = 10;
    private static final Queue<Integer> buffer = new LinkedList<>();
    
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // Producer using lambda
        Runnable producer = () -> {
            try {
                while (true) {
                    synchronized (buffer) {
                        while (buffer.size() == BUFFER_SIZE) {
                            buffer.wait();
                        }
                        int item = (int) (Math.random() * 100);
                        buffer.add(item);
                        System.out.println("Produced: " + item);
                        buffer.notify();
                        Thread.sleep(1000);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        
        // Consumer using lambda
        Runnable consumer = () -> {
            try {
                while (true) {
                    synchronized (buffer) {
                        while (buffer.isEmpty()) {
                            buffer.wait();
                        }
                        int item = buffer.poll();
                        System.out.println("Consumed: " + item);
                        buffer.notify();
                        Thread.sleep(1500);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        
        executor.submit(producer);
        executor.submit(consumer);
        
        // Run for 10 seconds then shutdown
        try {
            Thread.sleep(10000);
            executor.shutdownNow();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
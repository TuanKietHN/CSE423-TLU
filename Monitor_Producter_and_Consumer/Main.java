package Monitor_Producter_and_Consumer;

/**
 * Producer-Consumer implementation using monitors in Java
 * Based on "Concurrent and Distributed Computing in Java" by V. K. Garg
 */
public class Main {
    public static void main(String[] args) {
        final BoundedBufferMonitor buffer = new BoundedBufferMonitor(5);
        
        // Producer using Java 8 lambda
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                final Integer item = i;
                buffer.deposit(item);
                System.out.println("Producer deposited: " + item);
                try {
                    Thread.sleep((long)(Math.random() * 500));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        // Consumer using Java 8 lambda
        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                Object item = buffer.fetch();
                System.out.println("Consumer fetched: " + item);
                try {
                    Thread.sleep((long)(Math.random() * 700));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        producer.start();
        consumer.start();
        
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Producer-Consumer monitor demonstration completed");
    }
}

/**
 * Bounded buffer implementation using monitors (synchronized methods)
 */
class BoundedBufferMonitor {
    private final Object[] buffer;
    private final int size;
    private int count = 0;
    private int in = 0;
    private int out = 0;
    
    public BoundedBufferMonitor(int size) {
        this.size = size;
        this.buffer = new Object[size];
    }
    
    /**
     * Deposit an item into the buffer.
     * This method works as a monitor by using synchronized keyword
     */
    public synchronized void deposit(Object item) {
        // Wait until there's space in the buffer
        while (count == size) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Add the item to the buffer
        buffer[in] = item;
        in = (in + 1) % size;
        count++;
        
        // Notify waiting threads (could be consumers waiting to fetch)
        notifyAll();
    }
    
    /**
     * Fetch an item from the buffer.
     * This method works as a monitor by using synchronized keyword
     */
    public synchronized Object fetch() {
        // Wait until there's at least one item in the buffer
        while (count == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Remove the item from the buffer
        Object item = buffer[out];
        out = (out + 1) % size;
        count--;
        
        // Notify waiting threads (could be producers waiting to deposit)
        notifyAll();
        
        return item;
    }
}

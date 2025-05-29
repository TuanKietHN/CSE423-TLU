package Semaphore_Producter_and_Consumer;

/**
 * Main class demonstrating the producer-consumer pattern using semaphores
 * Based on "Concurrent and Distributed Computing in Java" by V. K. Garg
 */
public class ProducerConsumerSemaphoreMain {
    public static void main(String[] args) {
        final BoundedBuffer buffer = new BoundedBuffer(5);
        
        // Producer using Java 8 lambda
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                buffer.deposit(i);
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
                buffer.fetch();
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
        
        System.out.println("Producer-Consumer demonstration completed");
    }
}

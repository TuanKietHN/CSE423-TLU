package Semaphore_Reader_and_Writer;

/**
 * Main class demonstrating the reader-writer pattern using semaphores
 * Based on "Concurrent and Distributed Computing in Java" by V. K. Garg
 */
public class Main {
    public static void main(String[] args) {
        final ReaderWriter rw = new ReaderWriter();
        final int NUM_READERS = 3;
        final int NUM_WRITERS = 2;
        
        // Create and start reader threads using Java 8 lambda
        for (int i = 1; i <= NUM_READERS; i++) {
            final int readerId = i;
            Thread reader = new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    try {
                        Thread.sleep((long)(Math.random() * 1000));  // Random delay before reading
                        
                        rw.startReading(readerId);
                        
                        // Simulate reading
                        System.out.println("Reader " + readerId + " is reading...");
                        Thread.sleep((long)(Math.random() * 1000));
                        
                        rw.endReading(readerId);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            reader.start();
        }
        
        // Create and start writer threads using Java 8 lambda and Runnable
        for (int i = 1; i <= NUM_WRITERS; i++) {
            final int writerId = i;
            Runnable writerTask = () -> {
                for (int j = 0; j < 2; j++) {
                    try {
                        Thread.sleep((long)(Math.random() * 1500));  // Random delay before writing
                        
                        rw.startWriting(writerId);
                        
                        // Simulate writing
                        System.out.println("Writer " + writerId + " is writing...");
                        Thread.sleep((long)(Math.random() * 1000));
                        
                        rw.endWriting(writerId);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            };
            
            new Thread(writerTask).start();
        }
    }
}

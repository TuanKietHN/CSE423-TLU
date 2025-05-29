package Monitor_Reader_and_Writer;

/**
 * Reader-Writer implementation using monitors in Java
 * Based on "Concurrent and Distributed Computing in Java" by V. K. Garg
 */
public class Main {
    public static void main(String[] args) {
        final ReaderWriterMonitor rw = new ReaderWriterMonitor();
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

/**
 * Monitor class for Reader-Writer problem
 */
class ReaderWriterMonitor {
    private int activeReaders = 0;
    private int activeWriters = 0;
    private int waitingWriters = 0;
    
    /**
     * Reader wants to start reading
     */
    public synchronized void startReading(int readerId) throws InterruptedException {
        // If there are active writers or waiting writers, wait
        while (activeWriters > 0 || waitingWriters > 0) {
            wait();
        }
        
        activeReaders++;
        System.out.println("Reader " + readerId + " starts reading. Total readers: " + activeReaders);
    }
    
    /**
     * Reader finishes reading
     */
    public synchronized void endReading(int readerId) {
        activeReaders--;
        System.out.println("Reader " + readerId + " ends reading. Total readers: " + activeReaders);
        
        // If this is the last reader leaving, notify waiting writers
        if (activeReaders == 0) {
            notifyAll();
        }
    }
    
    /**
     * Writer wants to start writing
     */
    public synchronized void startWriting(int writerId) throws InterruptedException {
        waitingWriters++;
        
        // If there are active readers or writers, wait
        while (activeReaders > 0 || activeWriters > 0) {
            wait();
        }
        
        waitingWriters--;
        activeWriters++;
        System.out.println("Writer " + writerId + " starts writing");
    }
    
    /**
     * Writer finishes writing
     */
    public synchronized void endWriting(int writerId) {
        activeWriters--;
        System.out.println("Writer " + writerId + " ends writing");
        
        // Notify all waiting threads (readers and writers)
        notifyAll();
    }
}

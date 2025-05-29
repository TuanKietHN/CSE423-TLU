package Semaphore_Reader_and_Writer;

/**
 * Reader-Writer implementation using semaphores
 * Based on "Concurrent and Distributed Computing in Java" by V. K. Garg
 */
public class ReaderWriter {
    private int readerCount = 0;
    private final BinarySemaphore mutex = new BinarySemaphore(true);    // For protecting readerCount
    private final BinarySemaphore db = new BinarySemaphore(true);       // For database access

    /**
     * Reader wants to access the resource
     * @param id Reader identifier
     */
    public void startReading(int id) {
        mutex.P();                    // Enter critical section for readerCount
        readerCount++;
        if (readerCount == 1) {       // First reader locks the database
            db.P();
        }
        System.out.println("Reader " + id + " starts reading. Total readers: " + readerCount);
        mutex.V();                    // Exit critical section
    }

    /**
     * Reader is done accessing the resource
     * @param id Reader identifier
     */
    public void endReading(int id) {
        mutex.P();                    // Enter critical section for readerCount
        readerCount--;
        System.out.println("Reader " + id + " ends reading. Total readers: " + readerCount);
        if (readerCount == 0) {       // Last reader unlocks the database
            db.V();
        }
        mutex.V();                    // Exit critical section
    }

    /**
     * Writer wants to access the resource
     * @param id Writer identifier
     */
    public void startWriting(int id) {
        db.P();                       // Lock the database exclusively
        System.out.println("Writer " + id + " starts writing");
    }

    /**
     * Writer is done accessing the resource
     * @param id Writer identifier
     */
    public void endWriting(int id) {
        System.out.println("Writer " + id + " ends writing");
        db.V();                       // Release the database
    }
}
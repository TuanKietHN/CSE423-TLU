package Semaphore.Reader_and_Writer;

class BinarySemaphore {
    private boolean value;

    public BinarySemaphore(boolean initValue) {
        this.value = initValue;
    }

    public synchronized void P() {
        while (!value) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        value = false;
    }

    public synchronized void V() {
        value = true;
        notify();
    }
}

public class ReaderWriter {
    private int readerCount = 0;
    private final BinarySemaphore mutex = new BinarySemaphore(true);
    private final BinarySemaphore wlock = new BinarySemaphore(true);

    public void startReading(int id) {
        mutex.P();
        readerCount++;
        if (readerCount == 1) {
            wlock.P();
        }
        System.out.println("Reader " + id + " starts reading. Total readers: " + readerCount);
        mutex.V();
    }

    public void endReading(int id) {
        mutex.P();
        readerCount--;
        System.out.println("Reader " + id + " ends reading. Total readers: " + readerCount);
        if (readerCount == 0) {
            wlock.V();
        }
        mutex.V();
    }

    public void startWriting(int id) {
        wlock.P();
        System.out.println("Writer " + id + " starts writing");
    }

    public void endWriting(int id) {
        System.out.println("Writer " + id + " ends writing");
        wlock.V();
    }
}


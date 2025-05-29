package Semaphore;

import java.util.LinkedList;
import java.util.Queue;

public class BoundedBuffer<T> {
    private final Queue<T> buffer = new LinkedList<>();
    private final int capacity;
    private final CountingSemaphore full;
    private final CountingSemaphore empty;
    private final BinarySemaphore mutex = new BinarySemaphore();

    public BoundedBuffer(int capacity) {
        this.capacity = capacity;
        this.full = new CountingSemaphore(capacity);
        this.empty = new CountingSemaphore(0);
    }

    public void produce(T item) throws InterruptedException {
        full.acquire();
        mutex.acquire();
        buffer.add(item);
        System.out.println("Produced: " + item);
        mutex.release();
        empty.release();
    }

    public T consume() throws InterruptedException {
        empty.acquire();
        mutex.acquire();
        T item = buffer.remove();
        System.out.println("Consumed: " + item);
        mutex.release();
        full.release();
        return item;
    }
}

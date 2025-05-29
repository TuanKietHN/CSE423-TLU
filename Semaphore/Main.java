package Semaphore;

import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(5);

        Runnable producer = () -> {
            IntStream.range(1, 11).forEach(i -> {
                try {
                    buffer.produce(i);
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        };

        Runnable consumer = () -> {
            IntStream.range(1, 11).forEach(i -> {
                try {
                    buffer.consume();
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        };

        new Thread(producer).start();
        new Thread(consumer).start();
    }
}

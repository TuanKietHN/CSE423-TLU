package Lab3.Semaphore;

import java.util.ArrayList;
import java.util.Random;

public class Main {
    private static final ArrayList<Integer> buffer = new ArrayList<>();
    private static final int MAX_BUFFER_SIZE = 20;

    private static final BinarySemaphore producerSemaphore = new BinarySemaphore();
    private static final BinarySemaphore consumerSemaphore = new BinarySemaphore();
    private static final CountingSemaphore bufferSemaphore = new CountingSemaphore(MAX_BUFFER_SIZE);

    private static final Object lock = new Object(); 

    private static volatile boolean running = true;

    public static void main(String[] args) {
        System.out.println("=== CHƯƠNG TRÌNH SEMAPHORE DEMO (ArrayList) ===");

        producerSemaphore.release();

        Thread t1 = new Thread(new NumberProducer(), "T1-Producer");
        Thread t2 = new Thread(new NumberConsumer(), "T2-Consumer");

        t1.start();
        t2.start();

        try {
            Thread.sleep(15000);
            running = false;

            t1.join();
            t2.join();

            System.out.println("\n=== CHƯƠNG TRÌNH KẾT THÚC ===");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread bị interrupt: " + e.getMessage());
        }
    }

    static class NumberProducer implements Runnable {
        private final Random random = new Random();

        @Override
        public void run() {
            System.out.println("T1 (Producer) bắt đầu sinh số...");

            while (running) {
                try {
                    producerSemaphore.acquire();
                    bufferSemaphore.acquire();

                    int number = random.nextInt(191) + 10;

                    synchronized (lock) {
                        buffer.add(number);
                        System.out.println("T1 sinh số: " + number + " | Buffer size: " + buffer.size());
                    }

                    consumerSemaphore.release();
                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("T1 bị interrupt: " + e.getMessage());
                    break;
                }
            }

            System.out.println("T1 (Producer) kết thúc.");
        }
    }

    static class NumberConsumer implements Runnable {
        @Override
        public void run() {
            System.out.println("T2 (Consumer) bắt đầu xử lý số...");

            while (running || !buffer.isEmpty()) {
                try {
                    consumerSemaphore.acquire();

                    Integer number = null;
                    synchronized (lock) {
                        if (!buffer.isEmpty()) {
                            number = buffer.remove(0);
                        }
                    }

                    if (number != null) {
                        processNumber(number);
                        bufferSemaphore.release();
                        producerSemaphore.release();
                        Thread.sleep(300);
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("T2 bị interrupt: " + e.getMessage());
                    break;
                }
            }

            System.out.println("T2 (Consumer) kết thúc.");
        }

        private void processNumber(int number) {
            boolean isPrime = isPrimeNumber(number);
            boolean isPerfectSquare = isPerfectSquare(number);

            if (isPrime) {
                System.out.println(number + " - Số nguyên tố");
            } else if (isPerfectSquare) {
                System.out.println(number + " - Số chính phương");
            } else {
                System.out.println(number + " - Số thường");
            }
        }
    }

    private static boolean isPrimeNumber(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;

        int sqrt = (int) Math.sqrt(n);
        for (int i = 3; i <= sqrt; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private static boolean isPerfectSquare(int n) {
        int sqrt = (int) Math.sqrt(n);
        return sqrt * sqrt == n;
    }
}


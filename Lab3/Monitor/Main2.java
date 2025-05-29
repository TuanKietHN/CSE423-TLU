package Lab3.Monitor;

import java.util.ArrayList;
import java.util.Random;

public class Main2 {
    private static final ArrayList<Integer> buffer = new ArrayList<>();
    private static final int MAX_BUFFER_SIZE = 10;
    private static final Object lock = new Object();
    private static volatile boolean running = true;

    public static void main(String[] args) {
        System.out.println("=== CHƯƠNG TRÌNH MONITOR DEMO (ArrayList + synchronized) ===");

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
                int number = random.nextInt(191) + 10;

                synchronized (lock) {
                    while (buffer.size() >= MAX_BUFFER_SIZE) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    buffer.add(number);
                    System.out.println("T1 sinh số: " + number + " | Buffer size: " + buffer.size());
                    lock.notifyAll();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
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
                int number = -1;

                synchronized (lock) {
                    while (buffer.isEmpty()) {
                        try {
                            if (!running) return;
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    number = buffer.remove(0);
                    lock.notifyAll();
                }

                processNumber(number);

                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            System.out.println("T2 (Consumer) kết thúc.");
        }

        private void processNumber(int number) {
            if (isPrimeNumber(number)) {
                System.out.println("    ✓ " + number + " - Số nguyên tố");
            } else if (isPerfectSquare(number)) {
                System.out.println("    ✓ " + number + " - Số chính phương");
            } else {
                System.out.println("    • " + number + " - Số thường");
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

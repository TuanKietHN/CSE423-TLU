package Lab3.Monitor;

import java.util.Random;

/**
 * BoundedBufferMonitor - Quản lý luồng Producer và Consumer sử dụng Monitor
 */
public class BoundedBufferMonitor {
    private final BoundedBuffer buffer;
    private volatile boolean running = true;

    /**
     * Constructor khởi tạo buffer với dung lượng cho trước
     */
    public BoundedBufferMonitor(int capacity) {
        this.buffer = new BoundedBuffer(capacity);
    }

    /**
     * Tạo luồng Producer sử dụng lambda
     */
    public Thread createProducer() {
        return new Thread(() -> {
            System.out.println("T1 (Producer) bắt đầu sinh số...");
            Random random = new Random();
            while (running) {
                try {
                    double number = random.nextInt(191) + 10; // Sinh số từ 10-200
                    buffer.put(number);
                    System.out.println("T1 sinh số: " + number + " | Buffer size: " + buffer.size());
                    Thread.sleep(500); // Nghỉ 500ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("T1 bị interrupt: " + e.getMessage());
                    break;
                }
            }
            System.out.println("T1 (Producer) kết thúc.");
        }, "T1-Producer");
    }

    /**
     * Tạo luồng Consumer sử dụng lambda
     */
    public Thread createConsumer() {
        return new Thread(() -> {
            System.out.println("T2 (Consumer) bắt đầu xử lý số...");
            while (running || buffer.size() > 0) {
                try {
                    Double number = buffer.take();
                    if (number != null) {
                        processNumber(number);
                        Thread.sleep(300); // Nghỉ 300ms
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("T2 bị interrupt: " + e.getMessage());
                    break;
                }
            }
            System.out.println("T2 (Consumer) kết thúc.");
        }, "T2-Consumer");
    }

    /**
     * Xử lý và phân loại số
     */
    private void processNumber(double number) {
        int intNumber = (int) number; // Ép kiểu double về int
        boolean isPrime = isPrimeNumber(intNumber);
        boolean isPerfectSquare = isPerfectSquare(intNumber);

        if (isPrime) {
            System.out.println("    ✓ " + intNumber + " - Số nguyên tố");
        } else if (isPerfectSquare) {
            System.out.println("    ✓ " + intNumber + " - Số chính phương");
        } else {
            System.out.println("    • " + intNumber + " - Số thường");
        }
    }

    /**
     * Kiểm tra số nguyên tố
     */
    private boolean isPrimeNumber(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;

        int sqrt = (int) Math.sqrt(n);
        for (int i = 3; i <= sqrt; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Kiểm tra số chính phương
     */
    private boolean isPerfectSquare(int n) {
        if (n < 0) return false;
        int sqrt = (int) Math.sqrt(n);
        return sqrt * sqrt == n;
    }

    /**
     * Dừng chương trình
     */
    public void stop() {
        running = false;
    }
}
package Lab3.Semaphore;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Chương trình chính sử dụng Semaphore để đồng bộ hóa giữa hai luồng
 * T1: Sinh số ngẫu nhiên từ 10-200
 * T2: Kiểm tra số nguyên tố và số chính phương
 */
public class Main2 {
    // Shared buffer để truyền dữ liệu giữa các luồng
    private static final BlockingQueue<Integer> buffer = new LinkedBlockingQueue<>(20);
    
    // Binary semaphore để đồng bộ hóa việc sinh và xử lý số
    private static final BinarySemaphore producerSemaphore = new BinarySemaphore();
    private static final BinarySemaphore consumerSemaphore = new BinarySemaphore();
    
    // Counting semaphore để kiểm soát số lượng phần tử trong buffer
    private static final CountingSemaphore bufferSemaphore = new CountingSemaphore(20);
    
    // Flag để dừng chương trình
    private static volatile boolean running = true;
    
    public static void main(String[] args) {
        System.out.println("=== CHƯƠNG TRÌNH SEMAPHORE DEMO ===");
        System.out.println("T1: Sinh số ngẫu nhiên từ 10-200");
        System.out.println("T2: Kiểm tra số nguyên tố và số chính phương");
        System.out.println("=====================================\n");
        
        // Khởi tạo semaphore cho producer
        producerSemaphore.release();
        
        // Tạo và khởi động luồng T1 (Producer)
        Thread t1 = new Thread(new NumberProducer(), "T1-Producer");
        
        // Tạo và khởi động luồng T2 (Consumer)
        Thread t2 = new Thread(new NumberConsumer(), "T2-Consumer");
        
        t1.start();
        t2.start();
        
        // Chạy trong 15 giây rồi dừng
        try {
            Thread.sleep(15000);
            running = false;
            
            // Chờ các luồng kết thúc
            t1.join();
            t2.join();
            
            System.out.println("\n=== CHƯƠNG TRÌNH KẾT THÚC ===");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread bị interrupt: " + e.getMessage());
        }
    }
    
    /**
     * Luồng T1 - Producer: Sinh số ngẫu nhiên từ 10-200
     */
    static class NumberProducer implements Runnable {
        private final Random random = new Random();
        
        @Override
        public void run() {
            System.out.println("T1 (Producer) bắt đầu sinh số...");
            
            while (running) {
                try {
                    // Chờ semaphore producer
                    producerSemaphore.acquire();
                    
                    // Kiểm tra buffer space
                    bufferSemaphore.acquire();
                    
                    // Sinh số ngẫu nhiên từ 10-200
                    int number = random.nextInt(191) + 10; // 10 + (0-190)
                    
                    // Thêm vào buffer
                    buffer.offer(number);
                    
                    System.out.println("T1 sinh số: " + number + " | Buffer size: " + buffer.size());
                    
                    // Giải phóng semaphore cho consumer
                    consumerSemaphore.release();
                    
                    // Nghỉ 500ms
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
    
    /**
     * Luồng T2 - Consumer: Xử lý số và kiểm tra tính chất
     */
    static class NumberConsumer implements Runnable {
        
        @Override
        public void run() {
            System.out.println("T2 (Consumer) bắt đầu xử lý số...");
            
            while (running || !buffer.isEmpty()) {
                try {
                    // Chờ semaphore consumer
                    consumerSemaphore.acquire();
                    
                    // Lấy số từ buffer
                    Integer number = buffer.poll();
                    
                    if (number != null) {
                        // Xử lý số
                        processNumber(number);
                        
                        // Giải phóng buffer space
                        bufferSemaphore.release();
                        
                        // Giải phóng semaphore cho producer
                        producerSemaphore.release();
                        
                        // Nghỉ 300ms
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
        
        /**
         * Xử lý và phân loại số
         */
        private void processNumber(int number) {
            boolean isPrime = isPrimeNumber(number);
            boolean isPerfectSquare = isPerfectSquare(number);
            
            if (isPrime) {
                System.out.println("    ✓ " + number + " - Số nguyên tố");
            } else if (isPerfectSquare) {
                System.out.println("    ✓ " + number + " - Số chính phương");
            } else {
                System.out.println("    • " + number + " - Số thường");
            }
        }
    }
    
    /**
     * Kiểm tra số nguyên tố
     * @param n số cần kiểm tra
     * @return true nếu là số nguyên tố
     */
    private static boolean isPrimeNumber(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        
        // Kiểm tra từ 3 đến sqrt(n), chỉ các số lẻ
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
     * @param n số cần kiểm tra
     * @return true nếu là số chính phương
     */
    private static boolean isPerfectSquare(int n) {
        if (n < 0) return false;
        
        int sqrt = (int) Math.sqrt(n);
        return sqrt * sqrt == n;
    }
}

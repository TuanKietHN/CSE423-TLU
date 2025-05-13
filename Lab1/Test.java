package Lab1;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Scanner;
public class Test {
    
    private static final int MIN_ARRAY_SIZE = 20;
    private static final int MAX_VALUE = 10000;
    private static final Object lock = new Object();
    
    public static void main(String[] args) {
        var arraySize = 20; // Có thể thay đổi kích thước mảng (>= 20)
        var numThreads = 2; // Số luồng mặc định
        
        // Kiểm tra nếu người dùng cung cấp đối số dòng lệnh
        if (args.length > 0) {
            try {
                arraySize = Integer.parseInt(args[0]);
                if (arraySize < MIN_ARRAY_SIZE) {
                    arraySize = MIN_ARRAY_SIZE;
                    System.out.println("Kich thuoc mang đa duoc dat la toi thieu " + MIN_ARRAY_SIZE);
                }
            } catch (NumberFormatException e) {
                System.out.println("Doi so khong hop le. Su dung kich thuoc mang mac dinh: " + arraySize);
            }
        }
        
        if (args.length > 1) {
            try {
                numThreads = Integer.parseInt(args[1]);
                if (numThreads < 1) {
                    numThreads = 2;
                    System.out.println("So luong khong hop le. Su dung so luong mac dinh: 2");
                }
            } catch (NumberFormatException e) {
                System.out.println("Doi so khong hop le. Su dung so luong mac dinh: 2");
            }
        }
        
        // Tạo mảng số nguyên dương ngẫu nhiên
        var randomArray = generateRandomArray(arraySize);
        
        // In mảng ngẫu nhiên
        System.out.println("Mang ngau nhien:");
        printArray(randomArray);
        
        // Tìm số chính phương với 1 luồng
        System.out.println("\n--- Tim so chinh phuong voi 1 luong ---");
        var startTime1 = System.nanoTime();
        var perfectSquares1 = findPerfectSquaresSequential(randomArray);
        var endTime1 = System.nanoTime();
        
        System.out.println("Cac so chinh phuong tim duoc:");
        printPerfectSquares(perfectSquares1);
        System.out.println("Tong so: " + perfectSquares1.size());
        System.out.println("Thoi gian thuc thi: " + (endTime1 - startTime1) / 1_000_000.0 + " ms");
        
        // Tìm số chính phương với 2 luồng
        System.out.println("\n--- Tim so chinh phuong voi 2 luong ---");
        var startTime2 = System.nanoTime();
        var perfectSquares2 = findPerfectSquaresWithTwoThreads(randomArray);
        var endTime2 = System.nanoTime();
        
        System.out.println("Tong so: " + perfectSquares2.size());
        System.out.println("Thoi gian thuc thi: " + (endTime2 - startTime2) / 1_000_000.0 + " ms");
        
        // Mở rộng với k luồng
        System.out.println("\n--- Tim so chinh phuong voi " + numThreads + " luong ---");
        var startTime3 = System.nanoTime();
        var perfectSquares3 = findPerfectSquaresWithKThreads(randomArray, numThreads);
        var endTime3 = System.nanoTime();
        
        System.out.println("Tong so: " + perfectSquares3.size());
        System.out.println("Thoi gian thuc thi: " + (endTime3 - startTime3) / 1_000_000.0 + " ms");
        
        // So sánh thời gian
        System.out.println("\n--- So sanh thoi gian ---");
        System.out.println("1 thread: " + (endTime1 - startTime1) / 1_000_000.0 + " ms");
        System.out.println("2 thread: " + (endTime2 - startTime2) / 1_000_000.0 + " ms");
        System.out.println(numThreads + " thread: " + (endTime3 - startTime3) / 1_000_000.0 + " ms");
        
        double speedup2 = (double)(endTime1 - startTime1) / (endTime2 - startTime2);
        double speedupK = (double)(endTime1 - startTime1) / (endTime3 - startTime3);
        
        System.out.println("Tang toc voi 2 luong: " + String.format("%.2f", speedup2) + "x");
        System.out.println("Tang toc voi " + numThreads + " luong: " + String.format("%.2f", speedupK) + "x");
    }
    
    /**
     * Tạo mảng số nguyên dương ngẫu nhiên
     */
    private static int[] generateRandomArray(int size) {
        var random = new Random();
        var array = new int[size];
        
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(MAX_VALUE) + 1; // Số nguyên dương từ 1 đến MAX_VALUE
        }
        
        return array;
    }
    
    /**
     * In mảng
     */
    private static void printArray(int[] array) {
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    
    /**
     * In danh sách các số chính phương và vị trí
     */
    private static void printPerfectSquares(List<PerfectSquare> perfectSquares) {
        for (var ps : perfectSquares) {
            System.out.println("Vi tri " + ps.index() + ": " + ps.value() + " la so chinh phuong");
        }
    }
    
    /**
     * Kiểm tra số chính phương
     */
    private static boolean isPerfectSquare(int num) {
        if (num < 0) return false;
        var sqrt = (int) Math.sqrt(num);
        return sqrt * sqrt == num;
    }
    
    /**
     * Tìm số chính phương tuần tự
     */
    private static List<PerfectSquare> findPerfectSquaresSequential(int[] array) {
        var result = new ArrayList<PerfectSquare>();
        
        for (int i = 0; i < array.length; i++) {
            if (isPerfectSquare(array[i])) {
                result.add(new PerfectSquare(i, array[i]));
            }
        }
        
        return result;
    }
    
    /**
     * Tìm số chính phương với 2 luồng
     */
    private static List<PerfectSquare> findPerfectSquaresWithTwoThreads(int[] array) {
        var result = new CopyOnWriteArrayList<PerfectSquare>();
        var latch = new CountDownLatch(2);
        var totalCount = new AtomicInteger(0);
        
        // Luồng T1: tìm từ 0 đến N/2-1
        var thread1 = new Thread(() -> {
            int start = 0;
            int end = array.length / 2;
            
            for (int i = start; i < end; i++) {
                if (isPerfectSquare(array[i])) {
                    var ps = new PerfectSquare(i, array[i]);
                    result.add(ps);
                    totalCount.incrementAndGet();
                    
                    synchronized (lock) {
                        System.out.println("Thread 1: Vi tri " + i + ": " + array[i] + " la so chinh phuong");
                        try {
                            // Thông báo cho các luồng khác
                            lock.notifyAll();
                            // Đợi lượt của luồng khác
                            lock.wait(10); // Timeout để tránh deadlock
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
            
            latch.countDown();
        });
        
        // Luồng T2: tìm từ N/2 đến N-1
        var thread2 = new Thread(() -> {
            int start = array.length / 2;
            int end = array.length;
            
            for (int i = start; i < end; i++) {
                if (isPerfectSquare(array[i])) {
                    var ps = new PerfectSquare(i, array[i]);
                    result.add(ps);
                    totalCount.incrementAndGet();
                    
                    synchronized (lock) {
                        System.out.println("Thread 2: Vi tri " + i + ": " + array[i] + " la so chinh phuong");
                        try {
                            // Thông báo cho các luồng khác
                            lock.notifyAll();
                            // Đợi lượt của luồng khác
                            lock.wait(10); // Timeout để tránh deadlock
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
            
            latch.countDown();
        });
        
        // Bắt đầu hai luồng
        thread1.start();
        thread2.start();
        
        try {
            // Đợi hai luồng hoàn thành
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return result;
    }
    
    /**
     * Tìm số chính phương với k luồng
     */
    private static List<PerfectSquare> findPerfectSquaresWithKThreads(int[] array, int k) {
        var result = new CopyOnWriteArrayList<PerfectSquare>();
        var latch = new CountDownLatch(k);
        var totalCount = new AtomicInteger(0);
        var threads = new Thread[k];
        
        int chunkSize = array.length / k;
        
        for (int t = 0; t < k; t++) {
            final int threadId = t + 1;
            final int startIndex = t * chunkSize;
            final int endIndex = (t == k - 1) ? array.length : (t + 1) * chunkSize;
            
            threads[t] = new Thread(() -> {
                for (int i = startIndex; i < endIndex; i++) {
                    if (isPerfectSquare(array[i])) {
                        var ps = new PerfectSquare(i, array[i]);
                        result.add(ps);
                        totalCount.incrementAndGet();
                        
                        synchronized (lock) {
                            System.out.println("Thread " + threadId + ": Vi tri " + i + ": " + array[i] + " la so chinh phuong");
                            try {
                                // Thông báo cho các luồng khác
                                lock.notifyAll();
                                // Đợi lượt của luồng khác
                                lock.wait(10); // Timeout để tránh deadlock
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                }
                
                latch.countDown();
            });
            
            threads[t].start();
        }
        
        try {
            // Đợi tất cả các luồng hoàn thành
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return result;
    }
    
    /**
     * Record để lưu thông tin về số chính phương và vị trí của nó
     */
    private record PerfectSquare(int index, int value) {
    }
}
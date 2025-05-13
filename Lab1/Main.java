package Lab1;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

public class Main {

    private static final int MIN_ARRAY_SIZE = 20;
    private static final int MAX_VALUE = 10;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int arraySize;
        int numThreads;

        System.out.print("Nhap kich thuoc mang (toi thieu " + MIN_ARRAY_SIZE + "): ");
        while (true) {
            try {
                arraySize = Integer.parseInt(scanner.nextLine().trim());
                if (arraySize < MIN_ARRAY_SIZE) {
                    System.out.print("Kich thuoc mang phai it nhat " + MIN_ARRAY_SIZE + ", vui long nhap lai: ");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.print("Gia tri khong hop le, vui long nhap mot so nguyen: ");
            }
        }

        System.out.print("Nhap so luong luong (toi thieu 1): ");
        while (true) {
            try {
                numThreads = Integer.parseInt(scanner.nextLine().trim());
                if (numThreads < 1) {
                    System.out.print("So luong luong phai it nhat 1, vui long nhap lai: ");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.print("Gia tri khong hop le, vui long nhap mot so nguyen: ");
            }
        }

        var randomArray = generateRandomArray(arraySize);

        System.out.println("Mang ngau nhien:");
        printArray(randomArray);

        System.out.println("\n--- Tim so chinh phuong voi 1 luong ---");
        var startTime1 = System.nanoTime();
        var perfectSquares1 = findPerfectSquaresSequential(randomArray);
        var endTime1 = System.nanoTime();

        System.out.println("Cac so chinh phuong tim duoc:");
        printPerfectSquares(perfectSquares1);
        System.out.println("Tong so: " + perfectSquares1.size());
        System.out.println("Thoi gian thuc thi: " + (endTime1 - startTime1) / 1_000_000.0 + " ms");

        System.out.println("\n--- Tim so chinh phuong voi 2 luong ---");
        var startTime2 = System.nanoTime();
        var perfectSquares2 = findPerfectSquaresWithTwoThreads(randomArray);
        var endTime2 = System.nanoTime();

        System.out.println("Tong so: " + perfectSquares2.size());
        System.out.println("Thoi gian thuc thi: " + (endTime2 - startTime2) / 1_000_000.0 + " ms");

        System.out.println("\n--- Tim so chinh phuong voi " + numThreads + " luong ---");
        var startTime3 = System.nanoTime();
        var perfectSquares3 = findPerfectSquaresWithKThreads(randomArray, numThreads);
        var endTime3 = System.nanoTime();

        System.out.println("Tong so: " + perfectSquares3.size());
        System.out.println("Thoi gian thuc thi: " + (endTime3 - startTime3) / 1_000_000.0 + " ms");

        System.out.println("\n--- So sanh thoi gian ---");
        System.out.println("1 luong: " + (endTime1 - startTime1) / 1_000_000.0 + " ms");
        System.out.println("2 luong: " + (endTime2 - startTime2) / 1_000_000.0 + " ms");
        System.out.println(numThreads + " luong: " + (endTime3 - startTime3) / 1_000_000.0 + " ms");

        double speedup2 = (double) (endTime1 - startTime1) / (endTime2 - startTime2);
        double speedupK = (double) (endTime1 - startTime1) / (endTime3 - startTime3);

        System.out.println("Tang toc voi 2 luong: " + String.format("%.2f", speedup2) + "x");
        System.out.println("Tang toc voi " + numThreads + " luong: " + String.format("%.2f", speedupK) + "x");

        scanner.close();
    }

    private static int[] generateRandomArray(int size) {
        var random = new Random();
        var array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(MAX_VALUE) + 1;
        }
        return array;
    }

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

    private static void printPerfectSquares(List<PerfectSquare> perfectSquares) {
        for (var ps : perfectSquares) {
            System.out.println("Vi tri " + ps.index() + ": " + ps.value() + " la so chinh phuong");
        }
    }

    private static boolean isPerfectSquare(int num) {
        if (num < 0) return false;
        int sqrt = (int) Math.sqrt(num);
        return sqrt * sqrt == num;
    }

    private static List<PerfectSquare> findPerfectSquaresSequential(int[] array) {
        var result = new ArrayList<PerfectSquare>();
        for (int i = 0; i < array.length; i++) {
            if (isPerfectSquare(array[i])) {
                result.add(new PerfectSquare(i, array[i]));
            }
        }
        return result;
    }

    private static List<PerfectSquare> findPerfectSquaresWithTwoThreads(int[] array) {
        var result = new CopyOnWriteArrayList<PerfectSquare>();
        var latch = new CountDownLatch(2);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < array.length / 2; i++) {
                if (isPerfectSquare(array[i])) {
                    result.add(new PerfectSquare(i, array[i]));
                    synchronized (lock) {
                        System.out.println("Luong 1: Vi tri " + i + ": " + array[i] + " la so chinh phuong");
                        lock.notifyAll();
                        try {
                            lock.wait(10);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
            latch.countDown();
        });

        Thread thread2 = new Thread(() -> {
            for (int i = array.length / 2; i < array.length; i++) {
                if (isPerfectSquare(array[i])) {
                    result.add(new PerfectSquare(i, array[i]));
                    synchronized (lock) {
                        System.out.println("Luong 2: Vi tri " + i + ": " + array[i] + " la so chinh phuong");
                        lock.notifyAll();
                        try {
                            lock.wait(10);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
            latch.countDown();
        });

        thread1.start();
        thread2.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return result;
    }

    private static List<PerfectSquare> findPerfectSquaresWithKThreads(int[] array, int k) {
        var result = new CopyOnWriteArrayList<PerfectSquare>();
        var latch = new CountDownLatch(k);
        int chunkSize = array.length / k;

        List<Thread> threads = new ArrayList<>();

        for (int t = 0; t < k; t++) {
            final int threadId = t + 1;
            final int start = t * chunkSize;
            final int end = (t == k - 1) ? array.length : (t + 1) * chunkSize;

            Thread thread = new Thread(() -> {
                for (int i = start; i < end; i++) {
                    if (isPerfectSquare(array[i])) {
                        result.add(new PerfectSquare(i, array[i]));
                        synchronized (lock) {
                            System.out.println("Luong " + threadId + ": Vi tri " + i + ": " + array[i] + " la so chinh phuong");
                            lock.notifyAll();
                            try {
                                lock.wait(10);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                }
                latch.countDown();
            });
            threads.add(thread);
        }

        long startTime = System.nanoTime();

        for (Thread t : threads) {
            t.start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long endTime = System.nanoTime();
        System.out.println("Thoi gian thuc thi: " + (endTime - startTime) / 1_000_000.0 + " ms");

        return result;
    }

    private record PerfectSquare(int index, int value) {
    }
}

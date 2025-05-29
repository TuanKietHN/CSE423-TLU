package Lab2.Dekker;
import java.util.Scanner;

public class Main {
    private static int max = Integer.MIN_VALUE;
    private static int min = Integer.MAX_VALUE;
    private static int currentIndex = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("input =\n");
            int n = scanner.nextInt();
            int[] A = new int[n];
            for (int i = 0; i < n; i++) {
                A[i] = scanner.nextInt();
            }


            int k = 2;

            Lock lock;
            if (k == 2) {
                lock = new Dekker();
            } else {
                lock = new Bakery(k);
            }
           
            Thread[] threads = new Thread[k];

            for (int i = 0; i < 2; i++) {
                int id = i;
                Runnable task = () -> {
                    while (true) {
                        int iValue;

                        synchronized (Main.class) {
                            if (currentIndex >= A.length) break;
                            iValue = currentIndex;
                            currentIndex++;
                        }

                        lock.requestCS(id);
                        try {
                            if (A[iValue] > max) {
                                max = A[iValue];
                            }
                            if (A[iValue] < min) {
                                min = A[iValue];
                            }
                        } finally {
                            lock.releaseCS(id);
                        }
                    }
                };
                threads[i] = new Thread(task);
                threads[i].start();
            }

            for (Thread t : threads) {
                t.join();
            }

            System.out.println("\noutput=\n");
            System.out.println("Gia tri lon nhat: " + max);
            System.out.println("Gia tri nho nhat: " + min);

        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}

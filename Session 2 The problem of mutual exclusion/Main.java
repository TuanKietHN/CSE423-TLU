// Main.java
public class Main {
    private static int x = 0;
    private static final int N = 10000;

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new Peterson();

        Thread t0 = new Thread(new Worker(0, lock));
        Thread t1 = new Thread(new Worker(1, lock));

        t0.start();
        t1.start();

        t0.join();
        t1.join();

        System.out.println("Final value of x: " + x);
    }

    static class Worker implements Runnable {
        private final int id;
        private final Lock lock;

        public Worker(int id, Lock lock) {
            this.id = id;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < N; i++) {
                lock.lock(id);
                x = x + 1;  // Critical section
                lock.unlock(id);
            }
        }
    }
}

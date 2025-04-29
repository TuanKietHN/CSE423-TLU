public class MutexDemo {
    public static int sharedValue = 0;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Without Mutex (Race Condition Demo) ===");
        runThreads(new NoMutex());

        System.out.println("\n=== Mutex with Single State Variable ===");
        runThreads(new MutexWithOneFlag());

        System.out.println("\n=== Mutex with Two State Variables ===");
        runThreads(new MutexWithTwoFlags());

        System.out.println("\n=== Mutex with Array ===");
        runThreads(new MutexWithArray());

        System.out.println("\n=== Mutex with Combined Approach ===");
        runThreads(new MutexWithCombination());
    }

    private static void runThreads(Mutex mutex) throws InterruptedException {
        sharedValue = 0;

        Thread t1 = new Thread(() -> {
            for (int i = 0; i <= 5000; i++) {
                mutex.enter(0);
                sharedValue++;
                mutex.exit(0);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i <= 5000; i++) {
                mutex.enter(1);
                sharedValue++;
                mutex.exit(1);
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Final sharedValue: " + sharedValue);  // Expected: 1002
    }
}

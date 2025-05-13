import java.util.concurrent.Semaphore;
public class DiningPhilosophersSemaphore {
    public static void main(String[] args) {
        int n = 5;
        Semaphore[] forks = new Semaphore[n];
        for (int i = 0; i < n; i++) forks[i] = new Semaphore(1);

        for (int i = 0; i < n; i++) {
            new PhilosopherSemaphore(i, forks[i], forks[(i + 1) % n]).start();
        }
    }
}

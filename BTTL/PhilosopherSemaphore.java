import java.util.concurrent.Semaphore;

class PhilosopherSemaphore extends Thread {
    private final int id;
    private final Semaphore leftFork;
    private final Semaphore rightFork;

    public PhilosopherSemaphore(int id, Semaphore left, Semaphore right) {
        this.id = id;
        this.leftFork = left;
        this.rightFork = right;
    }

    public void run() {
        try {
            while (true) {
                System.out.println("Triet gia " + id + " dang suy nghi...");
                Thread.sleep((long) (Math.random() * 1000));

                System.out.println("Triet gia " + id + " muon an.");
                leftFork.acquire();
                System.out.println("Triet gia " + id + " lay dia ben trai .");
                rightFork.acquire();
                System.out.println("Triet gia " + id + " lay dia ben phai. => BAT DAU AN.");

                Thread.sleep((long) (Math.random() * 1000));

                System.out.println("Triet gia " + id + " ket thuc an.");
                leftFork.release();
                rightFork.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


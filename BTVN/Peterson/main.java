package BTVN.Peterson;

public class main {

    static int x = 0;
    static final int N = 100000;
    static lock Lock = new Peterson();

    public static void main(String[] args) throws InterruptedException {
        Thread t0 = new Thread(new MyThread(0));
        Thread t1 = new Thread(new MyThread(1));

        t0.start();
        t1.start();

        t0.join();
        t1.join();

        System.out.println("Tong bien x: " + x);
    }

    static class MyThread implements Runnable {
        int id;

        public MyThread(int id) {
            this.id = id;
        }

        public void run() {
            for (int i = 0; i < N; i++) {
                Lock.requestCS(id);
                x = x + 1; 
                Lock.releaseCS(id);
            }
        }
    }
}


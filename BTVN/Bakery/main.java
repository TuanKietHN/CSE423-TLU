package BTVN.Bakery;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class main {

    static int x = 0;
    static final int N = 10000;
    static lock Lock = new Bakery();

    public static void main(String[] args) throws InterruptedException {
        Thread t0 = new Thread(new Worker(0));
        Thread t1 = new Thread(new Worker(1));

        t0.start();
        t1.start();

        t0.join();
        t1.join();

        System.out.println("Final value of x: " + x);
    }

    static class Worker implements Runnable {
        int pid;

        public Worker(int pid) {
            this.pid = pid;
        }

        public void run() {
            for (int i = 0; i < N; i++) {
                Lock.requestCS(pid);
                x = x + 1; 
                Lock.releaseCS(pid);
            }
        }
    }
}

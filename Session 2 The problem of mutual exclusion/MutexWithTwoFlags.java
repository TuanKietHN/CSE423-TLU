public class MutexWithTwoFlags implements Mutex {
    private volatile boolean[] flag = new boolean[2];
    private volatile int turn;

    public void enter(int threadId) {
        int other = 1 - threadId;
        flag[threadId] = true;
        turn = other;

        while (flag[other] && turn == other) {
            // busy wait
        }
    }

    public void exit(int threadId) {
        flag[threadId] = false;
    }
}

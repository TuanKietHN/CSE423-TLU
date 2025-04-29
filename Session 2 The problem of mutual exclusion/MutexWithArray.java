public class MutexWithArray implements Mutex {
    private volatile int[] state = new int[2]; // 0: outside, 1: trying, 2: in critical section
    private volatile int turn;

    public void enter(int threadId) {
        int other = 1 - threadId;
        state[threadId] = 1;
        turn = other;

        while (state[other] != 0 && turn == other) {
            // wait until other is not interested or it's our turn
        }

        state[threadId] = 2;
    }

    public void exit(int threadId) {
        state[threadId] = 0;
    }
}

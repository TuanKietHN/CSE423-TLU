public class MutexWithCombination implements Mutex {
    private volatile boolean[] flag = new boolean[2];
    private volatile int[] state = new int[2];
    private volatile int turn;

    public void enter(int threadId) {
        int other = 1 - threadId;
        flag[threadId] = true;
        state[threadId] = 1; // trying
        turn = other;

        // Chờ nếu luồng kia đang muốn vào và được ưu tiên hơn
        while (flag[other] && turn == other) {
            // busy wait
        }

        state[threadId] = 2; // vào vùng găng
    }

    public void exit(int threadId) {
        flag[threadId] = false;
        state[threadId] = 0;
    }
}

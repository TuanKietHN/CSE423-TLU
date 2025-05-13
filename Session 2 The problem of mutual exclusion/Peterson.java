// Peterson.java
public class Peterson implements Lock {
    private volatile boolean[] flag = new boolean[2];
    private volatile int turn;

    @Override
    public void lock(int id) {
        int other = 1 - id;
        flag[id] = true;
        turn = other;
        while (flag[other] && turn == other) {
            // Busy wait
        }
    }

    @Override
    public void unlock(int id) {
        flag[id] = false;
    }
}

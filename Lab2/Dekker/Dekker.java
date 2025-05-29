package Lab2.Dekker;

public class Dekker implements Lock {
    private volatile boolean[] flag = {false, false};
    private volatile int turn = 0;

    @Override
    public void requestCS(int i) {
        int j = 1 - i;
        flag[i] = true;
        while (flag[j]) {
            if (turn == i) {
                Thread.yield(); 
                continue;
            }
            flag[i] = false;
            while (turn == j) Thread.yield();
            flag[i] = true;
        }
    }

    @Override
    public void releaseCS(int i) {
        int j = 1 - i;
        turn = j;
        flag[i] = false;
    }
}


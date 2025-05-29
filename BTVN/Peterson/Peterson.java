package BTVN.Peterson;

public class Peterson implements lock {
    private volatile boolean[] flag = new boolean[2];
    private volatile int victim;

    public Peterson() {
        flag[0] = false;
        flag[1] = false;
        victim = 0;
    }

    public void requestCS(int i) {
        int j = 1 - i;
        flag[i] = true;
        victim = i;
        while (flag[j] && victim == i) {}
    }

    public void releaseCS(int i) {
        flag[i] = false;
    }
}


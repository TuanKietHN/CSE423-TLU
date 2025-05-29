package Lab2.Dekker;

public class Bakery implements Lock {
    private final int n;
    private final boolean[] choosing;
    private final int[] number;

    public Bakery(int n) {
        this.n = n;
        choosing = new boolean[n];
        number = new int[n];
    }

    @Override
public void requestCS(int id) {
    choosing[id] = true;

    int max = 0;
    for (int i = 0; i < n; i++) {
        if (number[i] > max) {
            max = number[i];
        }
    }
    number[id] = max + 1;

    choosing[id] = false;

    for (int j = 0; j < n; j++) {
        if (j == id) continue;
        while (choosing[j]) Thread.yield();
        while (number[j] != 0 && (number[j] < number[id] || (number[j] == number[id] && j < id)))
            Thread.yield();
    }
}


    @Override
    public void releaseCS(int id) {
        number[id] = 0;
    }
}


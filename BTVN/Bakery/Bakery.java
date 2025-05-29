package BTVN.Bakery;

public class Bakery implements lock {
    private volatile boolean[] choosing = new boolean[2];
    private volatile int[] number = new int[2];

    public void requestCS(int pid) {
        choosing[pid] = true;
        number[pid] = 1 + Math.max(number[0], number[1]);
        choosing[pid] = false;

        for (int j = 0; j < 2; j++) {
            if (j == pid) continue;
            // đợi nếu j được chọn
            while (choosing[j]);
            // đợi nếu có tiến trình khác có ưu tiên cao hơn
            while (number[j] != 0 &&
                  (number[j] < number[pid] ||
                  (number[j] == number[pid] && j < pid))) {

            }
        }
    }

    public void releaseCS(int pid) {
        number[pid] = 0;
    }
}

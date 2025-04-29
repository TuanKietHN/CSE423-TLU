public class MutexWithOneFlag implements Mutex {
    private volatile boolean flag = false;

    public void enter(int threadId) {
        while (flag) {
            // busy wait
        }
        flag = true;
    }

    public void exit(int threadId) {
        flag = false;
    }
}

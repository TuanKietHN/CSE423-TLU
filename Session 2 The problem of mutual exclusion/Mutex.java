public interface Mutex {
    void enter(int threadId);
    void exit(int threadId);
}

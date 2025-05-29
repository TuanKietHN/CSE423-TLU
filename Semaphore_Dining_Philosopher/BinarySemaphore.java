package Semaphore_Dining_Philosopher;

class BinarySemaphore {
    private boolean value;

    public BinarySemaphore(boolean initValue) {
        this.value = initValue;
    }

    public synchronized void P() {
        while (!value) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        value = false;
    }

    public synchronized void V() {
        value = true;
        notify();
    }
}

class ReadWriteMonitor {
    private int readers = 0;
    private boolean isWriting = false;

    public synchronized void startRead(String name) throws InterruptedException {
        while (isWriting) wait();
        readers++;
        System.out.println(name + " bat dau doc. [Dang doc: " + readers + "]");
    }

    public synchronized void endRead(String name) {
        readers--;
        System.out.println(name + " ket thuc doc. [Con lai: " + readers + "]");
        if (readers == 0) notifyAll();
    }

    public synchronized void startWrite(String name) throws InterruptedException {
        while (isWriting || readers > 0) wait();
        isWriting = true;
        System.out.println(name + " bat dau ghi.");
    }

    public synchronized void endWrite(String name) {
        isWriting = false;
        System.out.println(name + " ket thuc ghi.");
        notifyAll();
    }
}


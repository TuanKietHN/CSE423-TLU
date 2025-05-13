class Buffer {
    private int data;
    private boolean hasData = false;

    public synchronized void produce(String name, int value) throws InterruptedException {
        while (hasData) wait();
        data = value;
        hasData = true;
        System.out.println(name + " san xuat: " + data);
        notifyAll();
    }

    public synchronized int consume(String name) throws InterruptedException {
        while (!hasData) wait();
        hasData = false;
        System.out.println(name + " tieu thu: " + data);
        notifyAll();
        return data;
    }
}

public class ReaderWriterMonitorExample {
    public static void main(String[] args) {
        ReadWriteMonitor monitor = new ReadWriteMonitor();

        Runnable reader = () -> {
            String name = Thread.currentThread().getName();
            try {
                monitor.startRead(name);
                Thread.sleep(1000); // Đọc xong
                monitor.endRead(name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable writer = () -> {
            String name = Thread.currentThread().getName();
            try {
                monitor.startWrite(name);
                Thread.sleep(1500); // Ghi xong
                monitor.endWrite(name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        new Thread(reader, "Reader-1").start();
        new Thread(reader, "Reader-2").start();
        new Thread(writer, "Writer-1").start();
        new Thread(reader, "Reader-3").start();
    }
}

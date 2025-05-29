package Semaphore.Reader_and_Writer;

public class Main {
    public static void main(String[] args) {
        final ReaderWriter rw = new ReaderWriter();
        final int NUM_READERS = 5;
        final int NUM_WRITERS = 1;


        for (int i = 1; i <= NUM_READERS; i++) {
            final int readerId = i;
            new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    try {
                        Thread.sleep((long)(Math.random() * 1000));
                        rw.startReading(readerId);
                        System.out.println("Reader " + readerId + " is reading...");
                        Thread.sleep((long)(Math.random() * 200));
                        rw.endReading(readerId);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }

        for (int i = 1; i <= NUM_WRITERS; i++) {
            final int writerId = i;
            Runnable writerTask = () -> {
                for (int j = 0; j < 2; j++) {
                    try {
                        Thread.sleep((long)(Math.random() * 1500));
                        rw.startWriting(writerId);
                        System.out.println("Writer " + writerId + " is writing...");
                        Thread.sleep((long)(Math.random() * 2000));
                        rw.endWriting(writerId);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            };
            new Thread(writerTask).start();
        }
    }
}

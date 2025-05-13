public class ProducerConsumerMonitor {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();

        Runnable producer = () -> {
            String name = Thread.currentThread().getName();
            int value = 1;
            try {
                while (true) {
                    buffer.produce(name, value++);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Runnable consumer = () -> {
            String name = Thread.currentThread().getName();
            try {
                while (true) {
                    buffer.consume(name);
                    Thread.sleep(1200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        new Thread(producer, "Producer").start();
        new Thread(consumer, "Consumer").start();
    }

}

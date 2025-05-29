package Lab3.Monitor;

/**
 * Chương trình chính sử dụng Monitor để đồng bộ hóa giữa hai luồng
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== CHƯƠNG TRÌNH MONITOR DEMO ===");
        System.out.println("T1: Sinh số ngẫu nhiên từ 10-200");
        System.out.println("T2: Kiểm tra số nguyên tố và số chính phương");
        System.out.println("=====================================\n");

        // Khởi tạo monitor với buffer dung lượng 10
        BoundedBufferMonitorLinkedList monitor = new BoundedBufferMonitorLinkedList(10);

        // Tạo và khởi động các luồng
        Thread t1 = monitor.createProducer();
        Thread t2 = monitor.createConsumer();

        t1.start();
        t2.start();

        // Chạy trong 15 giây rồi dừng
        try {
            Thread.sleep(15000);
            monitor.stop();

            // Chờ các luồng kết thúc
            t1.join();
            t2.join();

            System.out.println("\n=== CHƯƠNG TRÌNH KẾT THÚC ===");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread bị interrupt: " + e.getMessage());
        }
    }
}
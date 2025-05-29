package Lab3.Semaphore;

/**
 * BinarySemaphore - Semaphore nhị phân chỉ có thể có giá trị 0 hoặc 1
 * Được sử dụng để đồng bộ hóa giữa hai luồng
 */
public class BinarySemaphore {
    private boolean available = false;
    
    /**
     * Phương thức acquire - Chờ cho đến khi semaphore available
     */
    public synchronized void acquire() throws InterruptedException {
        while (!available) {
            wait(); // Chờ cho đến khi có signal
        }
        available = false; // Đặt lại về false sau khi acquire
    }
    
    /**
     * Phương thức release - Giải phóng semaphore và thông báo cho các luồng đang chờ
     */
    public synchronized void release() {
        available = true;
        notify(); // Thông báo cho luồng đang chờ
    }
    
    /**
     * Kiểm tra trạng thái của semaphore
     */
    public synchronized boolean isAvailable() {
        return available;
    }
}

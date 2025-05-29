package Lab3.Semaphore;

/**
 * CountingSemaphore - Semaphore đếm có thể có nhiều permits
 * Được sử dụng để kiểm soát số lượng luồng có thể truy cập tài nguyên cùng lúc
 */
public class CountingSemaphore {
    private int permits;
    private final int maxPermits;
    
    /**
     * Constructor khởi tạo với số permits ban đầu
     */
    public CountingSemaphore(int initialPermits) {
        this.permits = initialPermits;
        this.maxPermits = initialPermits;
    }
    
    /**
     * Phương thức acquire - Lấy một permit, chờ nếu không có permit nào available
     */
    public synchronized void acquire() throws InterruptedException {
        while (permits == 0) {
            wait(); // Chờ cho đến khi có permit available
        }
        permits--; // Giảm số permits
    }
    
    /**
     * Phương thức tryAcquire - Thử lấy permit mà không chờ
     */
    public synchronized boolean tryAcquire() {
        if (permits > 0) {
            permits--;
            return true;
        }
        return false;
    }
    
    /**
     * Phương thức release - Giải phóng một permit
     */
    public synchronized void release() {
        if (permits < maxPermits) {
            permits++;
            notify(); // Thông báo cho luồng đang chờ
        }
    }
    
    /**
     * Lấy số permits hiện tại
     */
    public synchronized int availablePermits() {
        return permits;
    }
}

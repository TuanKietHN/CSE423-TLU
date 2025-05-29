package Lab3.Monitor;

import java.util.LinkedList;
import java.util.Queue;

/**
 * BoundedBuffer - Buffer giới hạn để lưu trữ số nguyên, sử dụng cơ chế Monitor
 */
public class BoundedBufferQueue {
    private final Queue<Integer> buffer;
    private final int capacity;

    /**
     * Constructor khởi tạo buffer với dung lượng cho trước
     */
    public BoundedBufferQueue(int capacity) {
        this.buffer = new LinkedList<>();
        this.capacity = capacity;
    }

    /**
     * Thêm số vào buffer, chờ nếu buffer đầy
     */
    public synchronized void put(int number) throws InterruptedException {
        while (buffer.size() >= capacity) {
            wait(); // Chờ nếu buffer đầy
        }
        buffer.offer(number);
        notify(); // Thông báo cho luồng Consumer
    }

    /**
     * Lấy số từ buffer, chờ nếu buffer rỗng
     */
    public synchronized Integer take() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait(); // Chờ nếu buffer rỗng
        }
        Integer number = buffer.poll();
        notify(); // Thông báo cho luồng Producer
        return number;
    }

    /**
     * Lấy kích thước hiện tại của buffer
     */
    public synchronized int size() {
        return buffer.size();
    }
}
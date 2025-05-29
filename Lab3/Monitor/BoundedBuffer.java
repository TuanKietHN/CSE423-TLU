package Lab3.Monitor;

/**
 * BoundedBuffer - Buffer giới hạn sử dụng mảng double[] với cơ chế Monitor
 */
public class BoundedBuffer {
    private final double[] buffer;
    private final int capacity;
    private int head; // Chỉ số đầu buffer
    private int tail; // Chỉ số cuối buffer
    private int count; // Số phần tử hiện tại trong buffer

    /**
     * Constructor khởi tạo buffer với dung lượng cho trước
     */
    public BoundedBuffer(int capacity) {
        this.buffer = new double[capacity];
        this.capacity = capacity;
        this.head = 0;
        this.tail = 0;
        this.count = 0;
    }

    /**
     * Thêm số vào buffer, chờ nếu buffer đầy
     */
    public synchronized void put(double number) throws InterruptedException {
        while (count >= capacity) {
            wait(); // Chờ nếu buffer đầy
        }
        buffer[tail] = number;
        tail = (tail + 1) % capacity; // Tăng tail theo kiểu vòng
        count++;
        notify(); // Thông báo cho luồng Consumer
    }

    /**
     * Lấy số từ buffer, chờ nếu buffer rỗng
     */
    public synchronized Double take() throws InterruptedException {
        while (count == 0) {
            wait(); // Chờ nếu buffer rỗng
        }
        double number = buffer[head];
        head = (head + 1) % capacity; // Tăng head theo kiểu vòng
        count--;
        notify(); // Thông báo cho luồng Producer
        return number;
    }

    /**
     * Lấy kích thước hiện tại của buffer
     */
    public synchronized int size() {
        return count;
    }
}

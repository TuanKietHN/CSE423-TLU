package Lab4.UDP;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class DatagramClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final int NUM_COUNT = 10;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(5000); 

            while (true) {
                Random rand = new Random();
                StringBuilder numbers = new StringBuilder();
                for (int i = 0; i < NUM_COUNT; i++) {
                    numbers.append(rand.nextInt(100) + 1).append(" ");
                }
                String message = numbers.toString().trim();
                System.out.println("Sending to server: " + message);
                byte[] sendData = message.getBytes();
                InetAddress serverAddress = InetAddress.getByName(SERVER_ADDRESS);
                DatagramPacket sendPacket = new DatagramPacket(
                    sendData, sendData.length, serverAddress, SERVER_PORT);
                socket.send(sendPacket);

                // Nhận kết quả từ server
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                try {
                    socket.receive(receivePacket);
                    String sortedNumbers = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    System.out.println("Received from server: " + sortedNumbers);
                } catch (SocketTimeoutException e) {
                    System.out.println("No response from server");
                }

                // Nghỉ 2 giây trước khi gửi tiếp
                Thread.sleep(2000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

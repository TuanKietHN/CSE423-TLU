package Lab4.UDP;

import java.net.*;
import java.util.*;

public class client2 {
    static final int PORT = 8080;
    static final int LEN = 1024;

    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            while (true) {
                StringBuilder data = new StringBuilder();
                Random rand = new Random();
                for (int i = 0; i < 10; i++) data.append(rand.nextInt(100) + 1).append(" ");
                String message = data.toString();
                System.out.println("Sending: " + message);
                byte[] sendData = message.getBytes();
                DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, PORT);
                socket.send(packet);
                byte[] buffer = new byte[LEN];
                DatagramPacket receivePacket = new DatagramPacket(buffer, LEN);
                socket.receive(receivePacket);
                System.out.println("Received: " + new String(receivePacket.getData(), 0, receivePacket.getLength()));
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) socket.close();
        }
    }
}

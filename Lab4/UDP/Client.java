package Lab4.UDP;

import java.net.*;
import java.util.*;

public class Client {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        int port=8080;
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
                DatagramPacket packet = new DatagramPacket(sendData, sendData.length, address, port);
                socket.send(packet);
                byte[] buffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivePacket);
                System.out.println("Received: " + new String(receivePacket.getData(), 0, receivePacket.getLength()));
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            System.err.println("Client error: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}

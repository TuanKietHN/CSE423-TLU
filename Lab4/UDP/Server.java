package Lab4.UDP;

import java.net.*;
import java.util.*;

public class Server {
    public static void main(String[] args) {
        DatagramSocket socket = null;
        int port=8080;
        try {
            socket = new DatagramSocket(port);
            System.out.println("Server running on port: "+port);
            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                DatagramSocket finalSocket = socket; 
                new Thread(() -> handleClient(packet, finalSocket)).start();
            }
        } catch (SocketException e) {
            System.err.println("Cannot connect to port "+port+" : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    static void handleClient(DatagramPacket packet, DatagramSocket socket) {
        try {
            String input = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received: " + input);
            String[] nums = input.split(" ");
            ArrayList<Integer> even = new ArrayList<>();
            ArrayList<Integer> odd = new ArrayList<>();
            for (String s : nums) {
                try {
                    int n = Integer.parseInt(s);
                    if (n % 2 == 0) even.add(n);
                    else odd.add(n);
                } catch (NumberFormatException e) {
                    // Bỏ qua nếu không phải số
                }
            }
            Collections.sort(even);
            Collections.sort(odd);
            StringBuilder result = new StringBuilder();
            int i = 0, j = 0;
            while (i < even.size() || j < odd.size()) {
                if (i < even.size()) result.append(even.get(i++)).append(" ");
                if (j < odd.size()) result.append(odd.get(j++)).append(" ");
            }
            System.out.println("Sending: " + result);
            byte[] sendData = result.toString().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
            socket.send(sendPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

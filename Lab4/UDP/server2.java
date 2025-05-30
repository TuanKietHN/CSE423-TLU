package Lab4.UDP;

import java.net.*;
import java.util.*;

public class server2 {
    static final int PORT = 8080;
    static final int LEN = 1024;

    public static void main(String[] args) {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(PORT);
            System.out.println("Server running on port " + PORT);
            byte[] buffer = new byte[LEN];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, LEN);
                socket.receive(packet);
                DatagramSocket finalSocket = socket;
                new Thread(() -> handleClient(packet, finalSocket)).start();
            }
        } catch (SocketException e) {
            System.err.println("Cannot bind to port " + PORT + ": " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) socket.close();
        }
    }

    static void handleClient(DatagramPacket packet, DatagramSocket socket) {
        try {
            String input = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received: " + input);
            StringTokenizer st = new StringTokenizer(input);
            ArrayList<Integer> even = new ArrayList<>();
            ArrayList<Integer> odd = new ArrayList<>();
            while (st.hasMoreTokens()) {
                int n = Integer.parseInt(st.nextToken());
                if (n % 2 == 0) even.add(n);
                else odd.add(n);
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

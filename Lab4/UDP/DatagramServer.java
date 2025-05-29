package Lab4.UDP;

import java.net.*;
import java.util.*;
import java.io.*;

public class DatagramServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                Thread clientThread = new Thread(() -> handleClient(packet, socket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(DatagramPacket packet, DatagramSocket socket) {
        try {
            String received = new String(packet.getData(), 0, packet.getLength()).trim();
            System.out.println("Received from client: " + received);
            String[] numbersStr = received.split("\\s+");
            List<Integer> numbers = new ArrayList<>();
            for (String num : numbersStr) {
                try {
                    numbers.add(Integer.parseInt(num));
                } catch (NumberFormatException e) {
                }
            }
            List<Integer> even = new ArrayList<>();
            List<Integer> odd = new ArrayList<>();
            for (int num : numbers) {
                if (num % 2 == 0) even.add(num);
                else odd.add(num);
            }
            Collections.sort(even);
            Collections.sort(odd);

            List<Integer> result = new ArrayList<>();
            int i = 0, j = 0;
            while (i < even.size() || j < odd.size()) {
                if (i < even.size()) result.add(even.get(i++));
                if (j < odd.size()) result.add(odd.get(j++));
            }
            StringBuilder resultStr = new StringBuilder();
            for (int num : result) {
                resultStr.append(num).append(" ");
            }
            System.out.println("Sending to client: " + resultStr.toString().trim());
            byte[] sendData = resultStr.toString().getBytes();
            DatagramPacket sendPacket = new DatagramPacket(
                sendData, sendData.length, packet.getAddress(), packet.getPort());
            socket.send(sendPacket);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
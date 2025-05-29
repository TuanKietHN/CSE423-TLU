package Lab4.TCP;

import java.net.*;
import java.util.*;
import java.io.*;

public class TCPServer {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Port of Server " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client connected:: " + clientSocket.getInetAddress().getHostAddress());
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String received = in.readLine();
            if (received == null) {
                System.out.println("Client disconnected");
                return;
            }
            System.out.println("Data from client: " + received);
            String[] numbersStr = received.trim().split("\\s+");
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
            out.println(resultStr.toString().trim());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

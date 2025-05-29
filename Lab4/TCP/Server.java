package Lab4.TCP;

import java.net.*;
import java.util.*;
import java.io.*;

public class Server {
    public static void main(String[] args) {
        ServerSocket server = null;
        int port=8080;
        try {
            server = new ServerSocket(port);
            System.out.println("Server running on port: "+port);
            while (true) {
                Socket client = server.accept();
                new Thread(() -> handleClient(client)).start();
            }
        } catch (SocketException e) {
            System.err.println("Cannot bind to port"+port+" : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) try { server.close(); } catch (Exception e) {}
        }
    }

    static void handleClient(Socket client) {
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            String input = in.readUTF();
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
            out.writeUTF(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (in != null) in.close(); } catch (Exception e) {}
            try { if (out != null) out.close(); } catch (Exception e) {}
            try { client.close(); } catch (Exception e) {}
        }
    }
}
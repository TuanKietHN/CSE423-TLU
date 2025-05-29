package Lab4.TCP;

import java.net.*;
import java.util.*;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        while (true) {
            Socket socket = null;
            DataOutputStream out = null;
            DataInputStream in = null;
            int port=8080;
            try {
                socket = new Socket("localhost", port);
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                StringBuilder data = new StringBuilder();
                Random rand = new Random();
                for (int i = 0; i < 10; i++) data.append(rand.nextInt(100) + 1).append(" ");
                String message = data.toString();
                System.out.println("Sending: " + message);
                out.writeUTF(message);
                String result = in.readUTF();
                System.out.println("Received: " + result);
                Thread.sleep(2000);
            } catch (Exception e) {
                System.err.println("Client error: " + e.getMessage());
            } finally {
                try { if (in != null) in.close(); } catch (Exception e) {}
                try { if (out != null) out.close(); } catch (Exception e) {}
                try { if (socket != null) socket.close(); } catch (Exception e) {}
            }
        }
    }
}
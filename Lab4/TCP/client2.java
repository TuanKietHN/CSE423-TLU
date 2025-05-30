package Lab4.TCP;

import java.net.*;
import java.util.*;
import java.io.*;

public class client2 {
    static final int PORT = 12345;
    static final int LEN = 1024;

    public static void main(String[] args) {
        while (true) {
            Socket socket = null;
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                socket = new Socket("localhost", PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                StringBuilder data = new StringBuilder();
                Random rand = new Random();
                for (int i = 0; i < 10; i++) data.append(rand.nextInt(100) + 1).append(" ");
                String message = data.toString();
                System.out.println("Sending: " + message);
                out.println(message);
                String result = in.readLine();
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

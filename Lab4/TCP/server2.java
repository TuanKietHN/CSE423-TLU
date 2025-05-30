package Lab4.TCP;
import java.net.*;
import java.util.*;
import java.io.*;

public class server2 {
    static final int PORT = 12345;
    static final int LEN = 1024;

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT);
            System.out.println("Server running on port " + PORT);
            while (true) {
                Socket client = server.accept();
                new Thread(() -> handleClient(client)).start();
            }
        } catch (SocketException e) {
            System.err.println("Cannot bind to port " + PORT + ": " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (server != null) try { server.close(); } catch (Exception e) {}
        }
    }

    static void handleClient(Socket client) {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            String input = in.readLine();
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
            out.println(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (in != null) in.close(); } catch (Exception e) {}
            try { if (out != null) out.close(); } catch (Exception e) {}
            try { client.close(); } catch (Exception e) {}
        }
    }
}
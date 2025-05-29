package Lab4.TCP;

import java.net.*;
import java.util.*;
import java.io.*;

public class TCPClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8080;
    private static final int NUM_COUNT = 10;

    public static void main(String[] args) {
        while (true) {
            try (
                Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                Random rand = new Random();
                StringBuilder numbers = new StringBuilder();
                for (int i = 0; i < NUM_COUNT; i++) {
                    numbers.append(rand.nextInt(100) + 1).append(" ");
                }
                String message = numbers.toString().trim();
                System.out.println("Sending to server: " + message);
                out.println(message);

                String sortedNumbers = in.readLine();
                System.out.println("Received from server: " + sortedNumbers);
                Thread.sleep(2000);

            } catch (IOException e) {
                System.out.println("Connection error: " + e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

package deprecated;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ServerTest {
    public static void Server(int port) {
        try {
            // starts server, checks for and accepts client
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started - waiting for client...");
            Socket server = serverSocket.accept();
            System.out.println("Client connected.");

            // get and print input from client
            DataInputStream fromClient = new DataInputStream(server.getInputStream());
            String clientIn = fromClient.readUTF();
            System.out.println(clientIn);

            // send response to client
            DataOutputStream toClient = new DataOutputStream(server.getOutputStream());
            toClient.writeUTF("Server recieved:\n" + clientIn + "\nConnection to " + server.getLocalAddress()
                    + " on port " + server.getLocalPort() + " complete.");

            // close connection
            server.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // input for port
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter port:");
        int port = scanner.nextInt();
        scanner.close();

        // call client
        Server(port);
    }
}

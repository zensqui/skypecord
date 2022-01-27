package deprecated.demo;
import java.net.*;
import java.io.*;

public class ServerDemo {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(5050);
        System.out.println("Server started - waiting for client connection..");
        Socket socket = server.accept();
        System.out.println("Connection established.");

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        while (in.available() >= 0) {
            char c = (char)in.read();
            out.write((byte)c);
        }

        server.close();
    }
}

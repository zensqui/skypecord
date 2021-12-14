import java.net.*;
import java.io.*;

public class server {
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(5050);
        System.out.println("Server started - waiting for client connection..");
        Socket server = socket.accept();
        System.out.println("Connection established.");

        DataInputStream in = new DataInputStream(
            new BufferedInputStream(server.getInputStream())
        );

        while (in.available() >= 0) {
            char c = (char)in.read();
            System.out.print(c);
        }
    }
}

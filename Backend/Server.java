import java.net.*;
import java.io.*;

public class Server {
    static final int port = 5050;
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server listening on port " + port + ".");

        Socket socket = null;
        while (true) {
            socket = server.accept();
        }
    }
}

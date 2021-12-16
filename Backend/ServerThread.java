import java.net.*;
import java.io.*;

public class ServerThread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void run() throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        while (in.available() >= 0) {
            char c = (char)in.read();
            out.write((byte)c);
        }
    }
}

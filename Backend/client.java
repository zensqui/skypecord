import java.net.*;
import java.io.*;

public class client {
    public static void main(String[] args) throws IOException {
        Socket client = new Socket("127.0.0.1", 5050);
        System.out.println("Connected to server.");

        DataInputStream in = new DataInputStream(System.in);
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        while (in.available() >= 0) {
            char c = (char)in.read();
            out.write((byte)c);
        }
    }    
}

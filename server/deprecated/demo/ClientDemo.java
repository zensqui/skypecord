package deprecated.demo;
import java.net.*;
import java.io.*;

public class ClientDemo {
    public static void main(String[] args) throws IOException {
        Socket client = new Socket("sc.zenithproject.xyz", 5050);
        System.out.println("Connected to server.");

        DataInputStream stdIn = new DataInputStream(System.in);
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        while (stdIn.available() >= 0) {
            char c = (char)stdIn.read();
            out.write((byte)c);
            if (in.available() >= 0) {
                c = (char)in.read();
                System.out.print(c);
            }
        }

        client.close();
    }    
}

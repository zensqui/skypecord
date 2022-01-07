package Depreciated;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClientTest {
    public static void Client(String ip, int port, String UserIn) {
        try {
            //connect to server
            Socket client = new Socket(ip, port);
            System.out.println("Connected to " + ip + " on port " + port + ".");
            
            //sending input to server
            DataOutputStream toServer = new DataOutputStream(client.getOutputStream());
            toServer.writeUTF(UserIn);

            //get and print response from server
            DataInputStream fromServer = new DataInputStream(client.getInputStream());
            System.out.println(fromServer.readUTF());

            //close connection
            client.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String [] args) {
        //input for ip and port
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter(System.lineSeparator());
        System.out.println("Enter target IP:");
        String ip = scanner.next();
        System.out.println("Enter target port:");
        int port = scanner.nextInt();
        System.out.println("Enter input to server:");
        String userIn = scanner.next();
        scanner.close();

        //call client
        Client(ip, port, userIn);
    }
}
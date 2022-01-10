package deprecated;
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClientTest1 {
    public static void Client(String ip, int port) {
        try {
            //connect to server
            Socket client = new Socket(ip, port);
            System.out.println("Connected to " + ip + " on port " + port + ".");
            
            //input from terminal
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            //input to server
            DataOutputStream toServer = new DataOutputStream(client.getOutputStream());

            //get response from server
            DataInputStream fromServer = new DataInputStream(client.getInputStream());

            //sends output to server
            String line = "";
            while (!line.equals("CloseConnection")) {
                try { 
                    line = input.readLine();
                    toServer.writeUTF(line); 
                } 
                catch(IOException e) { 
                    e.printStackTrace();
                } 
            }

            
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
        scanner.close();

        //call client
        Client(ip, port);
    }
}
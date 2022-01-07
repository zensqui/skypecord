import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class ClientTest2 {
    public static void main(String[] args) throws IOException, ParseException {
        Socket client = new Socket("sc.zenithproject.xyz", 5050);

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));;
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8);

        JSONObject jsonLogin = new JSONObject();
        jsonLogin.put("type", "login");
        System.out.println("User:");
        String user = stdIn.readLine();
        jsonLogin.put("user", user);
        out.append(jsonLogin.toJSONString() + "\n");
        out.flush();

        String input = "";
        while (true) {
            //change to while loop for this, stdin is blocking and only for testing
            if ((input = in.readLine()) != null) {
                JSONObject jsonIn = (JSONObject)new JSONParser().parse(input);
                System.out.println(jsonIn.toJSONString());
            } 
            if ((input = stdIn.readLine()) != null) {
                JSONObject jsonOut = new JSONObject();
                System.out.println("Type:");
                jsonOut.put("type", input);
                jsonOut.put("user", user);
                System.out.println("Target:");
                jsonOut.put("target", stdIn.readLine());
                System.out.println("Data:");
                jsonOut.put("data", stdIn.readLine());
                out.append(jsonOut.toJSONString() + "\n");
                out.flush();
            }
        }
        /*while ((input = in.readLine()) != null) {
            JSONObject jsonIn = (JSONObject)new JSONParser().parse(input);
            System.out.println(jsonIn.toJSONString());
        }*/
    }    
}

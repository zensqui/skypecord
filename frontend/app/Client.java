import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Client {
    public static void main(String[] args) throws IOException, ParseException {
        Socket client = new Socket("sc.zenithproject.xyz", 5050);

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8);

        String input = "";
        while ((input = in.readLine()) != null) {
            JSONObject json = new JSONObject();
        }
        while (true) {
            
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

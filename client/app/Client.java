import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;
public class Client {
    private Socket client;
    private BufferedReader in;
    private OutputStreamWriter out;

    public Client() throws IOException {
        client = new Socket("sc.zenithproject.xyz", 5050);
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8);
    }

    public void login(String user, String pass) throws IOException, ParseException {
        JSONObject json = new JSONObject();
        json.put("type", "login");
        json.put("user", user);
        json.put("pass", pass);
        out.append(json.toJSONString() + "\n");
        out.flush();
    }  
}

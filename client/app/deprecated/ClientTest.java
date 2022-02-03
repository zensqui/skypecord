package deprecated;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import Client;

public class ClientTest {
    public static void main(String[] args) throws Exception {
        Client client = new Client();
        client.register("testing", "testing");
        JSONObject res = client.getResponse();
        String exit = res.get("data").toString();
        System.out.println(exit);
    }
}

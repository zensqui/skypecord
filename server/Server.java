import java.net.*;
import java.io.*; 
import java.util.HashMap;
import org.json.simple.JSONObject;

public class Server {
    private static final int port = 5050;
    static HashMap<String, ConnectionHandler> connections = new HashMap<String, ConnectionHandler>();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server listening on port " + port + ".");

        Socket socket = null;
        while (true) {
            socket = server.accept();

            ServerEventListener listener = new EventHandler();
            ConnectionHandler connection = new ConnectionHandler(socket, listener);
            Thread t = new Thread(connection);
            t.start();

            String user = connection.getUser();
            connections.put(user, connection);
            System.out.println("User " + user + " connected.");

            JSONObject jsonOut = new JSONObject();
            jsonOut.put("type", "status");
            jsonOut.put("data", "Logged in as " + user + ".");

            connection.add(jsonOut);
        }
    }
}

interface ServerEventListener {
    void onServerEvent(JSONObject json);
}

class EventHandler implements ServerEventListener {
    public void onServerEvent(JSONObject json) {
        System.out.println(json.toJSONString());
        ConnectionHandler target = Server.connections.get(json.get("target"));
        System.out.println("Target: " + target.getUser());
        target.add(json);
    }
}
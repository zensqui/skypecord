import java.net.*;
import java.io.*; 
import java.util.HashMap;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Server {
    private static final int port = 5050;
    private static HashMap<String, ConnectionHandler> connections = new HashMap<String, ConnectionHandler>();
    private static DbInterface db = new DbInterface();

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.println("listening on port " + port);

        Socket socket = null;
        while (true) {
            socket = server.accept();
            ServerEventListener listener = new EventHandler(db, connections);

            String user = UUID.randomUUID().toString();

            ConnectionHandler connection = new ConnectionHandler(socket, listener, user);
            Thread t = new Thread(connection);
            t.start();

            
            connections.put(user, connection);
            System.out.println("connection " + user + " --> new connection from " + socket.getInetAddress() + ":" + socket.getPort());
        }
    }
}

interface ServerEventListener {
    void onServerEvent(ConnectionHandler connection, JSONObject json);
}

class EventHandler implements ServerEventListener {
    private DbInterface db;
    HashMap<String, ConnectionHandler> connections;

    public EventHandler(DbInterface db, HashMap<String, ConnectionHandler> connections) {
        this.db = db;
        this.connections = connections;
    }

    private void sendExit(ConnectionHandler connection, JSONObject json, String res) {
        JSONObject jsonOut = new JSONObject();
        jsonOut.put("type", "res");
        jsonOut.put("data", res);
        connection.add(jsonOut);
        System.out.println(json.toJSONString() + " --> " + jsonOut.toJSONString());
    }

    public void onServerEvent(ConnectionHandler connection, JSONObject json) {
        String type = (String)json.get("type");
        String res;
        switch (type) {
            case "register":
                res = db.addUser((String)json.get("user"), (String)json.get("pass"));
                sendExit(connection, json, res);
                break;
            case "login":
                res = db.auth((String)json.get("user"), (String)json.get("pass"));
                sendExit(connection, json, res);
                if(res.equals("0")) {
                    String user = connection.getUser();
                    connection.setUser((String)json.get("user"));
                    connections.remove(user);
                    connections.put((String)json.get("user"), connection);
                    System.out.println("connection " + user + " --> user " + connection.getUser());
                }
                break;
            case "addconversation":
                JSONArray users = (JSONArray)json.get("users");
                res = (db.addConversation(users) == null) ? "0" : "1";
                sendExit(connection, json, res);
                break;
            case "msg":
                ConnectionHandler target = connections.get(json.get("target"));
                target.add(json);
                System.out.println(json.toJSONString() + " --> user " + json.get("target"));
                break;
            default:
                System.out.println("Unhandled event: " + type);
        }
        
    }
}
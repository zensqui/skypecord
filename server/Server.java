import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

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

            ConnectionHandler connection = new ConnectionHandler(user, socket, listener);

            connections.put(user, connection);
            System.out.println("connection [" + user + "] --> new connection from " + socket.getInetAddress() + ":"
                    + socket.getPort());
        }
    }
}

interface ServerEventListener {
    void onServerEvent(ConnectionHandler connection, JSONObject json);

    void onConnectionClosed(ConnectionHandler connection);
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
        String type = (String) json.get("type");
        String res;
        switch (type) {
            case "register":
                res = db.addUser((String) json.get("user"), (String) json.get("pass"));
                sendExit(connection, json, res);
                break;
            case "login":
                res = db.auth((String) json.get("user"), (String) json.get("pass"));
                sendExit(connection, json, res);
                if (res.equals("0")) {
                    String user = connection.getName();
                    connection.setName((String) json.get("user"));
                    connections.remove(user);
                    connections.put((String) json.get("user"), connection);
                    System.out.println("connection [" + user + "] --> user [" + connection.getName() + "]");
                }
                break;
            case "userexists":
                res = db.userExists((String) json.get("user"));
                sendExit(connection, json, res);
                break;
            case "addConvo":
                JSONArray userlist = (JSONArray) json.get("users");
                res = (db.addConversation(userlist));
                sendExit(connection, json, res);
                break;
            case "delConvo":
                res = db.delConversation((String) json.get("cid"));
                sendExit(connection, json, res);
                break;
            case "getConvoUsers":
                res = db.getConversationUsers((String) json.get("cid"));
                sendExit(connection, json, res);
                break;
            case "getUserConvos":
                res = db.getUserConversations((String) json.get("user"));
                sendExit(connection, json, res);
                break;
            case "addConvoUser":
                res = db.addConversationUser((String) json.get("cid"), (String) json.get("user"));
                sendExit(connection, json, res);
                break;
            case "delConvoUser":
                res = db.removeConversationUser((String) json.get("cid"), (String) json.get("user"));
                sendExit(connection, json, res);
                break;
            case "getConvoMessages":
                res = db.getConversationMessages((String) json.get("cid"));
                sendExit(connection, json, res);
                break;
            case "msg":
                try {
                    db.addMessage((String) json.get("cid"), (String) json.get("user"), (String) json.get("data"));
                    JSONArray users = (JSONArray) new JSONParser()
                            .parse(db.getConversationUsers((String) json.get("cid")));
                    for (Object u : users) {
                        if (!u.equals((String) json.get("user"))) {
                            ConnectionHandler c = connections.get((String) u);
                            if (c != null) {
                                c.add(json);
                                System.out.println(json.toJSONString() + " --> " + json.toJSONString());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("error: " + e.getMessage());
                }
                break;
            default:
                System.out.println("unhandled event: " + type);
        }
    }
    
    public void onConnectionClosed(ConnectionHandler connection) {
        String user = connection.getName();
        connections.remove(user);
        connection.stop();
        System.out.println("connection from [" + user + "] --> connection closed");
    }
}
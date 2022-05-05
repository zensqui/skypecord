import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class Client {
    private Socket client;
    private ThreadedBufferedReader in;
    private OutputStreamWriter out;
    private LinkedBlockingQueue<JSONObject> queue;
    private InputEventHandler inputListener;

    private String user;

    public Client() throws IOException {
        this.client = new Socket("sc.zepr.dev", 5050);
        this.out = new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8);
        this.queue = new LinkedBlockingQueue<JSONObject>();

        inputListener = new InputEventHandler(queue);
        this.in = new ThreadedBufferedReader(client, inputListener);
        Thread tIn = new Thread(in);
        tIn.start();
    }

    public void setMessageUi(messageInput messageUi) throws IOException {
        inputListener.updateMessageUi(messageUi);
    }

    public String getUser() {
        return user;
    }

    private JSONObject getResponse() {
        try {
            JSONObject res = queue.poll(10L, TimeUnit.SECONDS);
            if (res.get("type").equals("res")) {
                return res;
            }
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject login(String user, String pass) throws IOException, ParseException {
        JSONObject json = new JSONObject();
        json.put("type", "login");
        json.put("user", user);
        json.put("pass", pass);
        out.append(json.toJSONString() + "\n");
        out.flush();

        this.user = user;

        return getResponse();
    }

    public JSONObject register(String user, String pass) throws IOException, ParseException {
        JSONObject json = new JSONObject();
        json.put("type", "register");
        json.put("user", user);
        json.put("pass", pass);
        out.append(json.toJSONString() + "\n");
        out.flush();

        return getResponse();
    }

    public void message(String cid, String data) throws IOException, ParseException {
        JSONObject json = new JSONObject();
        json.put("type", "msg");
        json.put("user", user);
        json.put("cid", cid);
        json.put("data", data);
        out.append(json.toJSONString() + "\n");
        out.flush();
    }

    public String userExists(String user) throws IOException {
        JSONObject json = new JSONObject();
        json.put("type", "userexists");
        json.put("user", user);
        out.append(json.toJSONString() + "\n");
        out.flush();

        JSONObject res = getResponse();
        return (String) res.get("data");
    }

    public String addConvo(String users) throws IOException {
        String[] userarray = users.split(", ");
        JSONArray userjson = new JSONArray();
        for (String user : userarray) {
            userjson.add(user);
        }

        JSONObject json = new JSONObject();
        json.put("type", "addConvo");
        json.put("users", userjson);
        out.append(json.toJSONString() + "\n");
        out.flush();

        JSONObject jsonOut = getResponse();
        String cid = (String) jsonOut.get("data");
        return cid;
    }

    public JSONObject delConvo(String cid) throws IOException {
        JSONObject json = new JSONObject();
        json.put("type", "delConvo");
        json.put("cid", cid);
        out.append(json.toJSONString() + "\n");
        out.flush();

        return getResponse();
    }

    public String[] getConvoUsers(String cid) throws IOException {
        JSONObject json = new JSONObject();
        json.put("type", "getConvoUsers");
        json.put("cid", cid);
        out.append(json.toJSONString() + "\n");
        //System.out.println("SENDING GETUSERS REQ (CLIENT.JAVA:119) | " + json.toJSONString());
        out.flush();

        JSONObject jsonOut = getResponse();
        //System.out.println("RECIEVING GETUSERS RES (CLIENT.JAVA:123) | " + jsonOut.toJSONString());
        String[] users = jsonOut.get("data").toString().substring(1, jsonOut.get("data").toString().length() - 1)
                .split(", ");
        return users;
    }

    public String[] getUserConvos() throws IOException, ParseException {
        JSONObject json = new JSONObject();
        json.put("type", "getUserConvos");
        json.put("user", user);
        out.append(json.toJSONString() + "\n");
        out.flush();

        JSONObject jsonOut = getResponse();
        JSONArray convos = (JSONArray) new JSONParser().parse(jsonOut.get("data").toString());
        String[] cids = new String[convos.size()];
        for (int i = 0; i < convos.size(); i++) {
            cids[i] = (String) convos.get(i);
        }
        return cids;
    }

    public JSONObject addConvoUser(String cid, String user) throws IOException {
        JSONObject json = new JSONObject();
        json.put("type", "addConvoUser");
        json.put("cid", cid);
        json.put("user", user);
        out.append(json.toJSONString() + "\n");
        out.flush();

        return getResponse();
    }

    public JSONObject delConvoUser(String cid, String user) throws IOException {
        JSONObject json = new JSONObject();
        json.put("type", "delConvoUser");
        json.put("cid", cid);
        json.put("user", user);
        out.append(json.toJSONString() + "\n");
        out.flush();

        return getResponse();
    }

    public JSONArray getConvoMessages(String cid) throws IOException, ParseException {
        JSONObject json = new JSONObject();
        json.put("type", "getConvoMessages");
        json.put("cid", cid);
        out.append(json.toJSONString() + "\n");
        out.flush();

        JSONObject jsonOut = getResponse();
        JSONArray messages = (JSONArray) new JSONParser().parse(jsonOut.get("data").toString());
        return messages;
    }
}

interface InputEventListener {
    void onInputEvent(JSONObject json);
}

class InputEventHandler implements InputEventListener {
    private LinkedBlockingQueue<JSONObject> queue;
    private messageInput messageUi;

    public InputEventHandler(LinkedBlockingQueue<JSONObject> queue) {
        this.queue = queue;
    }

    public void updateMessageUi(messageInput messageUi) {
        this.messageUi = messageUi;
    }

    public void onInputEvent(JSONObject json) {
        //System.out.println("RECIEVING EVENT (CLIENT.JAVA:156) | " + json.toJSONString());
        if (json.get("type").equals("msg")) {
            //System.out.println("MESSAGE RECIEVED (CLIENT.JAVA:195) | [" + json.get("user") + "] " + json.get("data"));
            messageUi.addMessage(json.get("user").toString(), json.get("data").toString(), json.get("cid").toString());
            messageUi.textSpeech(json.get("user").toString() + " said " + json.get("data").toString());
        } else {
            //System.out.println("EVENT RECIEVED (CLIENT.JAVA:199) | " + json.toJSONString());
            try {
                //System.out.println("OTHER RESPONSE RECIEVED (CLIENT.JAVA:201) | " + json.toJSONString());
                //System.out.println("QUEUE BEFORE ADDING (CLIENT.JAVA:202) | " + queue.toString());
                this.queue.put(json);
                //System.out.println("QUEUE AFTER ADDING (CLIENT.JAVA:204) | " + queue.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

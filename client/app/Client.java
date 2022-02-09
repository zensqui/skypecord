import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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

    public JSONObject getResponse() {
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

    public void login(String user, String pass) throws IOException, ParseException {
        JSONObject json = new JSONObject();
        json.put("type", "login");
        json.put("user", user);
        json.put("pass", pass);
        out.append(json.toJSONString() + "\n");
        out.flush();

        this.user = user;
    }

    public void register(String user, String pass) throws IOException, ParseException {
        JSONObject json = new JSONObject();
        json.put("type", "register");
        json.put("user", user);
        json.put("pass", pass);
        out.append(json.toJSONString() + "\n");
        out.flush();
    }

    public void message(String target, String data) throws IOException, ParseException {
        JSONObject json = new JSONObject();
        json.put("type", "msg");
        json.put("user", user);
        json.put("target", target);
        json.put("data", data);
        out.append(json.toJSONString() + "\n");
        out.flush();
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
        if(json.get("type").equals("msg")) {
            System.out.println("[" + json.get("user") + "] " + json.get("data"));
            messageUi.addMessage(json.get("data").toString());
        } else {
            try {
                this.queue.put(json);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

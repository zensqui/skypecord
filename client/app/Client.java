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

    private String user;

    public Client() throws IOException {
        this.client = new Socket("sc.zenithproject.xyz", 5050);
        this.out = new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8);
        this.queue = new LinkedBlockingQueue<JSONObject>();

        InputEventListener inputListener = new InputEventHandler(queue);
        this.in = new ThreadedBufferedReader(client, inputListener);
        Thread tIn = new Thread(in);
        tIn.start();
    }

    public JSONObject getResponse() {
        try {
            JSONObject res = queue.poll(10L, TimeUnit.SECONDS);
            return res;
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

    public void message(String type, String target, String data) throws IOException, ParseException {
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

    public InputEventHandler(LinkedBlockingQueue<JSONObject> queue) {
        this.queue = queue;
    }

    public void onInputEvent(JSONObject json) {
        if(json.get("type") == "msg") {
            System.out.println("[" + json.get("user") + "] " + json.get("data"));
        } else {
            try {
                this.queue.put(json);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

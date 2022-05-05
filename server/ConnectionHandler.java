import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import org.json.simple.JSONObject;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionHandler implements Runnable {
    private String name;
    private Thread t;
    private boolean exit;

    private Socket socket;
    private ServerEventListener listener;
    private LinkedBlockingQueue<JSONObject> queue;

    private ThreadedBufferedReader in;
    private OutputStreamWriter out;
    private ThreadedQueueReader queueReader;

    public ConnectionHandler(String user, Socket socket, ServerEventListener listener) {
        this.name = user;
        this.exit = false;

        this.socket = socket;
        this.listener = listener;
        this.queue = new LinkedBlockingQueue<JSONObject>();

        this.t = new Thread(this, user + ":ch");
        this.t.start();
    }

    public void run() {
        System.out.println("ConnectionHandler: run()");

        try {
            InputEventListener inputListener = new InputEventHandler(this, listener);
            in = new ThreadedBufferedReader(name, socket, inputListener);

            this.out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);

            QueueEventListener queueListener = new QueueEventHandler(out);
            queueReader = new ThreadedQueueReader(name, queue, queueListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        System.out.println("ConnectionHandler: stop()");
        exit = true;
        try {
            in.stop();
            queueReader.stop();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        this.name = name;
        t.setName(name);
    }

    public String getName() {
        return this.name;
    }

    public void add(JSONObject json) {
        try {
            this.queue.put(json);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

interface InputEventListener {
    void onInputEvent(JSONObject json);

    void onDisconnect();
}

class InputEventHandler implements InputEventListener {
    private ConnectionHandler connection;
    private ServerEventListener listener;

    public InputEventHandler(ConnectionHandler connection, ServerEventListener listener) {
        this.connection = connection;
        this.listener = listener;
    }

    public void onInputEvent(JSONObject json) {
        listener.onServerEvent(connection, json);
    }

    public void onDisconnect() {
        listener.onConnectionClosed(connection);
    }
}

interface QueueEventListener {
    void onOutputEvent(JSONObject json);
}

class QueueEventHandler implements QueueEventListener {
    private OutputStreamWriter out;

    public QueueEventHandler(OutputStreamWriter out) {
        this.out = out;
    }

    public void onOutputEvent(JSONObject json) {
        try {
            out.write(json.toJSONString() + "\n");
            // System.out.println("QEVENT SENDING: " + json.toJSONString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
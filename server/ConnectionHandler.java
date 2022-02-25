import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import org.json.simple.JSONObject;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionHandler implements Runnable {
    private Socket socket;
    private ServerEventListener listener;
    private LinkedBlockingQueue<JSONObject> queue;

    private ThreadedBufferedReader in;
    private OutputStreamWriter out;
    private ThreadedQueueReader queueReader;

    private String user;

    public ConnectionHandler(Socket socket, ServerEventListener listener, String user) {
        this.socket = socket;
        this.listener = listener;
        this.queue = new LinkedBlockingQueue<JSONObject>();
        this.user = user;
    }

    public void run() {
        try {
            InputEventListener inputListener = new InputEventHandler(this, listener);
            in = new ThreadedBufferedReader(socket, inputListener);
            Thread tIn = new Thread(in);
            tIn.start();

            this.out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);

            QueueEventListener queueListener = new QueueEventHandler(out);
            queueReader = new ThreadedQueueReader(queue, queueListener);
            Thread tQueue = new Thread(queueReader);
            tQueue.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return this.user;
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

    public void onOutputEvent(JSONObject json){
        try {
            out.write(json.toJSONString() + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
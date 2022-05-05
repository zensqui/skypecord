import java.io.*;
import java.net.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class ThreadedBufferedReader implements Runnable {
    private String name;
    private Thread t;
    private boolean exit;

    private BufferedReader in;
    private InputEventListener listener;

    public ThreadedBufferedReader(String name, Socket socket, InputEventListener listener) throws IOException {
        this.name = name;
        this.exit = false;

        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.listener = listener;

        this.t = new Thread(this, name + ":tbr");
        this.t.start();
    }

    public void run() {
        System.out.println("ThreadedBufferedReader: run()");

        String input = "";
        JSONObject jsonIn = new JSONObject();
        JSONParser parser = new JSONParser();

        while (!exit) {
            try {
                while ((input = in.readLine()) != null) {
                    try {
                        jsonIn = (JSONObject) parser.parse(input);
                        listener.onInputEvent(jsonIn);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                listener.onDisconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        System.out.println("ThreadedBufferedReader: stop()");
        this.exit = true;
    }
}
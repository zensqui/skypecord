import java.io.*;
import java.net.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class ThreadedBufferedReader implements Runnable {
    private BufferedReader in;
    private InputEventListener listener;

    public ThreadedBufferedReader(Socket socket, InputEventListener listener) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.listener = listener;
    }

    public void run() {
        String input = "";
        JSONObject jsonIn = new JSONObject();
        JSONParser parser = new JSONParser();

        try {
            while ((input = in.readLine()) != null) {
                try {
                    jsonIn = (JSONObject) parser.parse(input);
                    listener.onInputEvent(jsonIn);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
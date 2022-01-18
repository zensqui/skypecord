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
        try {
            String input = "";
            while ((input = in.readLine()) != null) {
                JSONObject jsonIn = new JSONObject();
                jsonIn = (JSONObject)new JSONParser().parse(input);
                listener.onInputEvent(jsonIn);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }
}
import java.io.*;
import java.net.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.util.Arrays;
import java.util.HashMap;

public class BackgroundRefresh implements Runnable {
    private Client client;
    private messageInput messageUi;
    private int delay;

    public BackgroundRefresh(Client client, messageInput mIn, int delay) {
        this.client = client;
        this.messageUi = mIn;
        this.delay = delay;
    }

    public void run() {
        while (true) {
            try {
                System.out.println("Conversation list refreshed.");
                Thread.sleep(delay * 1000);

                String[] convos;
                HashMap<String, String> currentConvos;
                convos = client.getUserConvos();
                currentConvos = messageUi.getCurrentConvos();
                for (String convo : convos) {
                    if (!currentConvos.containsValue(convo)) {
                        messageUi.addConvo(convo);
                    }
                }
                for (String convo : currentConvos.values()) {
                    if (!Arrays.stream(convos).anyMatch(convo::equals)) {
                        messageUi.removeConvo(convo);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
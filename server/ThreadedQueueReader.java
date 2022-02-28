import org.json.simple.JSONObject;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadedQueueReader implements Runnable {
    private LinkedBlockingQueue<JSONObject> queue;
    private QueueEventListener listener;

    public ThreadedQueueReader(LinkedBlockingQueue<JSONObject> queue, QueueEventListener listener) {
        this.queue = queue;
        this.listener = listener;
    }

    public void run() {
        while (true) {
            if (!queue.isEmpty()) {
                JSONObject jsonOut = queue.poll();
                System.out.println("ThreadedQueueReader: " + jsonOut);
                listener.onOutputEvent(jsonOut);
            }
        }
    }
}
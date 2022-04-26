import org.json.simple.JSONObject;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadedQueueReader implements Runnable {
    private String name;
    private Thread t;
    private boolean exit;

    private LinkedBlockingQueue<JSONObject> queue;
    private QueueEventListener listener;

    public ThreadedQueueReader(String name, LinkedBlockingQueue<JSONObject> queue, QueueEventListener listener) {
        this.name = name;
        this.exit = false;

        this.queue = queue;
        this.listener = listener;

        this.t = new Thread(this, name + ":tqr");
        this.t.start();
    }

    public void run() {
        while (!exit) {
            try {
                JSONObject jsonOut = queue.take();
                listener.onOutputEvent(jsonOut);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*
             * if (!queue.isEmpty()) {
             * JSONObject jsonOut = queue.poll();
             * // System.out.println("ThreadedQueueReader: " + jsonOut);
             * listener.onOutputEvent(jsonOut);
             * }
             */
        }
    }

    public void stop() {
        this.exit = true;
    }
}
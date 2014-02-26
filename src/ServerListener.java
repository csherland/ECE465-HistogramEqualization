import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Created by lusterdome on 2/26/14.
 */
public class ServerListener implements Runnable {

    private PriorityQueue<ServerStatus> queue;
    private HashMap<String, ServerStatus> hashMap;
    private Comparator<ServerStatus> loadComparator = new ServerLoadComparator();
    private ServerSocket sock;

    public ServerListener(PriorityQueue<ServerStatus> q, HashMap<String, ServerStatus> hm, int portNumber) throws IOException {
        this.queue = q;
        this.hashMap = hm;
        this.sock = new ServerSocket(portNumber);
    }

    @Override
    public void run() {
        while (true) try {
            Socket newSock = sock.accept();
            ObjectInputStream ois = (ObjectInputStream) newSock.getInputStream();
            ServerStatus status = (ServerStatus) ois.readObject();

            String key = status.getKey();
            if (hashMap.containsKey(key)) {
                queue.remove(hashMap.get(key));
            }
            hashMap.put(key, status);
            queue.add(status);

            ois.close();
            newSock.close();
        } catch (Exception e) {
            System.out.println("Something went wrong.");
        }
    }
}

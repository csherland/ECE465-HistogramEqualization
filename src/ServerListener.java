import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class ServerListener implements Runnable {

    private PriorityQueue<ServerStatus> queue;
    private HashMap<String, ServerStatus> hashMap;
    private Comparator<ServerStatus> loadComparator = new ServerLoadComparator();
    private ServerSocket serverSocket;

    public ServerListener(PriorityQueue<ServerStatus> queue, HashMap<String, ServerStatus> hm, int portNumber) throws IOException {
        this.queue = queue;
        this.hashMap = hm;
        this.serverSocket = new ServerSocket(portNumber);
    }

    @Override
    public void run() {
        while (true) try {
            Socket socket = serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ServerStatus status =(ServerStatus) ois.readObject();

            String key = status.getKey();
            if (hashMap.containsKey(key)) {
                queue.remove(hashMap.get(key));
            }
            hashMap.put(key, status);
            queue.add(status);

            ois.close();
            socket.close();
        } catch (ClassNotFoundException e) {
            System.err.println("ServerListener: run: ClassNotFoundException.");
        } catch (IOException e) {
            System.err.println("ServerListener: run: IOException.");
        }
    }
}

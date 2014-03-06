/**
 * ServerListener.java
 *  A runnable that listens for new histogram server connections
 *  and stores information on the server within the specified
 *  priority queue and hashmap.
 *
 *  Listens for new histrogram server connections on the port specified
 *  by portNumber.
 *
 *  @author Christian Sherland
 *  @author Ethan Lusterman
 *  @author Michael Scibor
 *
 *  @version 1.0 Mar 6 2014
 */

import java.io.*;
import java.net.*;
import java.util.*;

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


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.PriorityQueue;

/* LoadBalancer receives requests from Client objects to be connected to an
 * available Server. Tells the Client which Server is available. */

public class LoadBalancer {
    public static void main(String[] args){

        // priority queue and hash table and comparator
        // initialize ServerListener with these
        // ServerLoadComparator
        // ServerListener implements Runnable (run method)
        //
        // ServerListener
        // ServerLoadComparator
        PriorityQueue<ServerStatus> queue = new PriorityQueue<ServerStatus>();
        HashMap<String,ServerStatus> hm;
        hm = new HashMap<String, ServerStatus>();
        int portNumber = 9999;

        Runnable listener = new ServerListener(queue, hm, portNumber);
        listener.run();

        try {
            ServerSocket serverSocket = new ServerSocket(2014);
            while(true){
                System.out.println("New Connection established.");
                Socket socket = serverSocket.accept();

                // look at best value in priority queue
                // connect client with that server



            }
        } catch (IOException e) {
            System.out.println("ServerSocket not established successfully.");
            System.exit(1);
        }
    }
}
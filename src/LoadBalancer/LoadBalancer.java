/*
 * LoadBalancer.java
 *  Receives requests from Client objects to be connected to an
 *  available Server. Tells the Client which Server is available.
 */

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.PriorityQueue;

public class LoadBalancer {
    public static void main(String[] args){

        // Listening ports
        final int CLIENT_PORT = 2014, SERVER_PORT = 9999;

        // Listen for connections from HistogramServers
        PriorityQueue<ServerStatus> queue = new PriorityQueue<ServerStatus>();
        HashMap<String, ServerStatus> hm = new HashMap<String, ServerStatus>();

        try {
            Runnable listener = new ServerListener(queue, hm, SERVER_PORT);
            listener.run();
        } catch (IOException e) {
            System.err.println("LoadBalancer: new ServerListener: IOException.");
        }

        try {
            // Listen for connections from clients and connect them with the best HistogramServer
            ServerSocket serverSocket = new ServerSocket(CLIENT_PORT);
            while(true){

                Socket socket = serverSocket.accept();
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("New connection from client.");

                ServerStatus bestServer = queue.remove();

                output.writeObject(bestServer.getHost());
                output.writeObject(bestServer.getPort());

                output.close();
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error: Could not listen on port" + CLIENT_PORT);
            System.exit(1);
        }
    }
}

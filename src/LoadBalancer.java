/*
 * LoadBalancer.java
 *  Receives requests from Client objects to be connected to an
 *  available Server. Tells the Client which Server is available.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.PriorityQueue;

public class LoadBalancer {
    public static void main(String[] args){

        // Listening ports
        int clientPort = 2014;
        int serverPort = 9999;

        // Listen for connections from HistogramServers
        PriorityQueue<ServerStatus> queue = new PriorityQueue<ServerStatus>();
        HashMap<String, ServerStatus> hm;
        hm = new HashMap<String, ServerStatus>();

        Runnable listener = new ServerListener(queue, hm, serverPort);
        listener.run();

        try {
            // Listen fro connections from clients and connect
            // them with the best HistogramServer
            ServerSocket serverSocket = new ServerSocket(clientPort);
            while(true){
                System.out.println("New connection from client");
                Socket socket = serverSocket.accept();
            }

        } catch (IOException e) {
            System.out.println("Error: Could not listen on port" + clientPort);
            System.exit(1);
        }
    }
}

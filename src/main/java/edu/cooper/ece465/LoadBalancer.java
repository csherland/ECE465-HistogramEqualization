/**
 * LoadBalancer.java
 *  Receives requests from Client objects to be connected to an
 *  available Server. Tells the Client which Server is available.
 *
 *  @author Christian Sherland
 *  @author Ethan Lusterman
 *  @author Michael Scibor
 *
 *  @version 1.0 Mar 6 2014
 */

package edu.cooper.ece465;

import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoadBalancer {

    private static Log LOG = LogFactory.getLog(LoadBalancer.class);

    public static void main(String[] args){

        if (args.length != 2) {
            LOG.debug("Usage: java LoadBalancer <client port number> <server port number>");
            System.exit(1);
        }

        // Listening ports
        final int CLIENT_PORT = Integer.parseInt(args[0]), SERVER_PORT = Integer.parseInt(args[1]);

        // Listen for connections from HistogramServers
        PriorityQueue<ServerStatus> queue = new PriorityQueue<ServerStatus>();
        HashMap<String, ServerStatus> hm = new HashMap<String, ServerStatus>();

        try {
            Runnable listener = new ServerListener(queue, hm, SERVER_PORT);
            (new Thread(listener)).start();
        } catch (IOException e) {
            LOG.debug("LoadBalancer: new ServerListener: IOException.");
        }
        try {
            // Listen for connections from clients and connect them with the best HistogramServer
            ServerSocket serverSocket = new ServerSocket(CLIENT_PORT);
            LOG.debug("Waiting for new client connections.");
            while(true){

                Socket socket = serverSocket.accept();
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                LOG.debug("New connection from client.");

                ServerStatus bestServer = queue.peek();

                output.writeObject(bestServer.getHost());
                output.writeObject(bestServer.getPort());

                output.close();
                socket.close();
            }
        } catch (IOException e) {
            LOG.debug("Error: Could not listen on port" + CLIENT_PORT);
            System.exit(1);
        }
    }
}

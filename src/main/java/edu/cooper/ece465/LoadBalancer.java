/*
 * Authors: Christian Sherland
 *          Ethan Lusterman
 *          Michael Scibor
 *
 * Date:    2/21/14
 *
 * LoadBalancer.java
 *      Accepts connections asking for histogram equalization
 *      servers and distributes load fairly across available
 *      equalization servers
 *
 */

package ece465.cooper.edu;
import java.util.*;
import java.net.*;
import java.io.*;

public class LoadBalancer {

    public static void main(int[] args) {

        int portNumber = 1992;
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {

            // Listen for connections continuously
            while (true) {
                System.out.println("New Connection");
                try {
                    Socket socket = serverSocket.accept();
        

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
}

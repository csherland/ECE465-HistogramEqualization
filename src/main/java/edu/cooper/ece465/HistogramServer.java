/*
 * Authors: Christian Sherland
 *          Ethan Lusterman
 *          Michael Scibor
 *
 * Date:    2/21/14
 *
 * HistogramServer.java
 *      Connects to the master load balancer and
 *      applies histogram equalization to incoming
 *      images.
 *
 *      Images are all equalized on different threads
 *
 *      The maximum number of threads is specified by
 *      MAX_NUM_THREADS
 *
 */

package ece465.cooper.edu;
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class HistogramServer {

    private static final int MAX_NUM_THREADS = 2;

    public static void main() {

        String hostName = "localhost";
        int portNumber = 1992;

        // Thread pool setup
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        //ThreadPoolExecutor executorPool = new ThreadPoolExecutor(THREAD_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
        //                                        new ArrayBlockingQueue<Runnable>(WORK_Q_SIZE), threadFactory, rejectionHandler);

        /*
         * Check for new clients and create consumers for each client
         */
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            while (true) {
                Socket s = serverSocket.accept();
                //executorPool.execute( new HistogramEQConsumer(s) );
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }

    }

}

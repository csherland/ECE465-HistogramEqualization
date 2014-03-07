/**
 * HistogramServer.java
 *  An image equalization server. Accepts new socket connections
 *  and performs image equalization on specified data.
 *
 *  Command line arguments specify the load balancer host name, the
 *  port on which to connect to the load balancer and the port to
 *  listen for connections from clients.
 *
 *  @author Christian Sherland
 *  @author Ethan Lusterman
 *  @author Michael Scibore
 *
 *  @version 1.0 Mar 6 2014
 */

package edu.cooper.ece465;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class HistogramServer {
    private static final int THREAD_POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE    = 4;
    private static final int KEEP_ALIVE_TIME  = 5;
    private static final int WORK_QUEUE_SIZE  = 2;
    private static Log LOG = LogFactory.getLog(HistogramServer.class);

    public static void main(String[] args) {

        if (args.length != 3) {
            LOG.fatal("Usage: java HistogramServer <load balancer host> <load balancer port> <client port>");
            System.exit(1);
        }

        final String HOSTNAME = args[0];
        final int SERVERPORT  = Integer.parseInt(args[1]);
        final int CLIENTPORT  = Integer.parseInt(args[2]);

        // Setup the thread pool
        RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ThreadPoolExecutor executorPool = new ThreadPoolExecutor(THREAD_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(WORK_QUEUE_SIZE),
                threadFactory, rejectionHandler);

        // Start the monitoring thread
        MonitorThread monitor = new MonitorThread(executorPool, 3, HOSTNAME, SERVERPORT, CLIENTPORT);
        Thread monitorThread  = new Thread(monitor);
        monitorThread.start();

        // Listen for client connections
        try {
            ServerSocket serverSocket = new ServerSocket(CLIENTPORT);
            LOG.info("Listening for new client connections.");
            while(true) {
                Socket socket = serverSocket.accept();
                LOG.info("Accepted new client connection.");

                executorPool.execute(new HistogramWorker(socket));
            }
        } catch (IOException e) {
            LOG.fatal("ServerSocket not functional.");
        }

        // Shutdown threadpool and monitor
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            LOG.error("Thread sleep error.", e);
        }
        executorPool.shutdown();
        monitor.shutdown();

        LOG.info("Histogram server shut down.");
    }
}

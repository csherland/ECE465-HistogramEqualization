/*
 * HistogramServer.java
 *  An image equalization server. Accepts new socket connections
 *  and performs image equalization on specified data
 */

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class HistogramServer {
    private static final int THREAD_POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 4;
    private static final int KEEP_ALIVE_TIME = 5;
    private static final int WORK_QUEUE_SIZE = 2;

    public static void main(String[] args) {

        // Setup the thread pool
        RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ThreadPoolExecutor executorPool = new ThreadPoolExecutor(THREAD_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                                                                 TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(WORK_QUEUE_SIZE),
                                                                 threadFactory, rejectionHandler);

        //start the monitoring thread
        MonitorThread monitor = new MonitorThread(executorPool, 3, "localhost", 9999, 2014);
        Thread monitorThread = new Thread(monitor);
        monitorThread.start();

        // open connection on socket
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            while(true) {
                System.out.println("Waiting for new connections.");
                Socket socket = serverSocket.accept();

                executorPool.execute(new HistogramWorker(socket));

            }
        } catch (IOException e) {
            System.out.println("ServerSocket not functional.");
        }

        // Shutdown threadpool and monitor
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorPool.shutdown();
        monitor.shutdown();

    }
}

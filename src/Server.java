/*
 * Server.java
 *  An image equalization server. Accepts new socket connections
 *  and performs image equalization on specified data
 */

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Server {
    private static final int THREAD_POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 4;
    private static final int KEEP_ALIVE_TIME = 5;
    private static final int WORK_QUEUE_SIZE = 2;

    public static void main(String[] args) {

        BufferedImage uneq = null;

        // Setup the thread pool
        RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        ThreadPoolExecutor executorPool = new ThreadPoolExecutor(THREAD_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                                                                 TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(WORK_QUEUE_SIZE),
                                                                 threadFactory, rejectionHandler);

        //start the monitoring thread
        MyMonitorThread monitor = new MyMonitorThread(executorPool, 3);
        Thread monitorThread = new Thread(monitor);
        monitorThread.start();

        // open connection on socket
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            while(true) {
                System.out.println("Waiting for new connections.");
                Socket socket = serverSocket.accept();

                executorPool.execute(new Worker(1));

                /*
                DataInputStream input = new DataInputStream(socket.getInputStream());
                System.out.println("Input stream open: Server side.");

                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                System.out.println("Output stream open: Server side.");

                while (true) {
                    uneq = ImageIO.read(socket.getInputStream());
                    if (uneq != null) {
                        break;
                    }
                }
                System.out.println("received image. equalizing...");

                BufferedImage eqd = Equalization.computeHistogramEQ(uneq);
                ImageIO.write(eqd, "PNG", socket.getOutputStream());

                output.close();
                input.close();
                socket.close();
                */
            }
        } catch (IOException e) {
            System.out.println("ServerSocket not functional.");
        }

        // Shutdown threadpool and monitor
        Thread.sleep(5000);
        executorPool.shutdown();
        monitor.shutdown();

    }
}

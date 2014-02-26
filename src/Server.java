
/* Once connected with a Client, the Server handles Equalization. */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    public static void main(String[] args) {

        BufferedImage uneq = null;

        //RejectedExecutionHandler implementation
        RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
        //Get the ThreadFactory implementation to use
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        //creating the ThreadPoolExecutor
        ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(6), threadFactory, rejectionHandler);

        //start the monitoring thread
        MyMonitorThread monitor = new MyMonitorThread(executorPool, 3);
        Thread monitorThread = new Thread(monitor);
        monitorThread.start();

        //submit work to the thread pool
        for(int i=0; i<10; i++){
            executorPool.execute(new WorkerThread(i));
        }


        // open connection on socket
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            Socket socket = serverSocket.accept();

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
        } catch (IOException e) {
            System.out.println("ServerSocket not functional.");
        }

        Thread.sleep(30000);
        //shut down the pool
        executorPool.shutdown();
        //shut down the monitor thread
        Thread.sleep(5000);
        monitor.shutdown();

    }
}

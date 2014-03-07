/**
 * MonitorThread.java
 *  Responsible for monitoring the state of threads in the
 *  HistogramServer thread pool. Periodically send load information 
 *  on performance to the LoadBalancer
 *
 *  @author Christian Sherland
 *  @author Ethan Lusterman
 *  @author Michael Scibor
 *
 *  @version 1.0 Mar 6 2014
 */

package edu.cooper.ece465;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MonitorThread implements Runnable {

    private ThreadPoolExecutor executor;
    private int seconds;
    private boolean run=true;
    private String hostname;
    private int portNumber;
    private int clientPortNumber;
    private static Log LOG = LogFactory.getLog(MonitorThread.class);

    public MonitorThread(ThreadPoolExecutor executor, int delay, String hostname, int portNumber, int clientPortNumber) {
        LOG.info("Spawning new monitor thread");
        this.executor = executor;
        this.seconds = delay;
        this.hostname = hostname;
        this.portNumber = portNumber;
        this.clientPortNumber = clientPortNumber;
    }

    public void shutdown(){
        LOG.info("Shutting down monitor thread");
        this.run = false;
    }

    @Override
    public void run() {
        while(run){
            // Locally display information about thread pool
            LOG.info(
                    String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
                        this.executor.getPoolSize(),
                        this.executor.getCorePoolSize(),
                        this.executor.getActiveCount(),
                        this.executor.getCompletedTaskCount(),
                        this.executor.getTaskCount(),
                        this.executor.isShutdown(),
                        this.executor.isTerminated()));

            // Talk to master server with current load stats and server status
            try {
                Socket socket = new Socket(InetAddress.getByName(this.hostname), this.portNumber);
                ServerStatus data = new ServerStatus(socket.getLocalAddress().getHostName(), this.clientPortNumber, 1.0);
                OutputStream os = socket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(data);
                oos.close();
                os.close();
                LOG.info("MyMonitor sent data: " + data.toString());
                Thread.sleep(seconds*1000);
            } catch (Exception e) {
                LOG.error("Error: could not communicate with master server.", e);
            }

        }
    }
}


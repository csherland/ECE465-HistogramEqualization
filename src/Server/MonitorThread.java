/*
 * MonitorThread.java
 *  Responsible for monitoring the state of threads in
 *  the HistogramServer thread pool. Periodically sends
 *  load information on performance to the LoadBalancer
 */

import java.util.concurrent.ThreadPoolExecutor;

public class MonitorThread implements Runnable {

    private ThreadPoolExecutor executor;
    private int seconds;
    private boolean run=true;

    public MonitorThread(ThreadPoolExecutor executor, int delay) {
        this.executor = executor;
        this.seconds = delay;
    }

    public void shutdown(){
        this.run=false;
    }

    @Override
    public void run() {
        while(run){
            // Locally display information about thread pool
            System.out.println(
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
                Socket socket = new Socket(this.hostname, this.portNumber);
                WorkerData data = new WorkerData(socket.getLocalAddress().getHostName(), this.clientPortNumber, 1.0);
                OutputStream os = socket.getOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(data);
                oos.close();
                os.close();
                System.out.println("MyMonitor sent data: " + data.toString());
                Thread.sleep(seconds*1000);
            } catch (Exception e) {
                System.out.println("Error: could not communicate with master server.");
            }

        }
    }
}


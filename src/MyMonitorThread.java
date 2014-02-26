import java.util.concurrent.ThreadPoolExecutor;

public class MyMonitorThread implements Runnable {

    private ThreadPoolExecutor executor;
    private int seconds;
    private boolean run=true;

    public MyMonitorThread(ThreadPoolExecutor executor, int delay) {
        this.executor = executor;
        this.seconds = delay;
    }

    public void shutdown(){
        this.run=false;
    }

    @Override
    public void run() {
        while(run){
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
                OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
                double load = osBean.getSystemLoadAverage();
                Socket socket = new Socket(this.hostname, this.portNumber);
                WorkerData data = new WorkerData(socket.getLocalAddress().getHostName(), this.clientPortNumber, load);
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


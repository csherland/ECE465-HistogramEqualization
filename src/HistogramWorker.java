/*
 *
 * HistogramWorker.java
 *  Performs equalization on a specified image in a thread
 *
 */

public class HistogramWorker implements Runnable {

    private int id;

    public HistogramWorker(int id) {
        System.out.println("New histogram worker spawned");
        id = id;
    }

    // Image equalization
    public void run() {

    }

}

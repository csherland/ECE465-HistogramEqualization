/*
 *
 * Worker.java
 *  Performs equalization on a specified image in a thread
 *
 */

public class Worker implements Runnable {

    private int id;

    public Worker(int id) {
        System.out.println("New worker spawned");
        id = id;
    }

    // Image equalization
    public void run() {

    }

}

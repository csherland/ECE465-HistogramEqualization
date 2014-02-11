/*
 * Authors: Christian Sherland
 *          Ethan Lusterman
 *          Elli Rappaport
 *          Michael Scibor
 *
 * Date:    2/4/14
 *
 */

package edu.cooper.ece465;

public class CubbyHole {
    private int contents;
    private int producers = 0;
    private boolean available = false;
    private boolean done = false;

    public synchronized void registerProducer() {
        producers++;
    } 

    public synchronized void deregisterProducer() {
        producers--;
        notifyAll();
    } 

    public synchronized boolean getDone() {
        return done;
    }

    public synchronized int get() {
        while (available == false) {
            if (producers == 0) {
                done = true;
                break;
            }
 
            try {
                wait();
            } catch (InterruptedException e) { }

        }

        available = false;
        notifyAll();
        return contents;
    }
 
    public synchronized void put(int value) {
        while (available == true) {
            try {
                wait();
            } catch (InterruptedException e) { }

        }
        
        contents = value;
        available = true;
        notifyAll();
    }
}

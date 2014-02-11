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

public class CubbyProducer extends Thread {
    private CubbyHole cubbyhole;
    private int number;
 
    public CubbyProducer(CubbyHole c, int number) {
        cubbyhole = c;
        this.number = number;
    }
 
    public void run() {
        cubbyhole.registerProducer();
        
        for (int i = 0; i < ProducerConsumerTest.PROD_SIZE; i++) {
            cubbyhole.put(i);
            System.out.println("Producer #" + this.number
                               + " put: " + i);
            try {
                sleep((int)(Math.random() * 100));
            } catch (InterruptedException e) { }

        }

        cubbyhole.deregisterProducer();
    }
}

package edu.cooper.ece465;
import java.util.*;
import java.awt.image.BufferedImage;

public class ImageHelper {

    private BufferedImage unequalized;
    private BufferedImage equalized;

    /* Some flags for determining whether a process
     * is a consumer or a producer */

    private boolean doneEqualizing = false;
    private boolean availableUnequalized = false;
    private boolean availableEqualized = false;

    public synchronized void setDoneEqualizing(boolean value) {
        doneEqualizing = value;
    }

    public synchronized boolean getDoneEqualizing() {
        return doneEqualizing;
    }

    /* Deals with passing the image between the ImageProducer
     * and the ImageConsumer.
     */

    public synchronized BufferedImage getUnequalized() {
        while (availableUnequalized == false) {
            if (doneEqualizing) {
                return null;
            }

            try {
                wait();
            } catch (InterruptedException e) { }

        }

        availableUnequalized = false;
        notifyAll();
        return unequalized;
    }

    public synchronized void putUnequalized(BufferedImage newImage) {
        while (availableUnequalized == true) {
            try {
                wait();
            } catch (InterruptedException e) { }

        }

        unequalized = newImage;
        availableUnequalized = false;
        notifyAll();
    }

    public synchronized void putEqualized(BufferedImage newImage) {
        while (availableEqualized == true) {
            try {
                wait();
            } catch (InterruptedException e) { }

        }

        availableEqualized = true;
        unequalized = newImage;
        notifyAll();
    }

    public synchronized BufferedImage getEqualized() {
        while (availableEqualized == true) {
            try {
                wait();
            } catch (InterruptedException e) { }

        }

        availableEqualized = false;
        notifyAll();
        return equalized;
    }
}

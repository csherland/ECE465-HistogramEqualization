/**
 * HistogramWorkerEQ.java
 *  A worker runnable that accepts image data over a socket and performs histogram
 *  equalization on incoming data. Returns equalized images over the same socket.
 *
 *  @author Christian Sherland
 *  @author Ethan Lusterman
 *  @author Michael Scibor
 *
 *  @version 1.0 Mar 6 2014
 */

package edu.cooper.ece465;

import java.util.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HistogramWriter implements Runnable {

    private int expected;
    private int sentBack;
    private ObjectOutputStream output;
    private ArrayList<SerialBufferedImage> imgBuffer;
    private static Log LOG = LogFactory.getLog(HistogramWorkerEQ.class);

    public HistogramWriter(ArrayList<SerialBufferedImage> imgBuffer, ObjectOutputStream out, int numImages) {
        LOG.info("HistogramWriter created.");
        this.imgBuffer = imgBuffer;
        this.output = out;
        this.expected = numImages;
        this.sentBack = 0;
    }

    @Override
    public void run() {
        while(sentBack < expected) {
            try {
                if(!imgBuffer.isEmpty()) {
                    SerialBufferedImage sendBack = imgBuffer.get(0);
                    imgBuffer.remove(0);
                    output.writeObject(sendBack);
                    LOG.info("Sent back img " + (sentBack+1));
                    sentBack++;
                } else {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
            //shhhhh
            }
        }

        try {
            Thread.sleep(100000);
        } catch (Exception e) {}
    }
}

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

public class HistogramWorkerEQ implements Runnable {

    private Socket socket;
    private int imageNumber;
    private BufferedImage unequalizedImage;
    private String imageName;
    private ArrayList<SerialBufferedImage> imgBuffer;
    private static Log LOG = LogFactory.getLog(HistogramWorkerEQ.class);

    public HistogramWorkerEQ(ArrayList<SerialBufferedImage> imgBuffer, int imageNumber, BufferedImage unequalizedImage, String imgName) {
        LOG.info("Thread created to equalize image: " + (imageNumber + 1.0000001) + " of " + HistogramWorker.imageCount);
        this.socket = socket;
        this.imageNumber = imageNumber;
        this.unequalizedImage = unequalizedImage;
        this.imageName = imgName;
        this.imgBuffer = imgBuffer;
    }

    @Override
    public void run() {
        BufferedImage equalizedImage = HistogramEqualization.computeHistogramEQ(unequalizedImage);
        LOG.info("Image " + imageNumber + " of " + HistogramWorker.imageCount + " equalized successfully.");
        imgBuffer.add(new SerialBufferedImage(equalizedImage, imageName));
    }
}

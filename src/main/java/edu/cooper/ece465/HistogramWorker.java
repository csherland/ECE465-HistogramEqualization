/**
 * HistogramWorker.java
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HistogramWorker implements Runnable {

    private Socket socket;
    private static Log LOG = LogFactory.getLog(HistogramWorker.class);
    public static Integer imageCount;

    public HistogramWorker(Socket s) {
        LOG.info("New histogram worker spawned.");
        socket = s;
    }

    public void run() {
        try {
            // Get input data stream
            ObjectInputStream input   = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            LOG.info("Reading images from client.");

            // Determine number of images to accept
            imageCount = (Integer) input.readObject();
            LOG.info("Expecting " + imageCount + " images from client.");

            Thread imgThreads[] = new Thread[imageCount];
            // Read in all expected images
            for (int i = 0; i < imageCount; i++) {
                SerialBufferedImage receivedImage = (SerialBufferedImage) input.readObject();
                LOG.info("Received image " + i + " from client.");
                Runnable equalizerThread = new HistogramWorkerEQ(output, i, receivedImage.getImage(), receivedImage.getName());
                imgThreads[i] = new Thread(equalizerThread);
                imgThreads[i].start();
            }

            for (Thread thread : imgThreads) {
                thread.join();
            }

            socket.close();
            LOG.info("Finished equalizing images.");
        } catch (IOException e) {
            LOG.error("IO Exception", e);
        } catch (ClassNotFoundException e) {
            LOG.error("Class not found error", e);
        } catch (Exception e) {
            LOG.error("Unexpected exception:", e);
        }
    }
}

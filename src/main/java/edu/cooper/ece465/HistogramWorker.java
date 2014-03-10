/**
 * HistogramWorker.java
 *  A worker runnable that accepts image data over a socket and
 *  performs histogram equalization on incoming data. Returns
 *  equalized images over the same socket.
 *
 *  @author Christian Sherland
 *  @author Ethan Lusterman
 *  @author Michael Scibor
 *
 *  @version 1.0 Mar 6 2014
 */

package edu.cooper.ece465;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HistogramWorker implements Runnable {

    private Socket socket;
    private static Log LOG = LogFactory.getLog(HistogramWorker.class);

    public HistogramWorker(Socket s) {
        LOG.info("New histogram worker spawned.");
        socket = s;
    }

    public void run() {
        try {
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            LOG.info("Reading images from client.");

            Integer imageCount = (Integer) input.readObject();
            LOG.info("Expecting " + imageCount + " images from client.");
            BufferedImage[] unequalizedImages = new BufferedImage[imageCount];
            BufferedImage[] equalizedImages = new BufferedImage[imageCount];

            for (int i = 0; i < imageCount; i++) {
                SerialBufferedImage receivedImage = (SerialBufferedImage) input.readObject();
                unequalizedImages[i] = receivedImage.get();
                LOG.info("Received image " + i + " from client.");
            }

            LOG.info("Images received.");

            for (int i = 0; i < imageCount; i++) {
                LOG.info("Equalizing image " + (i + 1) + " of " + imageCount);
                equalizedImages[i] = HistogramEqualization.computeHistogramEQ(unequalizedImages[i]);
            }

            LOG.info("All images equalized. Now sending images back to client.");
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

            for (BufferedImage equalized : equalizedImages) {
                SerialBufferedImage sendImage = new SerialBufferedImage(equalized);
                output.writeObject(sendImage);
            }

            output.close();
            input.close();
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

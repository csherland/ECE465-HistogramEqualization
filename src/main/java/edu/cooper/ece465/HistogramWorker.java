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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HistogramWorker implements Runnable {

    private Socket socket;
    private static Log LOG = LogFactory.getLog(HistogramWorker.class);

    public HistogramWorker(Socket s) {
        LOG.info("New histogram worker spawned");
        socket = s;
    }

    public void run() {
        try {
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            LOG.info("Reading images from client.");

            Integer imageCount = (Integer) input.readObject();
            BufferedImage[] unequalizedImages = new BufferedImage[imageCount];
            BufferedImage[] equalizedImages = new BufferedImage[imageCount];

            for (int i = 0; i < unequalizedImages.length; i++) {
                LOG.info("Received a new image.");
                unequalizedImages[i] = ImageIO.read(socket.getInputStream());
            }

            for (int i = 0; i < unequalizedImages.length; i++) {
                LOG.info("Equalizing image " + (i+1) + " of " + unequalizedImages.length);
                equalizedImages[i] = HistogramEqualization.computeHistogramEQ(unequalizedImages[i]);
            }

            for (BufferedImage equalizedImage : equalizedImages) {
                ImageIO.write(equalizedImage, "PNG", socket.getOutputStream());
            }

            input.close();
            socket.close();
        } catch (IOException e) {
            LOG.error("IO Exception", e);
        } catch (ClassNotFoundException e) {
            LOG.error("Class not found error", e);
        }

        LOG.info("Finished equalizing images.");
    }

}

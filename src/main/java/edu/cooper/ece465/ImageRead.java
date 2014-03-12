/**
 * Client.java
 *  Client objects have images that need to be Equalized: they send a request
 *  to the LoadBalancer for an available Server, which can handle the equalization.
 *  The LoadBalancer then connects the Client to this available Server.
 *
 *  Accepts command line arguments to setup connection with a load balancer and
 *  specify image input source and output location.
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
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImageRead implements Runnable {

    private static Log LOG = LogFactory.getLog(ImageRead.class);
    private Socket socket;
    private String outputDir;
    private int numImgs;

    public ImageRead(Socket s, String outputDir, int numImgs) {
        this.socket    = s;
        this.outputDir = outputDir;
        this.numImgs   = numImgs;
    }

    @Override
    public void run() {

        try {
            // Wait for equalized images from Server
            LOG.info("Waiting for equalized images.");
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            for (int i = 0; i < numImgs; i++) {
                SerialBufferedImage receivedImage = (SerialBufferedImage) input.readObject();
                BufferedImage equalizedImage = receivedImage.getImage();
                String imageName = receivedImage.getName();
                LOG.info("Received image " + (i+1) + " of " + numImgs);

                File file = new File(outputDir + "/"+ imageName +".png");
                if (!file.exists()) {
                    file.createNewFile();
                }

                ImageIO.write(equalizedImage, "png", file);
            }
            
            input.close();
        } catch (IOException e) {
            LOG.fatal("IO exception", e);
            System.exit(1);
        } catch (Exception e) {
            LOG.fatal("Unexpected exception", e);
            System.exit(1);
        }
    }
}

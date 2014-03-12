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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ImageWrite implements Runnable {

    // Array of supported image extensions
    private static final String[] EXTENSIONS = new String[]{
            "png", "jpg"
    };

    // Filter to identify images based on their extensions
    private static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(final File dir, final String name) {
            for (final String ext : EXTENSIONS) {
                if (name.endsWith("." + ext)) {
                    return (true);
                }
            }
            return (false);
        }
    };

    private static Log LOG = LogFactory.getLog(ImageWrite.class);
    private Socket socket;
    private String sourceDir;

    public ImageWrite(Socket s, String sourceDir) {
        this.socket = s;
        this.sourceDir = sourceDir;
    }

    @Override
    public void run() {
        try {
            // Send the number of images to be processed to the server
            File[] files = new File(sourceDir).listFiles(IMAGE_FILTER);
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(files.length);

            LOG.info("Reading " + files.length + " image files from directory:" + sourceDir);
            for (int i = 0; i < files.length; i++) {
                BufferedImage unequalizedImage = ImageIO.read(files[i]);
                SerialBufferedImage unequalizedSerial = new SerialBufferedImage(unequalizedImage);
                output.writeObject(unequalizedSerial);
            }
            output.close();
        } catch (IOException e) {
            LOG.fatal("IO exception", e);
            System.exit(1);
        } catch (Exception e) {
            LOG.fatal("Unexpected exception", e);
            System.exit(1);
        }

    }
}

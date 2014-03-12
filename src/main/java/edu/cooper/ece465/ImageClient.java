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

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ImageClient {

    // Array of supported image extensions
    static final String[] EXTENSIONS = new String[]{
            "png", "jpg"
    };

    // Filter to identify images based on their extensions
    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
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

    private static Log LOG = LogFactory.getLog(ImageClient.class);

    public static void main(String[] args) {

        if (args.length != 4) {
            LOG.fatal("Usage: java ImageClient <host> <port> <input directory> <output directory>");
            System.exit(1);
        }

        final String LOAD_BALANCER_NAME = args[0];
        final int LOAD_BALANCER_PORT = Integer.parseInt(args[1]);
        final String INPUT_DIRECTORY = args[2];
        final String OUTPUT_DIRECTORY = args[3];

        LOG.info("Image client running with load balancer name:\t "
                + LOAD_BALANCER_NAME + "\n\t\t port: "
                + LOAD_BALANCER_PORT + "\n\t\t input dir: "
                + INPUT_DIRECTORY + "\n\t\t output dir: "
                + OUTPUT_DIRECTORY);

        String histServerName = null;
        Integer histServerPort = null;

        try {
            LOG.info("Getting server assignment from load balancer.");
            Socket socket = new Socket(LOAD_BALANCER_NAME, LOAD_BALANCER_PORT); // Open a connection to the LoadBalancer
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            histServerName = (String) input.readObject();
            histServerPort = (Integer) input.readObject();

            input.close();
            socket.close(); // Close connection to LoadBalancer after you have retrieved necessary information
        } catch (IOException e) {
            LOG.fatal("Socket failed to initialize.");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            LOG.fatal("ClassNotFoundException.", e);
            System.exit(1);
        }

        // Connects to a Server and send the image over for equalization. Then retrieve image.
        try {
            LOG.info("Connecting to histogram server: " + histServerName + " on port: " + histServerPort);
            Socket socket = new Socket(histServerName, histServerPort);

            //// Send the number of images to be processed to the server
            File[] files = new File(INPUT_DIRECTORY).listFiles(IMAGE_FILTER);
            int numImgs = files.length;

            Runnable imgWrite = new ImageWrite(socket, INPUT_DIRECTORY);
            Runnable imgRead = new ImageRead(socket, OUTPUT_DIRECTORY, numImgs);
            (new Thread(imgWrite)).start();
            (new Thread(imgRead)).start();

        } catch (UnknownHostException e) {
            LOG.fatal("Unknown host", e);
            System.exit(1);
        } catch (IOException e) {
            LOG.fatal("IO exception", e);
            System.exit(1);
        } catch (Exception e) {
            LOG.fatal("Unexpected exception", e);
            System.exit(1);
        }

//        LOG.info("Received all equalized images and saved output to directory: " + OUTPUT_DIRECTORY);
    }
}
/*
 * ImageClient.java
 *
 */

package ece465.cooper.edu;
import java.util.*;
import java.net.*;
import java.awt.image.*;
import java.awt.*;
import com.flickr4java.flickr.*;

public class ImageClient {

    public ImageClient() {
        System.out.println("Image Client Initialized.");
    }

    public static void main(String[] args) {

        /*
         * Connect to flickr
         *  TODO: Change the key to be read from properties file and
         *          add to .gitignore
         */
        String apiKey = "66f8567513744d2afa1019d05be88a17";
        String secret = "30406da39c66d4af";
        Flickr f = new Flickr(apiKey, secret, new REST());

        /*
         * Get a bunch of images
         */
        Vector<BufferedImage> images = new Vector<BufferedImage>();
        for (int i = 0; i < 3; ++i) {
            BufferedImage currentImage = ImageIO.read(Files.newInputStream(Paths.get(basePath + imageSource)));
            images.add(currentImage);
        }

        /*
         * Set up socket connection
         */
        int portNumber = 1992;
        String hoseName = "localhost";
        Socket imageSocket = new Socket(hostName, portNumber);

        PrintWriter out = new PrintWriter(imageSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(imageSocket.getInputStream()));

        /*
         * Send images to be equalized
         */
        for (int i = 0; i < images.size(); ++i) {

        }

        /*
         * Get back equalized images
         * and save to disk
         */
        for (int i = 0; i < images.size(); ++i) {
            ImageIO.write(equalized, "JPG", out);
        }


    }

}

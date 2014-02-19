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

        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setAccuracy(10);
        searchParameters.setBBox(minimum_longitude,
                minimum_latitude,
                maximum_longitude,
                maximum_latitude);
        PhotoList<Photo> list = flickr.getPhotosInterface().search(searchParameters, 0, 0);

        /*
         * Get a bunch of images
         */
        Vector<BufferedImage> images = new Vector<BufferedImage>();
        for (int i = 0; i < photos.size(); ++i) {
            BufferedImage currentImage = photos.get(i).getMediumImage();
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
            ImageIO.write(images.get(i), "JPG", in);
        }

        /*
         * Get back equalized images
         * and save to disk
         */
        for (int i = 0; i < images.size(); ++i) {
            BufferedImage equalied = ImageIO.read(out);
        }


    }

}

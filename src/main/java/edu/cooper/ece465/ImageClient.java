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

    public static int main(String[] args) {

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


        /*
         * Send them to the server
         */


        /*
         * Get back equalized images
         */


        return 0;
    }

}

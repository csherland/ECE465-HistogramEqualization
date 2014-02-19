/*
 * ImageClient.java
 *
 */

package ece465.cooper.edu;
import java.util.*;
import java.net.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;

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
        PhotoList<Photo> photos = new PhotoList<Photo>();
        try {
            photos = f.getPhotosInterface().search(searchParameters, 0, 0);
        } catch(Exception e) {
            System.out.println("Could not search for photos");
        }

        /*
         * Get a bunch of images (1 for now)
         */
        Vector<BufferedImage> images = new Vector<BufferedImage>();
        for (int i = 0; i < photos.size(); ++i) {
            //BufferedImage currentImage = photos.get(i).getMediumImage();
            try {
                String imageURL = photos.get(i).getUrl();
                images.add(ImageIO.read(new URL(imageURL)));
            } catch (Exception e) {
                System.out.println("could not get image url");
            }
        }

        /*
         * Set up socket connection
         */
        int portNumber = 1992;
        String hostName = "localhost";
        try {
            Socket imageSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(imageSocket.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println("error connecting to host");
        }
        //BufferedReader in = new BufferedReader(new InputStreamReader(imageSocket.getInputStream()));

        /*
         * Send images to be equalized
         */
        for (int i = 0; i < images.size(); ++i) {
            //ImageIO.write(images.get(i), "JPG", in);
        }

        /*
         * Get back equalized images
         * and save to disk
         */
        for (int i = 0; i < images.size(); ++i) {
            //BufferedImage equalized = ImageIO.read(in);
        }


    }

}

/*
 * Authors: Christian Sherland
 *          Ethan Lusterman
 *          Michael Scibor
 *
 * Date:    2/21/14
 *
 * ImageClient.java
 *      Takes a set of images and sends them to a
 *      server that applies histogram equalization.
 *
 *      Saves the output in formate "{originalname}_new.jpg"
 *
 */

package ece465.cooper.edu;
import java.util.*;
import java.net.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class ImageClient {

    public ImageClient() {
        System.out.println("Image Client Initialized.");
    }

    public static void main(String[] args) {

        /*
         * Get specified images for equalization
         * TODO:
         *      - create a good way to specify images
         */
        BufferedImage unequalized;
        try {
            String imageName = "test1.png";
            unequalized = ImageIO.read(new File(imageName));
        } catch (Exception e) {
            System.out.println("Error reading input image");
        }

        /*
         * Get connection from LB to a hist equalization server
         *
         * TODO:
         *      - make this configurable in a properties file
         */
        int portNumber = 1992;
        String hostName = "localhost";

        try {
            // Setup connection to LB
            Socket lbSocket = new Socket(hostName, portNumber);
            InputStream input = lbSocket.getInputStream();
            ObjectInputStream objectInput = new ObjectInputStream(input);

            // Ask for connection to hist equalization server

            // Close connections that we don't need anymore
            objectInput.close();
            input.close();
            lbSocket.close();

        } catch (Exception e) {
            System.out.println("Error getting connection to hist equalization server");
        }

        /*
         * Connect to histogram equalization server and
         * send data to be equalized
         *
         */
        try {
            // Setup connection to hist worker
            Socket histSocket = new Socket(hostName, portNumber);
            DataInputStream in = new DataInputStream(histSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(histSocket.getOutputStream());

            //Do work with the pictures


            // Close connections we're done with
            in.close();
            out.close();
            histSocket.close();

            // Write equalized images back to disk


        } catch (Exception e) {
            System.out.println("Error communicating with equalization server");
        }

    }

}

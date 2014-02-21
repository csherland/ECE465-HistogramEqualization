
/*
 * Authors: Christian Sherland
 *          Ethan Lusterman
 *          Michael Scibor
 *
 * Date:    2/18/14
 *
 * ImageProducer.java
 *      Responsible for producing data to be sorted by consumers
 *
 */

package edu.cooper.ece465;
import java.util.*;
import java.io.*;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageProducer extends Thread {
    private ImageHelper helper;

    public ImageProducer(ImageHelper c) {
        this.helper = c;
    }

    public void run() {

        // Read input
        BufferedImage img = null;
        try {
           img = ImageIO.read(new File("src/main/resources/image1.jpg"));
        } catch (IOException e) {
            System.out.println("Image not found");
        }

        helper.putUnequalized(img);
        img = helper.getEqualized();
        helper.setDoneEqualizing(true);

        System.out.println("Image put.");

        /*
        try {
            File outputFile = new File("output.jpg");
            ImageIO.write(img, "jpg", outputFile);
        } catch (IOException e) {
        }
        */
    }
}

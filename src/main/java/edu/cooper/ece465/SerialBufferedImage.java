/**
 * SerialBufferedImage.java
 *   Because buffered images are not serializable, they cannot be send over object
 *   streams. This class implements a serializable buffered image to enable that
 *   functionality.
 *
 *  @author Christian Sherland
 *  @author Ethan Lusterman
 *  @author Michael Scibore
 *
 *  @version 1.0 Mar 10 2014
 */

package edu.cooper.ece465;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class SerialBufferedImage implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient BufferedImage image = null;

    private String name;
    private int imageWidth;
    private int imageHeight;
    private int[][] pixelArray;

    public SerialBufferedImage(BufferedImage image, String imgName) {
        setImage(image);
        this.name = imgName;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();

        pixelArray = new int[imageWidth][imageHeight];
        for (int x = 0; x < imageWidth; x++)
            for (int y = 0; y < imageHeight; y++)
                pixelArray[x][y] = image.getRGB(x, y);
    }

    public BufferedImage getImage() {
        if (image == null) {
            image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < imageWidth; x++)
                for (int y = 0; y < imageHeight; y++)
                    image.setRGB(x, y, pixelArray[x][y]);
        }
        return image;
    }

    public void setName(String name) {}
        this.name = name;
    }

    public void getName() {
        return name;
    }

}

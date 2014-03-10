package edu.cooper.ece465;

import java.awt.image.BufferedImage;
import java.io.Serializable;

// BufferedImages are not Serializable, use this to store a BufferedImage as a
// Serializable object, with appropriate getter and setter methods.

public class SerialBufferedImage implements Serializable {
    private static final long serialVersionUID = 1L;

    private transient BufferedImage image = null;

    private int imageWidth;
    private int imageHeight;
    private int[][] pixelArray;

    public SerialBufferedImage(BufferedImage image) {
        set(image);
    }

    public void set(BufferedImage image) {
        this.image = image;

        imageWidth = image.getWidth();
        imageHeight = image.getHeight();

        pixelArray = new int[imageWidth][imageHeight];

        setPixelArray();
    }

    public BufferedImage get() {
        if (image == null) {
            image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            for (int x = 0; x < imageWidth; x++)
                for (int y = 0; y < imageHeight; y++)
                    image.setRGB(x, y, pixelArray[x][y]);
        }
        return image;
    }

    private void setPixelArray() {
        for (int x = 0; x < imageWidth; x++)
            for (int y = 0; y < imageHeight; y++)
                pixelArray[x][y] = image.getRGB(x, y);
    }

}

package edu.cooper.ece465;
import java.util.*;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class ImageConsumer extends Thread {
    private ImageHelper protocol;
    private HistogramEQ eq;

    public ImageConsumer(ImageHelper protocol) {
        this.protocol = protocol;
        this.eq = new HistogramEQ();
    }

    public void run() {
        BufferedImage img;

        while (true) {
            img = protocol.getUnequalized();
            if (protocol.getDoneEqualizing() && img == null) {
                break;
            }

            try {
                eq.equalize(img);
            } catch (IOException e) {
                System.out.println("Error equalizing image.");
            }
            System.out.println("Image equalized.");
//            protocol.putEqualized(img);
        }

    }
}

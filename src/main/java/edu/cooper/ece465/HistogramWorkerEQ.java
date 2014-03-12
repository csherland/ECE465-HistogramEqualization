package edu.cooper.ece465;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class HistogramWorkerEQ implements Runnable {

    private Socket socket;
    private int imageNumber;
    private BufferedImage unequalizedImage;
    private String imageName;
    private ObjectOutputStream output;
    private static Log LOG = LogFactory.getLog(HistogramWorkerEQ.class);

    public HistogramWorkerEQ(ObjectOutputStream out, int imageNumber, BufferedImage unequalizedImage, String imgName) {
        LOG.info("Thread created to equalize image: " + imageNumber + " of " + HistogramWorker.imageCount);
        this.socket = socket;
        this.imageNumber = imageNumber;
        this.unequalizedImage = unequalizedImage;
        this.imageName = imgName;
        this.output = out;
    }

    @Override
    public void run() {
        try {
            BufferedImage equalizedImage = HistogramEqualization.computeHistogramEQ(unequalizedImage);
            LOG.info("Image " + imageNumber + " of " + HistogramWorker.imageCount + " equalized successfully.");

            output.writeObject(new SerialBufferedImage(equalizedImage, imageName));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

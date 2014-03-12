package edu.cooper.ece465;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WorkerWrite implements Runnable {

    private Socket socket;
    private int imageNumber;
    private BufferedImage unequalizedImage;
    private static Log LOG = LogFactory.getLog(WorkerWrite.class);

    public WorkerWrite(Socket socket, int imageNumber, BufferedImage unequalizedImage) {
        LOG.info("Thread created to equalize image: " + imageNumber + " of " + HistogramWorker.imageCount);
        this.socket = socket;
        this.imageNumber = imageNumber;
        this.unequalizedImage = unequalizedImage;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            BufferedImage equalizedImage = HistogramEqualization.computeHistogramEQ(unequalizedImage);
            LOG.info("Image " + imageNumber + " of " + HistogramWorker.imageCount + " equalized successfully.");

            SerialBufferedImage sendImage = new SerialBufferedImage(equalizedImage);
            output.writeObject(sendImage);

            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

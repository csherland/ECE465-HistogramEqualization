/*
 *
 * HistogramWorker.java
 *  Performs equalization on a specified image in a thread
 *
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class HistogramWorker implements Runnable {

    private Socket socket;

    public HistogramWorker(Socket s) {
        System.out.println("New histogram worker spawned");
        socket = s;
    }

    // Image equalization
    public void run() {
        try {
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            System.out.println("Input stream open: Server side.");

            Integer imageCount = (Integer) input.readObject();
            BufferedImage[] unequalizedImages = new BufferedImage[imageCount];
            BufferedImage[] equalizedImages = new BufferedImage[imageCount];

            for (BufferedImage unequalizedImage : unequalizedImages) {
                unequalizedImage = ImageIO.read(socket.getInputStream());
                System.out.println("Received a new image.");
                // TODO: Spawn new threads for Equalizing images while still receiving new ones
            }

            for (int i = 0; i < unequalizedImages.length; i++) {
                System.out.println("Equalizing image " + (i+1) + " of " + unequalizedImages.length);
                equalizedImages[i] = HistogramEqualization.computeHistogramEQ(unequalizedImages[i]);
            }

            for (BufferedImage equalizedImage : equalizedImages) {
                ImageIO.write(equalizedImage, "PNG", socket.getOutputStream());
            }

            input.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Something went wrong");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}

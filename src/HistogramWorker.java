/**
 * HistogramWorker.java
 *  A worker runnable that accepts image data over a socket and
 *  performs histogram equalization on incoming data. Returns 
 *  equalized images over the same socket.
 *
 *  @author Christian Sherland
 *  @author Ethan Lusterman
 *  @author Michael Scibor
 *
 *  @version 1.0 Mar 6 2014
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

    public void run() {
        try {
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            System.out.println("Input stream open: Server side.");

            Integer imageCount = (Integer) input.readObject();
            BufferedImage[] unequalizedImages = new BufferedImage[imageCount];
            BufferedImage[] equalizedImages = new BufferedImage[imageCount];

            for (int i = 0; i < unequalizedImages.length; i++) {
                unequalizedImages[i] = ImageIO.read(socket.getInputStream());
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

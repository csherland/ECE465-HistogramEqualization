/*
 *
 * HistogramWorker.java
 *  Performs equalization on a specified image in a thread
 *
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
            BufferedImage uneq = null;

            DataInputStream input = new DataInputStream(socket.getInputStream());
            System.out.println("Input stream open: Server side.");

            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            System.out.println("Output stream open: Server side.");

            while (true) {
                uneq = ImageIO.read(socket.getInputStream());
                if (uneq != null) {
                    break;
                }
            }
            System.out.println("received image. equalizing...");

            BufferedImage eqd = HistogramEqualization.computeHistogramEQ(uneq);
            ImageIO.write(eqd, "PNG", socket.getOutputStream());

            output.close();
            input.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
    }

}

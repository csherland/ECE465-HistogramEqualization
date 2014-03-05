/*
 * Client.java
 *  Client objects have images that need to be Equalized: they send a request
 *  to the LoadBalancer for an available Server, which can handle the equalization.
 *  The LoadBalancer then connects the Client to this available Server.
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ImageClient {
    public static void main(String[] args) {

        if (args.length != 3) {
            System.err.println(
                    "Usage: java ImageClient <host> <port> <filename>");
            System.exit(1);
        }

        String loadBalancerName = args[0];
        int loadBalanacerPort   = Integer.parseInt(args[1]);
        String imageDirectory   = args[2];

        String hostNameServer    = null;
        Integer portNumberServer = null;

        try {
            Socket socket = new Socket(loadBalancerName, loadBalanacerPort); // Open a connection to the LoadBalancer
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            hostNameServer   = (String)  input.readObject();
            portNumberServer = (Integer) input.readObject();

            input.close();
            socket.close(); // Close connection to LoadBalancer after you have retrieved necessary information
        } catch (IOException e) {
            System.out.println("Socket failed to initialize.");
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.err.println("ImageClient: ClassNotFoundException.");
            System.exit(1);
        }

        // Connects to a Server and send the image over for equalization. Then retrieve image.
        try {
            Socket socket = new Socket(hostNameServer, portNumberServer);

            File[] files = new File(imageDirectory).listFiles();
            BufferedImage[] unequalizedImages = new BufferedImage[files.length];
            BufferedImage[] equalizedImages = new BufferedImage[files.length];

            // Instantiate the array of unequalized images
            for (int i = 0; i < files.length; i++) {
                unequalizedImages[i] = ImageIO.read(files[i]);
            }

            // Send the number of images to be processed to the server
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            output.writeObject(unequalizedImages.length);

            // Send images to the Server
            for (BufferedImage unequalizedImage : unequalizedImages) {
                ImageIO.write(unequalizedImage, "PNG", socket.getOutputStream());
            }

            // Wait for equalized images from Server
            int imageCount = 1;
            for (BufferedImage equalizedImage : equalizedImages) {
                equalizedImage = ImageIO.read(socket.getInputStream());
                File file = new File("images-equalized/img-" + imageCount + ".png");
                ImageIO.write(equalizedImage, "png", file);
                imageCount++;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

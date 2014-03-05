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
import java.net.Socket;
import java.net.UnknownHostException;

public class ImageClient {
    public static void main(String[] args) {
        String hostName = null;
        Integer portNumber = null;

        // First try{}...catch gets a hostName and portNumber to an available Server from LoadBalancer
        try {
            Socket socket = new Socket("localhost", 2014); // Open a connection to the LoadBalancer
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());


            hostName = (String) input.readObject();
            portNumber = (Integer) input.readObject();

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
            Socket socket = new Socket(hostName, portNumber);

            BufferedImage unequalized = null;
            BufferedImage equalized = null;
            String imageName = "img.png";

            // TODO: Here we can do something with an array of images instead to try to work the Server
            unequalized = ImageIO.read(new File(imageName));

            // Send image to the Server
            ImageIO.write(unequalized, "PNG", socket.getOutputStream());

            // Wait for equalized image from Server
            equalized = ImageIO.read(socket.getInputStream());

            File file = new File(imageName.substring(0, imageName.length() - 4) + "-eq.png");
            ImageIO.write(equalized, "png", file); // Write image to file
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

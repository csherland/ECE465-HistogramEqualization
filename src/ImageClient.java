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

        if (args.length != 3) {
            System.err.println(
                    "Usage: java ImageClient <host> <port> <filename>");
            System.exit(1);
        }

        String hostNameServer = null;
        Integer portNumberServer = null;

        // First try{}...catch gets a hostName and portNumber to an available Server from LoadBalancer
        try {
            Socket socket = new Socket(args[0], Integer.parseInt(args[1])); // Open a connection to the LoadBalancer
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

            hostNameServer = (String) input.readObject();
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

            BufferedImage unequalized = null;
            BufferedImage equalized = null;
            String imageName = args[2];

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

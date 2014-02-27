/*
 * Client.java
 *  Client objects have images that need to be Equalized: they send a request
 *  to the LoadBalancer for an available Server, which can handle the equalization.
 *  The LoadBalancer then connects the Client to this available Server.
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

public class ImageClient {
    public static void main(String[] args) {

        // read image
        BufferedImage uneq = null;
        BufferedImage eqd = null;
        String imageName = "img.png";
        try {
            uneq = ImageIO.read(new File(imageName));
            System.out.println("image loaded");
        } catch (IOException e) {
            System.out.println("image not loaded");
            e.printStackTrace();
        }

        // open socket connection and send data / listen for data
        Socket socket = null;
        try {
            socket = new Socket("localhost", 9999);

            DataInputStream input = new DataInputStream(socket.getInputStream());
            System.out.println("Input stream open: Client side.");

            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            System.out.println("Output stream open: Client side.");

            // send img file over socket
            ImageIO.write(uneq, "PNG", socket.getOutputStream());

            while (true) {
                eqd = ImageIO.read(socket.getInputStream());
                if (eqd != null) {
                    break;
                }
            }

            System.out.println("received eqd image. writing to file...");
            File file = new File(imageName.substring(0,imageName.length()-4)+"-eq.png");
            ImageIO.write(eqd, "png", file);

            output.close();
            input.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Socket failed to initialize.");
        }
    }

}

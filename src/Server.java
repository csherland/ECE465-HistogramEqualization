
/* Once connected with a Client, the Server handles Equalization. */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {

        BufferedImage uneq = null;

        // open connection on socket
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            Socket socket = serverSocket.accept();

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

            BufferedImage eqd = Equalization.computeHistogramEQ(uneq);

            ImageIO.write(eqd, "PNG", socket.getOutputStream());

            output.close();
            input.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("ServerSocket not functional.");
        }
    }
}

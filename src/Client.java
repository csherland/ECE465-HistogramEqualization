import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/* Client objects have images that need to be Equalized: they send a request
 * to the LoadBalancer for an available Server, which can handle the equalization.
 * The LoadBalancer then connects the Client to this available Server.
 */

public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 9999);

            DataInputStream input = new DataInputStream(socket.getInputStream());
            System.out.println("Input stream open: Client side.");

            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            System.out.println("Output stream open: Client side.");

            output.writeBytes("HELLO\nJust in case.\nJust in case. \n Though, just in case.\n");

            output.close();
            input.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Socket failed to initialize.");
        }
    }

}

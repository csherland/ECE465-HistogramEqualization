import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/* Client objects have images that need to be Equalized: they send a request
 * to the LoadBalancer for an available Server, which can handle the equalization.
 * The LoadBalancer then connects the Client to this available Server.
 */

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 2014);

        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        System.out.println("Input stream open: Client side.");

        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Output stream open: Client side.");

        output.writeBytes("HELLO\nJust in case.\nJust in case. \n Though, just in case.\n");

        output.close();
        input.close();
        socket.close();
    }

}

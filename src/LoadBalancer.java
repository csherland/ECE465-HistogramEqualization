
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/* LoadBalancer receives requests from Client objects to be connected to an
 * available Server. Tells the Client which Server is available. */

public class LoadBalancer {
    public static void main(String[] args){
        try {
            ServerSocket serverSocket = new ServerSocket(2014);
            while(true){
                System.out.println("New Connection established.");
                Socket socket = serverSocket.accept();

            }
        } catch (IOException e) {
            System.out.println("ServerSocket not established successfully.");
            System.exit(1);
        }
    }
}

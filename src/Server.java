
/* Once connected with a Client, the Server handles Equalization. */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(2014);
            Socket socket = serverSocket.accept();

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            System.out.println("Input stream open: Server side.");

            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Output stream open: Server side.");

            while (true) {

                String ourMessage = input.readLine();
                System.out.println(ourMessage);
                if (ourMessage.equals("Just in case.")){
                    break;
                }
            }

            output.close();
            input.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("ServerSocket not functional.");
        }
    }
}

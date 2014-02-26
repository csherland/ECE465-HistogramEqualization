
/* Once connected with a Client, the Server handles Equalization. */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            Socket socket = serverSocket.accept();

            DataInputStream input = new DataInputStream(socket.getInputStream());
            System.out.println("Input stream open: Server side.");

            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
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

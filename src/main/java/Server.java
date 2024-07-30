import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;//listen for incoming connections or clients and creating a socket object to communicate with them

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {// to make the server run indefinitely  until the server socket is closed
                Socket socket = serverSocket.accept();//waiting for a client to connect , this is a blocking method, and it returns a socket object
                System.out.println("A new client has connected! ");
                ClientHandler clientHandler = new ClientHandler(socket);//TODO class not created yet
                //ClientHandler implements runnable , therefore objects made from that class can run on a new thread so we pass it into a Thread object

                Thread thread = new Thread(clientHandler);
                thread.start();
            }


        } catch (IOException e) {
        }
    }


    public void closedServerSocket() {//this method shuts down the server if an error occurs
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}

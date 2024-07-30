import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();//keeps track of all our clients
    private Socket socket;
    private BufferedReader bufferedReader;//read and receive data
    private BufferedWriter bufferedWriter;//send data
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(OutputStreamWriter(socket.getOutputStream()));//send
            this.bufferedReader = new BufferedReader(InputStreamReader(socket.getInputStream()));//read
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER" + clientUsername + "has entered the chat!");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {

    }
}

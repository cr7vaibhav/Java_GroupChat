import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

  public static ArrayList<ClientHandler> clientHandlers =
      new ArrayList<>(); // keeps track of all our clients
  private Socket socket;
  private BufferedReader bufferedReader; // read and receive data
  private BufferedWriter bufferedWriter; // send data
  private String clientUsername;

  public ClientHandler(Socket socket) {
    try {
      this.socket = socket;
      this.bufferedWriter =
          new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // send
      this.bufferedReader =
          new BufferedReader(new InputStreamReader(socket.getInputStream())); // read
      this.clientUsername = bufferedReader.readLine();
      clientHandlers.add(this);
      broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");
    } catch (IOException e) {
      closeEverything(socket, bufferedReader, bufferedWriter);
    }
  }

  @Override
  public void
      run() { // everything runs on a separate thread , what we want to do is listen for messages on
    // a thread , since it's a blocking operation therefore we will have a separate thread
    // waiting for messages and another for the rest of the application
    String messageFromClient;

    while (!(socket.isClosed())) {
      try {
        messageFromClient =
            bufferedReader
                .readLine(); // we want to run this on a separate thread so that the rest of the
        // program doesn't stop because of this line of code here
        broadcastMessage(messageFromClient);
      } catch (IOException e) {
        closeEverything(socket, bufferedReader, bufferedWriter);
        break; // without break this loop doesn't stop
      }
    }
  }

  public void broadcastMessage(String messagetoSend) {
    for (ClientHandler clientHandler : clientHandlers) {
      try {
        if (!clientHandler.clientUsername.equals(clientUsername)) {
          clientHandler.bufferedWriter.write(messagetoSend);
          clientHandler.bufferedWriter.newLine();
          clientHandler.bufferedWriter.flush();
        }

      } catch (IOException e) {
        closeEverything(socket, bufferedReader, bufferedWriter);
      }
    }
  }

  public void removeClientHandler() {
    clientHandlers.remove(this);
    broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
  }

  public void closeEverything(
      Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
    removeClientHandler();
    try {
      if (bufferedReader != null) {
        bufferedReader.close();
      }

      if (bufferedWriter != null) {
        bufferedWriter.close();
      }

      if (socket != null) {
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

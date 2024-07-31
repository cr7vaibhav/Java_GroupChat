import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
  private Server server;
  private ServerSocket serverSocket;

  @BeforeEach
  public void beforeEachTest() throws IOException {
    // create a real ServerSocket for testing
    serverSocket = new ServerSocket(0); // port 0 to find any free ports
    server = new Server(serverSocket);
  }

  @Test
  void testServerStart() throws IOException {
    Thread serverThread=new Thread(()-> server.startServer());
    serverThread.start();

    //Create a client to connect to the server
    Socket clientSocket=new Socket("localhost",serverSocket.getLocalPort());
    assertTrue(clientSocket.isConnected());

    //Stop the server
    server.closedServerSocket();;
    assertTrue(serverSocket.isClosed());

    clientSocket.close();
  }

  @Test
  void testServerSocketClosed() {
    server.closedServerSocket();
    assertTrue(serverSocket.isClosed());
  }
}

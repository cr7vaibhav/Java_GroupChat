import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClientHandlerTest {
  private Socket socket;
  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
  private ClientHandler clientHandler;

  @BeforeEach
  public void beforeEachTest() throws IOException {
    socket = new Socket("localhost", 1234); // Ensure this port is available
    OutputStream outputStream = new ByteArrayOutputStream();
    InputStream inputStream = new ByteArrayInputStream("testUser\n".getBytes());

    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

    clientHandler = new ClientHandler(socket);
    clearClientHandlers(); // Clear the list before each test
  }

  @AfterEach
  public void afterEachTest() throws IOException {
    socket.close();
    bufferedReader.close();
    bufferedWriter.close();
    clearClientHandlers();
  }

  // Using reflection to access private fields in test cases without modifying access levels
  private void clearClientHandlers() throws IOException {
    try {
      Field clientHandlersField = ClientHandler.class.getDeclaredField("clientHandlers");
      clientHandlersField.setAccessible(true);
      ((ArrayList<ClientHandler>) clientHandlersField.get(null)).clear();
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private String getClientUsername() {
    try {
      Field clientUsernameField = ClientHandler.class.getDeclaredField("clientUsername");
      clientUsernameField.setAccessible(true);
      return (String) clientUsernameField.get(clientHandler);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }
  }

  private ArrayList<ClientHandler> getClientHandlers() {
    try {
      Field clientHandlersField = ClientHandler.class.getDeclaredField("clientHandlers");
      clientHandlersField.setAccessible(true);
      return (ArrayList<ClientHandler>) clientHandlersField.get(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Test
  void testClientHandlerInit() throws IOException {
    assertEquals(1, getClientHandlers().size());
    assertEquals("testUser", getClientUsername());
  }

  @Test
  void testbroadcastMessage() throws IOException {
    ClientHandler anotherClientHandler=new ClientHandler(socket);

    clientHandler.broadcastMessage("Test 12345, 321!");

    //simulate broadcast message
    anotherClientHandler.bufferedWriter.flush();
    anotherClientHandler.bufferedWriter.close();

    assertTrue(getClientHandlers().contains(anotherClientHandler));
  }

  @Test
  void removeClientHandler() {}

  @Test
  void closeEverything() {}
}

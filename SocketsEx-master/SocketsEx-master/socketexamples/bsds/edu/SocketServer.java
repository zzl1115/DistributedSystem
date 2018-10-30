package socketexamples.bsds.edu;

/**
 *
 * @author Ian Gortan
 */


import java.net.ServerSocket;
import java.net.Socket;



public class SocketServer {
  public static void main(String[] args) throws Exception {
    ServerSocket m_ServerSocket = new ServerSocket(12031);
    ActiveCount threadCount = new ActiveCount();
    int id = 0; 
    System.out.println("Server started .....");
    while (true) {
      Socket clientSocket = m_ServerSocket.accept();
      SocketHandlerThread server = new SocketHandlerThread (clientSocket, threadCount);
      server.start();
     
    }
  }
}


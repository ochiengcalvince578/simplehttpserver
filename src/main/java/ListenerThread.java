import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenerThread extends Thread {


    private final static Logger LOGGER = LoggerFactory.getLogger(ListenerThread.class);
    private ServerSocket serverSocket;

    private int port;

    public ListenerThread(int port) throws IOException {
        this.port = port;

        this.serverSocket =  new ServerSocket(this.port);
    }

    @Override
    public void run() {

            try {

                while (serverSocket.isBound() && !serverSocket.isClosed()) {

                    Socket clientSocket = serverSocket.accept();
                    LOGGER.info(" * connection accepted: " + clientSocket.getInetAddress());

                    HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(clientSocket);

                   workerThread.start();
                }

                //serverSocket.close(); //TODO handle close

            } catch (Exception e) {

                e.printStackTrace();
                //TODO handle ex
                LOGGER.error("something happened", e);
            } finally {
              if (serverSocket != null) {
                  try {
                      serverSocket.close();
                      LOGGER.info("server connection closed");
                  } catch (IOException ignored) {
                  }
              }
            }
    }
}

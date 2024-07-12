import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class HttpConnectionWorkerThread extends Thread{

    private Socket clientSocket;

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    public HttpConnectionWorkerThread(Socket clientSocket) {

        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

         InputStream in = null;
         OutputStream outputStream = null;

       try {
           in = clientSocket.getInputStream();

           outputStream = clientSocket.getOutputStream();

           // our simple server only uses the first line from the request
//           String request = in.readLine();

           int _byte;

           while ((_byte = in.read()) >= 0) {

               System.out.print((char) _byte);
           }

//           String response = httpParser(request);

           //outputStream.write(response.getBytes());

           in.close();
           outputStream.close();
           clientSocket.close();

           try {
               sleep(5000);
           } catch (Exception e) {
               LOGGER.error("problem with connection", e);
           }
       }
       catch (Exception e) {
           LOGGER.error("problem with connection", e);
       } finally {

           if (in != null) {

               try {
                   in.close();
               } catch (IOException ignored) {}
           }

           if (outputStream != null) {
               try {
                   outputStream.close();
               } catch (IOException ignored) {}
           }

           if (clientSocket != null) {
               try {
                   clientSocket.close();
               } catch (IOException ignored) {}
           }

       }
    }

    private String httpParser (String request) {


        String[] requestArray = request.split(" ");

        String method = "GET";

        String version = "HTTP/1.1";

        String path = "/";

        String status = "200 OK";

        for (int i = 0; i < requestArray.length; i++) {

            switch (requestArray[i]) {
                case "POST" -> method = "POST";
                case "PUT" -> method = "PUT";
                case "DELETE" -> method = "DELETE";
            }

            if (requestArray[i].matches("HTTP/\\d+.\\d+")) {

                version = requestArray[i];
                path = requestArray[i - 1];
            }
        }

        String CRLF = "\n\r";

        String html = "<html><head><title>wahoo!!</title></head><body><p>wahoo is the internet front</p></body></html>";


        return version + " " + status + CRLF +
                "Content-Length: " + html.getBytes().length + CRLF +
                CRLF +
                html +
                CRLF +
                CRLF;
    }
}

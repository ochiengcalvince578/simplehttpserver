package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; //32

    private static final int CR = 0x0D; //13

    private static final int LF = 0x0A; //10

    public HttpRequest parseHttpRequest (InputStream inputStream) throws HttpParsingException {

        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest request = new HttpRequest();


     try {
         parseRequestLine(reader, request);
     }
     catch (IOException | HttpParsingException e) {
         e.printStackTrace();
     }

     parseHeaders(reader, request);

     parseBody(reader, request);

     return request;
    }

    private void parseBody(InputStreamReader reader, HttpRequest request) {
    }

    private void parseHeaders(InputStreamReader reader, HttpRequest request) {
    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {


      StringBuilder processingDataBuffer = new StringBuilder();

      boolean methodParsed = false;
      boolean requestTargetParsed = false;


      int _byte;
      while ((_byte = reader.read()) >= 0) {

          if (_byte == CR) {
              _byte = reader.read();

              if (_byte == LF) {

                  LOGGER.debug("Request line VERSION to process : {}", processingDataBuffer.toString());

                  return;
              }
          }

          if (_byte == SP) {

              if (!methodParsed) {

                  LOGGER.debug("Request line METHOD to process : {}", processingDataBuffer.toString());
                  request.setMethod(processingDataBuffer.toString());
                  methodParsed = true;
              }

              else if (!requestTargetParsed) {

                  LOGGER.debug("Request line REQ TARGET to process : {}", processingDataBuffer.toString());
                  requestTargetParsed = true;
              }

              processingDataBuffer.delete(0,processingDataBuffer.length());
          }

          else {

              processingDataBuffer.append((char)_byte);

              if(!methodParsed) {
                  if (processingDataBuffer.length() > HttpMethod.MAX_LENGTH) {
                      throw new HttpParsingException(HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
                  }
              }


          }


      }


    }


}

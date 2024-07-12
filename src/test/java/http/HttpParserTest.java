package http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    //object scope variable
    HttpParser httpParser;

    @BeforeAll
    public void beforeClass () {

        httpParser = new HttpParser();
    }

    @org.junit.jupiter.api.Test
    void parseHttpRequest() {

        httpParser.parseHttpRequest(generateValidTestCase());

    }

    private InputStream generateValidTestCase () {

       String rawData = "GET / HTTP/1.1\r\n" +
               "Host: localhost:8082\r\n" +
               "Connection: keep-alive\r\n" +
               "Cache-Control: max-age=0\r\n" +
               "sec-ch-ua: \"Chromium\";v=\"122\", \"Not(A:Brand\";v=\"24\", \"Google Chrome\";v=\"122\"\r\n" +
               "sec-ch-ua-mobile: ?0\r\n" +
               "sec-ch-ua-platform: \"Linux\"\r\n" +
               "Upgrade-Insecure-Requests: 1\r\n" +
               "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36\r\n" +
               "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\r\n" +
               "Sec-Fetch-Site: none\r\n" +
               "Sec-Fetch-Mode: navigate\r\n" +
               "Sec-Fetch-User: ?1\r\n" +
               "Sec-Fetch-Dest: document\r\n" +
               "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
               "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8,sw;q=0.7\r\n" +
               "\r\n";

               InputStream inputStream = new ByteArrayInputStream(
                       rawData.getBytes(StandardCharsets.US_ASCII)
               );
               return  inputStream;
    }
}
package http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    //object scope variable
    HttpParser httpParser;

    @BeforeAll
    public void beforeClass () {

        httpParser = new HttpParser();
    }

    @Test
    void parseHttpRequest() {

        HttpRequest request = null;

        try {
            request = httpParser.parseHttpRequest(generateValidGetTestCase());
        }
        catch (HttpParsingException e) {

            fail(e);
        }

        assertNotNull(request);
        assertEquals(request.getMethod(), HttpMethod.GET);
        assertEquals(request.getRequestTarget(), "/");
        assertEquals(request.getOriginalHttpVersion(), "HTTP/1.1");
        assertEquals(request.getBestCompatibleHttpVersion(), HttpVersion.HTTP_1_1);

    }

    @Test
    void parseHttpRequestBadMethodName1()  {

        try {
            HttpRequest request = httpParser.parseHttpRequest(generateBadGetTestCase());
            fail();
        } catch (HttpParsingException e) {

            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
    void parseHttpRequestBadMethodName2() {

        HttpRequest request = null;
        try {
            request = httpParser.parseHttpRequest(generateBadGetTestCase2());
            //LOGGER.debug("request method added : {}", request.getMethod());
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }

    }

    @Test
    void parseHttpRequestInvNumItems1() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadTestCaseRequestLineInvNumItems()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHttpRequestEmptyRequestLine() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadTestCaseEmptyRequestLine()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHttpRequestOnlyCRnoLF() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadTestCaseRequestLineOnlyCRnoLF()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHttpRequestBadHttpVersion() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateBadHttpVersion()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHttpRequestUnsupportedHttpVersion() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateUnsupportedHttpVersion()
            );
            fail();
        } catch (HttpParsingException e) {
            assertEquals(e.getErrorCode(), HttpStatusCode.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }


    @Test
    void parseHttpRequestSupportedHttpVersion() {
        try {
            HttpRequest request = httpParser.parseHttpRequest(
                    generateSupportedHttpVersion()
            );
            assertNotNull(request);
            assertEquals(request.getBestCompatibleHttpVersion(), HttpVersion.HTTP_1_1);
            assertEquals(request.getOriginalHttpVersion(), "HTTP/1.2");
        } catch (HttpParsingException e) {
            fail();
        }
    }


    private InputStream generateValidGetTestCase () {

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


    private InputStream generateBadGetTestCase () {

        String rawData = "GeT / HTTP/1.1\r\n" +
                "Host: localhost:8082\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8,sw;q=0.7\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return  inputStream;
    }

    private InputStream generateBadGetTestCase2 () {

        String rawData = "GETTTTTT / HTTP/1.1\r\n" +
                "Host: localhost:8082\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8,sw;q=0.7\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return  inputStream;
    }

    private InputStream generateBadTestCaseRequestLineInvNumItems () {

        String rawData = "GET / AAAAA HTTP/1.1\r\n" +
                "Host: localhost:8082\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8,sw;q=0.7\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return  inputStream;
    }

    private InputStream generateBadTestCaseEmptyRequestLine () {

        String rawData = "\r\n" +
                "Host: localhost:8082\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8,sw;q=0.7\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return  inputStream;
    }

    private InputStream generateBadTestCaseRequestLineOnlyCRnoLF () {

        String rawData = "GET / HTTP/1.1\r" + // <----- no LF
                "Host: localhost:8082\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8,sw;q=0.7\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return  inputStream;
    }

    private InputStream generateBadHttpVersion () {

        String rawData = "GET / HTTP1.1\r" +
                "Host: localhost:8082\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8,sw;q=0.7\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return  inputStream;
    }

    private InputStream generateUnsupportedHttpVersion () {

        String rawData = "GET / HTTP/2.1\r\n" +
                "Host: localhost:8082\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Accept-Encoding: gzip, deflate, br, zstd\r\n" +
                "Accept-Language: en-GB,en-US;q=0.9,en;q=0.8,sw;q=0.7\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(StandardCharsets.US_ASCII)
        );
        return  inputStream;
    }

    private InputStream generateSupportedHttpVersion () {

        String rawData = "GET / HTTP/1.2\r\n" +
                "Host: localhost:8082\r\n" +
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
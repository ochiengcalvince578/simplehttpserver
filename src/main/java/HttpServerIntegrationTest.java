import httpServer.config.Configuration;
import httpServer.config.ConfigurationManager;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;


public class HttpServerIntegrationTest {

    @Test
    public void CorrectResponseOnGetRequestWithoutVerb () throws IOException {

        Client client = new Client();
        client.startConnection("127.0.0.1", 8082);

        String response = client.sendMessage("GET / HTTP/1.1");

//        assertEquals("", response);

        System.out.println("response is " + response);

    }

//    @Test
//    public  void CorrectPortExposedonServerStart () {
//
//        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
//
//        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();
//
//        ListenerThread listenerThread = new ListenerThread(conf.getPort());
//    }

}

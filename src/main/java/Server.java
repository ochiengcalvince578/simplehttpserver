import httpServer.config.Configuration;
import httpServer.config.ConfigurationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

   private final static Logger LOOGER = LoggerFactory.getLogger(Server.class);


    public static void main (String[] args) {

        //Server server = new Server();

        try {


            ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");

            Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

            ListenerThread listenerThread = new ListenerThread(conf.getPort());
            LOOGER.info("server listening on port " + conf.getPort());
            listenerThread.start();

        }

        catch (Exception e) {

            throw new RuntimeException(e);
        }

    }
}

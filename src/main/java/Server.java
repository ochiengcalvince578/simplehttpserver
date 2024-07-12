import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

   private final static Logger LOOGER = LoggerFactory.getLogger(Server.class);


    public static void main (String[] args) {

        //Server server = new Server();

        try {

            ListenerThread listenerThread = new ListenerThread(8082);
            LOOGER.info("server listening on port " + 8082);
            listenerThread.start();

        }

        catch (Exception e) {

            throw new RuntimeException(e);
        }

    }
}

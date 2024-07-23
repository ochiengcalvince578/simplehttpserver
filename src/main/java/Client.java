import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;

    private PrintWriter out;

    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException {

        socket = new Socket(ip, port);

        out = new PrintWriter(socket.getOutputStream(), true);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String sendMessage (String msg) throws IOException {

        out.println(msg);

        return in.readLine();
    }

    public void stopConnection () throws IOException {

        in.close();
        out.close();

        socket.close();

    }

//    public static void main (String[] args) throws IOException {
//
//        Client client = new Client();
//
//        client.startConnection("127.0.0.1", 8082);
//
////        System.out.println("Enter your request: ");
////
////        Scanner scanner = new Scanner(System.in);
////
////        String msg = scanner.nextLine();
////
////        client.sendMessage(msg);
////
////        scanner.close();
//
//    }
}

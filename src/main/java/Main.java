
public class Main {
    public static final int PORT = 8989;

    public static void main(String[] args) throws Exception {

        Server server = new Server(PORT);
        server.startServer();
    }
}
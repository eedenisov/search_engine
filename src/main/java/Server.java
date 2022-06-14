import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server {
    private final int port;
    private final BooleanSearchEngine engine;

    public Server(int port) throws IOException {
        this.port = port;
        engine = new BooleanSearchEngine(new File("pdfs"));
    }

    public static class Request {
        String word;

        public Request(String word) {
            this.word = word;
        }

        @Override
        public String toString() {
            return "Request{" +
                    "word='" + word + '\'' +
                    '}';
        }
    }

    public void startServer() {
        System.out.println("Сервер стартовал");

        try (ServerSocket socket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = socket.accept();
                try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(
                             new BufferedWriter(
                                     new OutputStreamWriter(clientSocket.getOutputStream())), true)) {

                    String answer = in.readLine();
                    Request request = new Gson().fromJson(answer, Request.class);
                    List<PageEntry> result = engine.search(request.word);

                    String gsonStr = listToJson(result);
                    out.println(gsonStr);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String listToJson(List<PageEntry> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();

        return gson.toJson(list);
    }
}


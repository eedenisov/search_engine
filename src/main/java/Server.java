import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
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
                    String json = in.readLine();

                    Request request = new Gson().fromJson(json, Request.class);
                    if (request.word != null && !request.word.isEmpty()) {
                        List<PageEntry> result = engine.search(request.word);
                        String j = (String) listToJson(result);

                        String nameFile = "file.json";
                        writeToJson(j, nameFile);

                        out.println(j);
                    } else {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> Object listToJson(List<T> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Type listT = new TypeToken<List<T>>() {
        }.getType();

        return gson.toJson(list, listT);
    }

    private static void writeToJson(String json, String jsonFile) {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write(json);
            writer.append("\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


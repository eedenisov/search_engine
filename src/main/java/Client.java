import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String host = "eedenisov";

        try (Socket client = new Socket(host, Main.PORT);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(
                     new BufferedWriter(
                             new OutputStreamWriter(client.getOutputStream())), true)) {

            out.println("Бизнес");
            String s;
            for (int i = 0; (s = in.readLine()) != null; i++) {
                System.out.println(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

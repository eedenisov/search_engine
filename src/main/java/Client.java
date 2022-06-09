import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    public static void main(String[] args) {
        String host = "eedenisov";


        try (Socket client = new Socket(host, Main.PORT);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(client.getInputStream()));
             PrintWriter out = new PrintWriter(
                     new BufferedWriter(
                             new OutputStreamWriter(client.getOutputStream())), true)) {
            out.println("{\"word\": \"Бизнес\"}");
            String json = in.readLine();
            String result = jsonToList(json);
            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String rowFormatJson(List<PageEntry> list) {
        String s = list.toString();
        int level = 0;
        StringBuilder jsonFormatStr = new StringBuilder();
        for (var i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (level > 0 && '\n' == jsonFormatStr.charAt(jsonFormatStr.length() - 1)) {
                jsonFormatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                case ',':
                    jsonFormatStr.append(c).append("\n");
                    break;
                case '}':
                case ']':
                    jsonFormatStr.append("\n");
                    level--;
                    jsonFormatStr.append(getLevelStr(level));
                    jsonFormatStr.append(c);
                    break;
                default:
                    jsonFormatStr.append(c);
                    break;
            }
        }
        return jsonFormatStr.toString();
    }

    public static String getLevelStr(int level) {
        StringBuilder levelStr = new StringBuilder();
        for (var i = 0; i < level; i++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

    public static String jsonToList(String json) {
        List<PageEntry> list = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(json);
            JSONArray jsonArray = (JSONArray) obj;

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            for (Object object : jsonArray) {
                JSONObject jsonObject = (JSONObject) object;
                PageEntry pageEntry = gson.fromJson(String.valueOf(jsonObject), PageEntry.class);
                list.add(pageEntry);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return rowFormatJson(list);
    }
}

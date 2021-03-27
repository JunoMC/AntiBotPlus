package dev.junomc.antibotplus.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestUtils {
    private String APIKey;
    private String IP;

    public RequestUtils(String APIKey, String IP) {
        this.APIKey = APIKey;
        this.IP = IP;
    }

    public JsonObject response() {
        JsonObject object = null;

        try {
            StringBuilder result = new StringBuilder();

            URL url = new URL("https://www.iphunter.info:8082/v1/ip/1.53.165.31");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-Key", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.WzE2MDMsMTYxNjgxNDI3MiwyMDAwXQ.5iZq3nNUoKzMpiQyd4rwplxxAtE9ic2ZqNwHaBSFF7k");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                for (String line; (line = reader.readLine()) != null;) {
                    result.append(line);
                }
            }

            JsonParser parser = new JsonParser();
            object = parser.parse(result.toString()).getAsJsonObject();

            System.out.println(result);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }
}

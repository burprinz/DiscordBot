package de.luiskun.apis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.luiskun.log.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class Requester {

    private static final OkHttpClient client = new OkHttpClient();

    /**
     * Requestet dinge von der Riot-API
     * @param url die request-URL
     * @return das JSON-Objekt
     */
    public static JsonObject request(String url) {
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String json = response.body().string();
                return JsonParser.parseString(json).getAsJsonObject();
            } else {
                Log.error("Error getting infos: " + response.body().string());
                return null;
            }
        } catch (IOException e) {
            Log.error("Could not retrieve data: " + e.getMessage());
            return null;
        }
    }

    /**
     * Requestet dinge von der Riot-API
     * @param url die request-URL
     * @return das JSON-Objekt
     */
    public static String requestRawJson(String url) {
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                Log.error("Error getting infos: " + response.body().string());
                return null;
            }
        } catch (IOException e) {
            Log.error("Could not retrieve data: " + e.getMessage());
            return null;
        }
    }

    /**
     * Requestet dinge von der Riot-API
     * @param url die request-URL
     * @return das JSON-Objekt
     */
    public static JsonObject requestAsList(String url) {
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String json  = "{list:" + response.body().string() + "}";
                return JsonParser.parseString(json).getAsJsonObject();
            } else {
                Log.error("Error getting infos: " + response.body().string());
                return null;
            }
        } catch (IOException e) {
            Log.error("Could not retrieve data: " + e.getMessage());
            return null;
        }
    }
}

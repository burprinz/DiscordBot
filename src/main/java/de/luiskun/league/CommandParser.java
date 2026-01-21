package de.luiskun.league;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.luiskun.log.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CommandParser {

    public static final String apiKey = "RGAPI-757c657c-6b18-4f50-ac27-d68f29b4f7d3";

    private static final OkHttpClient client = new OkHttpClient();


    public static final JsonObject GetAccountInfosByRiotID(String riotID) {
        File[] retrievedData = new File(DataPaths.LEAGUE_ACCOUNTS).listFiles();
        for (File file : retrievedData) {
            if (file.getName().equals(riotID + ".json")) {
                try {
                    return JsonParser.parseString(new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())))).getAsJsonObject();
                } catch (Exception e) {
                    Log.error("Getting saved Account infos of " + riotID + " failed. Trying to download new.");
                }
            }
        }

        String[] spl = riotID.split("#");
        String name = spl[0];
        String tagline = spl[1];
        String url = HTTPLinks.ACCOUNT_BY_RIOT_ID + name + "/" + tagline + "?api_key=" + apiKey;
        Log.debug("GetAccountInfosByRiotID: " + url);

        Request request = new Request.Builder().url(url).build();
        String json = null;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                json = response.body().string();
                try (PrintWriter out = new PrintWriter(DataPaths.LEAGUE_ACCOUNTS + "/" + riotID + ".json")) {
                    out.println(json);
                }
            } else {
                Log.error("Error getting Account infos: " + response.body().string());
                return null;
            }

        } catch (IOException e) {
            Log.error("Could not retrieve Account data: " + e.getMessage());
            return null;
        }
        return JsonParser.parseString(json).getAsJsonObject();
    }


    public static JsonObject GetChampionMasteriesByPUUID(String puuid) {
        String url = HTTPLinks.MASTERIES_BY_PUUID + puuid + "?api_key=" + apiKey;
        Log.debug("GetChampionMasteriesByPUUID: " + url);

        Request request = new Request.Builder().url(url).build();
        String json = null;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                json = response.body().string();
                json = "{list:" + json + "}";
            } else {
                Log.error("Error getting Mastery infos: " + response.body().string());
                return null;
            }

        } catch (IOException e) {
            Log.error("Could not retrieve Mastery data: " + e.getMessage());
            return null;
        }
        return JsonParser.parseString(json).getAsJsonObject();
    }


}

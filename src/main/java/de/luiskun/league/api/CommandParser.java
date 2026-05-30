package de.luiskun.league.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.luiskun.league.DataPaths;
import de.luiskun.league.objects.AccountInfo;
import de.luiskun.league.objects.Match;
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

    public static final String apiKey = "RGAPI-61144ced-332d-408e-870d-2e2e71a4a426";

    private static final OkHttpClient client = new OkHttpClient();

    private static Gson gson = new Gson();

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
        return request(url);
    }

    public static JsonObject request(String url) {
        Request request = new Request.Builder().url(url).build();
        String json = null;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                json = response.body().string();
                json = "{list:" + json + "}";
            } else {
                Log.error("Error getting infos: " + response.body().string());
                return null;
            }

        } catch (IOException e) {
            Log.error("Could not retrieve data: " + e.getMessage());
            return null;
        }
        return JsonParser.parseString(json).getAsJsonObject();
    }

    public static JsonObject requestLast100MatchIDsByPUUID(String puuid) {
        String url = HTTPLinks.MATCH_HISTORY_BY_PUUID + puuid + "/ids?start=0&count=100&api_key=" + CommandParser.apiKey;
        Request request = new Request.Builder().url(url).build();
        String json;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                json = response.body().string();
                json = "{list:" + json + "}";
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return JsonParser.parseString(json).getAsJsonObject();
    }

    public static JsonObject requestMatchInformationByMatchID(String matchID) {
        String url = HTTPLinks.MATCH_DATA_BY_MATCH_ID + matchID + "?api_key=" + CommandParser.apiKey;
        Request request = new Request.Builder().url(url).build();
        String json;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                json = response.body().string();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return JsonParser.parseString(json).getAsJsonObject();
    }

    public static AccountInfo downloadAccountInfosByPuuid(String puuid) {
        String url = HTTPLinks.ACCOUNT_BY_PUUID + puuid + "?api_key=" + apiKey;
        System.out.println("GetAccountInfosByPUUID: " + url);

        Request request = new Request.Builder().url(url).build();
        String json = null;
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                json = response.body().string();
            } else {
                Log.error("Error getting Account infos: " + response.body().string());
                return null;
            }

        } catch (IOException e) {
            Log.error("Could not retrieve Account data: " + e.getMessage());
            return null;
        }

        return gson.fromJson(json, AccountInfo.class);
    }


}

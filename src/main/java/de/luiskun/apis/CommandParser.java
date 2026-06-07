package de.luiskun.apis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.luiskun.APIKeys;
import de.luiskun.league.DataPaths;
import de.luiskun.league.objects.AccountInfo;
import de.luiskun.league.objects.MatchIDList;
import de.luiskun.log.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CommandParser {

    private static final OkHttpClient client = new OkHttpClient();

    private static Gson gson = new Gson();

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

    /**
     * Liest die Accountinformationen von einem League-Account aus
     * @param riotID name#EUW
     * @return die Informationen als JSON-Objekt
     */
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
        String url = HTTPLinks.LEAGUE_ACCOUNT_BY_RIOT_ID + name + "/" + tagline + "?api_key=" + APIKeys.RIOT_API_KEY;
        Log.debug("GetAccountInfosByRiotID: " + url);

        String json = requestRawJson(url);
        if (json != null) {
            try (PrintWriter out = new PrintWriter(DataPaths.LEAGUE_ACCOUNTS + "/" + riotID + ".json")) {
                out.println(json);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return JsonParser.parseString(json).getAsJsonObject();
        }
        return null;
    }

    /**
     * Liest die aktuellen Champion-Masteries eines Spielers aus
     * @param puuid die ID des spielers
     * @return die Mastery Levels als JSON-Objekt
     */
    public static JsonObject GetChampionMasteriesByPUUID(String puuid) {
        String url = HTTPLinks.LEAGUE_MASTERIES_BY_PUUID + puuid + "?api_key=" + APIKeys.RIOT_API_KEY;
        Log.debug("GetChampionMasteriesByPUUID: " + url);
        return requestAsList(url);
    }

    /**
     * Gibt eine Liste mit den Match-IDs der letzen 100 Matches eines Spielers
     * @param puuid die ID des Spielers
     * @return die Liste als JSON-Objekt
     */
    public static JsonObject requestLast100MatchIDsByPUUID(String puuid) {
        String url = HTTPLinks.LEAGUE_MATCH_HISTORY_BY_PUUID + puuid + "/ids?start=0&count=100&api_key=" + APIKeys.RIOT_API_KEY;
        return requestAsList(url);
    }

    /**
     * Gibt die genauen Informationen zu einem Match
     * @param matchID die MatchID
     * @return die Informationen als JSON-Objekt
     */
    public static JsonObject requestMatchInformationByMatchID(String matchID) {
        String url = HTTPLinks.LEAGUE_MATCH_DATA_BY_MATCH_ID + matchID + "?api_key=" + APIKeys.RIOT_API_KEY;
        return request(url);
    }

    /**
     * Liest die Accountinformationen von einem League-Account aus
     * @param puuid die ID des Accounts
     * @return die Informationen
     */
    public static AccountInfo downloadAccountInfosByPuuid(String puuid) {
        String url = HTTPLinks.LEAGUE_ACCOUNT_BY_PUUID + puuid + "?api_key=" + APIKeys.RIOT_API_KEY;
        System.out.println("GetAccountInfosByPUUID: " + url);

        String json = requestRawJson(url);
        if  (json != null) {
            return gson.fromJson(json, AccountInfo.class);
        } else {
            return null;
        }
    }

    /**
     * List die Matches aus, welche bereits fehlerhaft heruntergeladen wurden.
     * @return die Fehlerhaften Matches
     */
    public static List<String> getFaultyMatchIDs() {
        File faultyMatchesFile = new File(DataPaths.FAULTY_MATCHES);
        if (!faultyMatchesFile.exists()) {
            return new ArrayList<String>();
        }
        try {
            String json = new String(Files.readAllBytes(Paths.get(DataPaths.FAULTY_MATCHES)));
            ObjectMapper m = new ObjectMapper();
            return m.readValue(json, MatchIDList.class).getMatchIDs();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<String>();
    }

    /**
     * Speichert eine neue fehlerhafte MatchID
     * @param faultyMatchIDs die ID
     */
    public static void saveFaultyMatchIDs(List<String> faultyMatchIDs) {
        File faultyFile = new File(DataPaths.FAULTY_MATCHES);
        MatchIDList matchIDList = new MatchIDList();
        matchIDList.setMatchIDs(faultyMatchIDs);
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(faultyFile, matchIDList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

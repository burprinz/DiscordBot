package de.luiskun.league;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.luiskun.log.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LeagueAPI {

    public static String getPUUIDByRiotID(String riotID) {
        JsonObject obj = CommandParser.GetAccountInfosByRiotID(riotID);
        if (obj == null) {
            return null;
        }
        return obj.get("puuid").getAsString();
    }

    public static Mastery[] getMasteriesByPUUID(String puuid) {

        JsonObject obj = CommandParser.GetChampionMasteriesByPUUID(puuid);

        List<JsonElement> array = obj.get("list").getAsJsonArray().asList();
        Mastery[] result = new Mastery[array.size()];

        for (int i = 0; i < array.size(); i++) {
            JsonObject mastery = array.get(i).getAsJsonObject();
            int id = mastery.get("championId").getAsInt();
            result[i] = new Mastery(puuid, id, getChampionNameById(id),
                    mastery.get("championLevel").getAsInt(),
                    mastery.get("championPoints").getAsInt(),
                    mastery.get("lastPlayTime").getAsLong(),
                    mastery.get("championPointsSinceLastLevel").getAsInt(),
                    mastery.get("championPointsUntilNextLevel").getAsInt(),
                    mastery.get("tokensEarned").getAsInt());
        }

        return result;
    }


    public static Mastery getBestMasteryByPUUID(String puuid) {
        Mastery[] masteries =  getMasteriesByPUUID(puuid);
        if (masteries.length > 0) {
            return getMasteriesByPUUID(puuid)[0];
        } else {
            return null;
        }
    }

    public static String getChampionNameById(int id) {
        try {
            String json = new String(Files.readAllBytes(Paths.get("src/main/resources/leagueData/championIds.json")));
            JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
            return obj.get("list").getAsJsonObject().get(""+id).getAsString();
        } catch (IOException e) {
            Log.error("Error retrieving Champion name by ID: " + e.getMessage());
            return null;
        }
    }


}

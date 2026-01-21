package de.luiskun.testing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import de.luiskun.league.CommandParser;
import de.luiskun.league.DataPaths;
import de.luiskun.league.HTTPLinks;
import de.luiskun.league.objects.Match;
import de.luiskun.league.objects.MatchParticipant;
import de.luiskun.league.objects.MatchTeam;
import de.luiskun.league.objects.TeamObjective;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {

    String puuid = "0oLxnK8ztghtEmptbgodKAN7xlTBmlnhQ0gEoRM2yTUXXFdEUzOPXdwtP__o_tUso2r-ukU2boL2rA";

    private static final OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) {

        new Main();

    }

    public Main() {
        downloadEveryMapIfNotAlready(puuid);
    }


    // LEAGUE API
    public static void downloadEveryMapIfNotAlready(String puuid) {
        List<String> matchIDs = getLast100MatchIdsByPUUID(puuid);

        for (int i = 0; i < matchIDs.size(); i++) {
            String id = matchIDs.get(i);
            String filePath = DataPaths.LEAGUE_MATCHES + id + ".json";
            if (!Files.exists(Paths.get(filePath))) {
                Match m = getMatchInformationByMatchID(matchIDs.get(i));
                ObjectMapper om = new ObjectMapper();
                try {
                    om.writeValue(new File(filePath), m);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(i + " - downloaded");
            } else {
                System.out.println(i + " - already downloaded");
            }
        }
    }


    public static List<String> getLast100MatchIdsByPUUID(String puuid) {
        JsonObject json =  requestLast100MatchIDsByPUUID(puuid);
        if (json == null) {
            return null;
        }
        List<JsonElement> arr = json.get("list").getAsJsonArray().asList();
        List<String> result = new ArrayList<>();

        for (int i = 0; i < arr.size(); i++) {
            result.add(arr.get(i).getAsString());
        }
        return  result;
    }

    public static Match getMatchInformationByMatchID(String matchID) {
        JsonObject rawMatch = requestMatchInformationByMatchID(matchID);
        JsonObject rawMetadata = rawMatch.getAsJsonObject("metadata");
        JsonObject rawInfos = rawMatch.getAsJsonObject("info");

        Match result = new Match();
        result.matchID = rawMetadata.get("matchId").getAsString();
        result.gameID = rawInfos.get("gameId").getAsLong();
        result.region = rawInfos.get("platformId").getAsString();
        result.gameMode = rawInfos.get("gameMode").getAsString();
        result.gameName = rawInfos.get("gameName").getAsString();
        result.gameVersion = rawInfos.get("gameVersion").getAsString();
        result.mapID = rawInfos.get("mapId").getAsInt();
        result.startTime = rawInfos.get("gameStartTimestamp").getAsLong();
        result.endTime = rawInfos.get("gameEndTimestamp").getAsLong();
        result.gameduration = rawInfos.get("gameDuration").getAsInt();
        result.gameEndingType = rawInfos.get("endOfGameResult").getAsString();

        List<JsonElement> rawTeams = rawInfos.get("teams").getAsJsonArray().asList();
        List<MatchTeam> teams = new ArrayList<>();
        for (JsonElement t : rawTeams) {
            JsonObject obj = t.getAsJsonObject();
            MatchTeam team = new MatchTeam();
            team.winner = obj.get("win").getAsBoolean();

            List<JsonElement> rawBans = obj.get("bans").getAsJsonArray().asList();
            List<Integer> bans = new ArrayList<>();
            for (JsonElement b : rawBans) {
                bans.add(b.getAsJsonObject().get("championId").getAsInt());
            }
            team.bans = bans;

            team.objectives = new ArrayList<>();
            JsonObject rawObjectives = obj.getAsJsonObject("objectives");
            for (Map.Entry<String, JsonElement> e : rawObjectives.entrySet()) {
                String key = e.getKey();
                JsonObject value = e.getValue().getAsJsonObject();
                TeamObjective objective = new TeamObjective();
                objective.name = key;
                objective.first = value.get("first").getAsBoolean();
                objective.kills = value.get("kills").getAsInt();
                team.objectives.add(objective);
            }
            teams.add(team);
        }
        result.teams = teams;


        List<JsonElement> rawPlayers = rawInfos.get("participants").getAsJsonArray().asList();
        List<MatchParticipant> participants = new ArrayList<>();
        for (JsonElement e : rawPlayers) {
            JsonObject  obj = e.getAsJsonObject();
            MatchParticipant p = new MatchParticipant();

            p.puuid = obj.get("puuid").getAsString();
            p.basicPings = obj.get("basicPings").getAsInt();
            p.allInPings = obj.get("allInPings").getAsInt();
            p.assistMePings = obj.get("assistMePings").getAsInt();
            p.commandPings = obj.get("commandPings").getAsInt();
            p.dangerPings = obj.get("dangerPings").getAsInt();
            p.enemyMissingPings = obj.get("enemyMissingPings").getAsInt();
            p.enemyVisionPings = obj.get("enemyVisionPings").getAsInt();
            p.getBackPings = obj.get("getBackPings").getAsInt();
            p.holdPings = obj.get("holdPings").getAsInt();
            p.needVisionPings = obj.get("needVisionPings").getAsInt();
            p.onMyWayPings = obj.get("onMyWayPings").getAsInt();
            p.pushPings = obj.get("pushPings").getAsInt();
            p.retreatPings = obj.get("retreatPings").getAsInt();
            p.visionClearedPings = obj.get("visionClearedPings").getAsInt();
            p.spell1Casts = obj.get("spell1Casts").getAsInt();
            p.spell2Casts = obj.get("spell2Casts").getAsInt();
            p.spell3Casts = obj.get("spell3Casts").getAsInt();
            p.spell4Casts = obj.get("spell4Casts").getAsInt();
            p.item0 = obj.get("item0").getAsInt();
            p.item1 = obj.get("item1").getAsInt();
            p.item2 = obj.get("item2").getAsInt();
            p.item3 = obj.get("item3").getAsInt();
            p.item4 = obj.get("item4").getAsInt();
            p.item5 = obj.get("item5").getAsInt();
            p.item6 = obj.get("item6").getAsInt();
            p.championId = obj.get("championId").getAsInt();
            p.gameEndedInEarlySurrender = obj.get("gameEndedInEarlySurrender").getAsBoolean();
            p.gameEndedInSurrender = obj.get("gameEndedInSurrender").getAsBoolean();
            p.actualPosition = obj.get("lane").getAsString();
            p.preferredPosition = obj.get("individualPosition").getAsString();
            p.won =  obj.get("win").getAsBoolean();
            p.timePlayed = obj.get("timePlayed").getAsInt();
            p.totalTimeSpentDead = obj.get("totalTimeSpentDead").getAsInt();
            p.visionScore = obj.get("visionScore").getAsInt();
            p.wardsPlaced = obj.get("wardsPlaced").getAsInt();
            p.detectorWardsPlaced = obj.get("detectorWardsPlaced").getAsInt();
            p.wardsKilled = obj.get("wardsKilled").getAsInt();
            p.visionWardsBoughtInGame = obj.get("visionWardsBoughtInGame").getAsInt();
            p.sightWardsBoughtInGame = obj.get("sightWardsBoughtInGame").getAsInt();
            p.consumablesPurchased = obj.get("consumablesPurchased").getAsInt();
            p.itemsPurchased = obj.get("itemsPurchased").getAsInt();
            p.goldEarned = obj.get("goldEarned").getAsInt();
            p.goldSpent = obj.get("goldSpent").getAsInt();
            p.damageDealtToBuildings = obj.get("damageDealtToBuildings").getAsInt();
            p.damageDealtToObjectives = obj.get("damageDealtToObjectives").getAsInt();
            p.damageDealtToTurrets = obj.get("damageDealtToTurrets").getAsInt();
            p.magicDamageDealt = obj.get("magicDamageDealt").getAsInt();
            p.magicDamageDealtToChampions = obj.get("magicDamageDealtToChampions").getAsInt();
            p.magicDamageTaken = obj.get("magicDamageTaken").getAsInt();
            p.physicalDamageDealt = obj.get("physicalDamageDealt").getAsInt();
            p.physicalDamageDealtToChampions = obj.get("physicalDamageDealtToChampions").getAsInt();
            p.physicalDamageTaken = obj.get("physicalDamageTaken").getAsInt();
            p.totalDamageDealt = obj.get("totalDamageDealt").getAsInt();
            p.totalDamageDealtToChampions = obj.get("totalDamageDealtToChampions").getAsInt();
            p.totalDamageTaken = obj.get("totalDamageTaken").getAsInt();
            p.trueDamageDealt = obj.get("trueDamageDealt").getAsInt();
            p.trueDamageDealtToChampions = obj.get("trueDamageDealtToChampions").getAsInt();
            p.trueDamageTaken = obj.get("trueDamageTaken").getAsInt();
            p.timeCCingOthers = obj.get("timeCCingOthers").getAsInt();
            p.totalTimeCCDealt = obj.get("totalTimeCCDealt").getAsInt();
            p.totalHeal = obj.get("totalHeal").getAsInt();
            p.totalHealsOnTeammates = obj.get("totalHealsOnTeammates").getAsInt();
            p.kills = obj.get("kills").getAsInt();
            p.deaths = obj.get("deaths").getAsInt();
            p.assists = obj.get("assists").getAsInt();
            p.totalAllyJungleMinionsKilled = obj.get("totalAllyJungleMinionsKilled").getAsInt();
            p.totalEnemyJungleMinionsKilled = obj.get("totalEnemyJungleMinionsKilled").getAsInt();
            p.totalMinionsKilled = obj.get("totalMinionsKilled").getAsInt();
            p.turretKills = obj.get("turretKills").getAsInt();
            p.turretTakedowns = obj.get("turretTakedowns").getAsInt();
            p.turretsLost = obj.get("turretsLost").getAsInt();
            p.baronKills = obj.get("baronKills").getAsInt();
            p.dragonKills = obj.get("dragonKills").getAsInt();
            p.killingSprees = obj.get("killingSprees").getAsInt();
            p.doubleKills = obj.get("doubleKills").getAsInt();
            p.tripleKills = obj.get("tripleKills").getAsInt();
            p.quadraKills = obj.get("quadraKills").getAsInt();
            p.pentaKills = obj.get("pentaKills").getAsInt();
            p.unrealKills = obj.get("unrealKills").getAsInt();
            p.inhibitorKills = obj.get("inhibitorKills").getAsInt();
            p.inhibitorTakedowns = obj.get("inhibitorTakedowns").getAsInt();
            p.inhibitorsLost = obj.get("inhibitorsLost").getAsInt();
            p.largestCriticalStrike = obj.get("largestCriticalStrike").getAsInt();
            p.largestKillingSpree = obj.get("largestKillingSpree").getAsInt();
            p.largestMultiKill = obj.get("largestMultiKill").getAsInt();
            p.longestTimeSpentLiving = obj.get("longestTimeSpentLiving").getAsInt();
            p.firstBloodAssist = obj.get("firstBloodAssist").getAsBoolean();
            p.firstBloodKill = obj.get("firstBloodKill").getAsBoolean();
            p.firstTowerAssist = obj.get("firstTowerAssist").getAsBoolean();
            p.firstTowerKill = obj.get("firstTowerKill").getAsBoolean();
            participants.add(p);
        }
        result.matchParticipants = participants;
        return result;
    }


    // CommandParser
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

}

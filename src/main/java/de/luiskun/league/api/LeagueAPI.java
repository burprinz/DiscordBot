package de.luiskun.league.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.luiskun.league.DataPaths;
import de.luiskun.league.objects.*;
import de.luiskun.log.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeagueAPI {

    public static final int EXPIRED_API_KEY = -1;

    public static String getPUUIDByRiotID(String riotID) {
        JsonObject obj = CommandParser.GetAccountInfosByRiotID(riotID);
        if (obj == null) {
            return null;
        }
        return obj.get("puuid").getAsString();
    }

    public static Mastery[] getMasteriesByPUUID(String puuid) {

        JsonObject obj = CommandParser.GetChampionMasteriesByPUUID(puuid);

        if (obj == null) {
            return null;
        }

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
        if (masteries != null && masteries.length > 0) {
            return masteries[0];
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

    public static void downloadMatchesFromRegisteredUsers() {

        Log.info("Starting to download matches from registered users");

        String[] ids = getRegisteredUserIds();

        int downloadCount = 0;
        for (String puuid : ids) {
            int c = downloadEveryMapIfNotAlready(puuid);
            if (c < 0) {
                switch (c) {
                    case EXPIRED_API_KEY:
                        Log.error("Invalid or expired API-Key!");
                        return;
                }
            }
            downloadCount += c;
        }
        Log.success("Downloaded " + downloadCount + " matches");
    }

    public static String[] getRegisteredUserIds() {
        File[] accounts = new File(DataPaths.LEAGUE_ACCOUNTS).listFiles();
        String[] result = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            try {
                result[i] = JsonParser.parseString(new String(Files.readAllBytes(Paths.get(accounts[i].getPath())))).getAsJsonObject().get("puuid").getAsString();
            } catch (IOException e) {
                result[i] = "error";
            }
        }
        return result;
    }

    public static AccountInfo[] getRegisteredUsers() {
        File[] accounts = new File(DataPaths.LEAGUE_ACCOUNTS).listFiles();
        AccountInfo[] result = new AccountInfo[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            try {
                ObjectMapper om = new ObjectMapper();
                result[i] = om.readValue(new String(Files.readAllBytes(Paths.get(accounts[i].getPath()))), AccountInfo.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static List<String> getLast100MatchIdsById(String id) {
        JsonObject json = CommandParser.requestLast100MatchIDsByPUUID(id);
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

    public static int downloadEveryMapIfNotAlready(String puuid) {
        int downloadCount = 0;

        List<String> faultyMapIDs = CommandParser.getFaultyMatchIDs();
        List<String> matchIDs = getLast100MatchIdsById(puuid);

        if (matchIDs == null) {
            return EXPIRED_API_KEY;
        }

        for (int i = 0; i < matchIDs.size(); i++) {
            String id = matchIDs.get(i);
            String filePath = DataPaths.LEAGUE_MATCHES + id + ".json";
            if (!Files.exists(Paths.get(filePath)) && !faultyMapIDs.contains(id)) {
                try {
                    Match m = getMatchInformationByMatchID(matchIDs.get(i));
                    ObjectMapper om = new ObjectMapper();
                    om.writeValue(new File(filePath), m);
                    //System.out.println(i + " - downloaded");
                    downloadCount++;
                } catch (Exception e) {
                    System.out.println(i + "ERROR DOWNLOADING, id: " + id);
                    faultyMapIDs.add(id);
                }
            } else {
                //System.out.println(i + " - already downloaded");
            }
        }
        CommandParser.saveFaultyMatchIDs(faultyMapIDs);
        return downloadCount;
    }

    public static Match getMatchInformationByMatchID(String matchID) throws Exception{
        JsonObject rawMatch = CommandParser.requestMatchInformationByMatchID(matchID);
        if (rawMatch == null) {
            return null;
        }

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

    public static AccountInfo getRandomAccountInfoByPUUID(String puuid) {
        Map<String, AccountInfo> accountInfos = getRandomAccountInfos();

        if (accountInfos.containsKey(puuid)) {
            return accountInfos.get(puuid);
        }

        AccountInfo info = CommandParser.downloadAccountInfosByPuuid(puuid);
        if (info == null) {
            return null;
        }
        accountInfos.put(puuid, info);
        String path = DataPaths.RANDOM_LEAGUE_ACCOUNTS + "accountInfos.json";
        ObjectMapper om = new ObjectMapper();
        try {
            om.writeValue(new File(path), accountInfos);
        } catch (IOException e) {
            System.out.println("Error writing accountInfos to JSON: " + e.getMessage());
        }
        return info;
    }

    public static Map<String, AccountInfo> getRandomAccountInfos() {
        ObjectMapper om = new ObjectMapper();
        String path = DataPaths.RANDOM_LEAGUE_ACCOUNTS + "accountInfos.json";
        Map<String, AccountInfo> accountInfos;
        File file = new File(path);
        if (file.exists()) {
            try {
                accountInfos = om.readValue(file, new TypeReference<Map<String, AccountInfo>>() {});
                return accountInfos;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new HashMap<>();
    }

    public static boolean hasUniqueAccountInfo(String puuid) {
        String[] puuids = getRegisteredUserIds();
        for (String pu : puuids) {
            if (pu.equals(puuid)) {
                return true;
            }
        }
        return false;
    }

    public static AccountInfo getUniqueAccountInfo(String puuid) {
        AccountInfo[] infos = getRegisteredUsers();
        for (AccountInfo i : infos) {
            if (i != null && i.getPuuid().equals(puuid)) {
                return i;
            }
        }
        return null;
    }

}

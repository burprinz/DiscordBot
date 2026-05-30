package de.luiskun.league.stats;

import com.google.gson.Gson;
import de.luiskun.league.DataPaths;
import de.luiskun.league.api.CommandParser;
import de.luiskun.league.api.LeagueAPI;
import de.luiskun.league.objects.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatsAPI {

    private static Gson gson = new Gson();

    public static void createAccountMatchlists() {
        String[] userIDs = LeagueAPI.getRegisteredUserIds();

        for (String userID : userIDs) {
            MatchEditor.createMatchList(userID);
        }
    }

    public static OverallMatchStats getOverallMatchStats(String puuid) {

        List<String> matchIDs = MatchEditor.getMatchIDList(puuid);

        OverallMatchStats stats = new OverallMatchStats();
        stats.puuid = puuid;
        Map<String, Integer> teammateMap = new HashMap<>();

        for (String matchID : matchIDs) {
            File matchFile = new File(DataPaths.LEAGUE_MATCHES + matchID + ".json");
            Match match = null;
            try {
                match = gson.fromJson(new String(Files.readAllBytes(Paths.get(matchFile.getPath()))), Match.class);
            } catch (IOException e) {
                System.out.println("ERROR reading match infos " + e.getMessage());
            }

            if (match == null) {
                System.out.println("Error reading Match: Match is null!!");
                continue;
            }

            int nonSupportMatches = 0;

            for (MatchParticipant participant : match.getMatchParticipants()) {
                if (participant.puuid.equals(puuid)) {
                    stats.matchAmount += 1;
                    stats.totalKills += participant.kills;
                    stats.totalDeaths += participant.deaths;
                    stats.totalAssists += participant.assists;
                    stats.totalChampDamage += participant.totalDamageDealtToChampions;

                    stats.totalPings +=
                            (participant.basicPings + participant.allInPings + participant.assistMePings + participant.commandPings
                            + participant.dangerPings + participant.enemyMissingPings + participant.enemyVisionPings + participant.getBackPings
                            + participant.holdPings + participant.needVisionPings + participant.onMyWayPings + participant.pushPings
                            + participant.retreatPings + participant.visionClearedPings);

                    if (participant.won) {
                        stats.wins += 1;
                    } else {
                        stats.losses += 1;
                    }

                    stats.totalTimePlayed += participant.timePlayed;
                    stats.totalTimeSpentDead += participant.totalTimeSpentDead;

                    stats.totalGoldEarned += participant.goldEarned;
                    stats.totalGoldSpent += participant.goldSpent;

                    double kd = (double)participant.kills / (double) (participant.deaths>0?participant.deaths:1);

                    if (participant.kills > stats.mostKills) {
                        stats.mostKills = participant.kills;
                        stats.deathsInMostKills = participant.deaths;
                        stats.assistsInMostKills = participant.assists;
                        stats.kdInMostKills = kd;
                    }

                    if (kd > stats.bestKD) {
                        stats.bestKD = kd;
                        stats.killsInBestKD =  participant.kills;
                        stats.deathsInBestKD = participant.deaths;
                        stats.assistsInBestKD = participant.assists;
                    }

                    if (participant.deaths > stats.mostDeaths) {
                        stats.mostDeaths = participant.deaths;
                        stats.killsInMostDeaths = participant.kills;
                        stats.assistsInMostDeaths = participant.assists;
                        stats.kdInMostDeaths = kd;
                    }

                    if (kd < stats.worstKD) {
                        stats.worstKD = kd;
                    }

                    stats.averageVisionScore += participant.visionScore;
                    stats.minionsKilled += participant.totalMinionsKilled;
                    stats.wardsPlaced += participant.wardsPlaced;

                    if (participant.actualPosition.equals("TOP")) stats.topAmount += 1;
                    if (participant.actualPosition.equals("MIDDLE")) stats.midAmount += 1;
                    if (participant.actualPosition.equals("JUNGLE")) stats.jungleAmount += 1;
                    if (participant.actualPosition.equals("BOTTOM")) stats.botAmount += 1;

                    stats.doubleKills += participant.doubleKills;
                    stats.tripleKills += participant.tripleKills;
                    stats.quadraKills += participant.quadraKills;
                    stats.pentaKills += participant.pentaKills;

                    stats.spell1 += participant.spell1Casts;
                    stats.spell2 += participant.spell2Casts;
                    stats.spell3 += participant.spell3Casts;
                    stats.spell4 += participant.spell4Casts;

                    if (participant.gameEndedInSurrender) stats.surrenders += 1;

                } else {
                    teammateMap.merge(participant.puuid, 1, Integer::sum);
                }
            }
        }

        stats.averageVisionScore = stats.averageVisionScore / (double) stats.matchAmount;
        stats.averageMinionsKilled = (double)stats.minionsKilled / (double) stats.matchAmount;
        stats.winrate = (double) stats.wins / (double) (stats.matchAmount);
        stats.totalKD = (double) stats.totalKills / (double)(stats.totalDeaths>0?stats.totalDeaths:1);

        List<String> top10MostPlayedTeammateIDs = teammateMap.entrySet().stream()
                        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                        .limit(10)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

        List<TeammateInfos> mostPlayedTeammates = new ArrayList<>();
        for (String teammatePUUID : top10MostPlayedTeammateIDs) {
            TeammateInfos info = new  TeammateInfos();
            info.puuid = teammatePUUID;
            info.amMatches = teammateMap.get(teammatePUUID);


            AccountInfo ai = LeagueAPI.getUniqueAccountInfo(teammatePUUID);
            if (ai == null) {
                ai = LeagueAPI.getRandomAccountInfoByPUUID(teammatePUUID);
            }
            info.gameName = ai.getGameName() + "#" + ai.getTagLine();

            mostPlayedTeammates.add(info);
        }
        stats.mostPlayedCompanions = mostPlayedTeammates;

        return stats;
    }

}

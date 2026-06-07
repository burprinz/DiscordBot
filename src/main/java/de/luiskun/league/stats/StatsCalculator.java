package de.luiskun.league.stats;

import com.google.gson.Gson;
import de.luiskun.league.DataPaths;
import de.luiskun.apis.LeagueAPI;
import de.luiskun.league.objects.*;
import de.luiskun.log.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatsCalculator {

    private static Gson gson = new Gson();

    public static void createAccountMatchlists() {

        Log.info("Creating AccountMatchlists");
        String[] userIDs = LeagueAPI.getRegisteredUserIds();

        for (String userID : userIDs) {
            MatchEditor.createMatchList(userID);
        }
        Log.success("AccountMatchlists created");
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

            double team1OverallKills = 0;
            double team2OverallKills = 0;
            boolean playerWon = false;

            for (MatchParticipant participant : match.getMatchParticipants()) {

                playerWon = participant.won;
                if (participant.won) {
                    stats.wins += 1;
                    team1OverallKills += participant.kills;
                } else {
                    stats.losses += 1;
                    team2OverallKills += participant.kills;
                }

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
            if (playerWon) {
                stats.killParticipation += team1OverallKills;
            } else {
                stats.killParticipation += team2OverallKills;
            }
        }

        stats.averageVisionScore = stats.averageVisionScore / (double) stats.matchAmount;
        stats.averageMinionsKilled = (double)stats.minionsKilled / (double) stats.matchAmount;
        stats.winrate = (double) stats.wins / (double) (stats.matchAmount);
        stats.totalKD = (double) stats.totalKills / (double)(stats.totalDeaths>0?stats.totalDeaths:1);
        stats.totalKDA = (double) (stats.totalKills + stats.totalAssists) / (double)(stats.totalDeaths>0?stats.totalDeaths:1);
        stats.averageGameLength = (int) ((double) stats.totalTimePlayed / (double) stats.matchAmount);
        stats.percentageInDeathScreen = 1.0d / (double) stats.totalTimePlayed * (double) stats.totalTimeSpentDead;

        stats.killParticipation = ((double) stats.totalKills) / stats.killParticipation;
        
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
            if (ai != null) {
                info.gameName = ai.getGameName() + "#" + ai.getTagLine();
            }

            int teamWins = 0;
            int teamLost = 0;

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

                for (MatchParticipant participant : match.getMatchParticipants()) {
                    if (participant.puuid.equals(teammatePUUID)) {
                        if (participant.won) {
                            teamWins += 1;
                        } else {
                            teamLost += 1;
                        }
                        break;
                    }
                }
            }

            info.wins = teamWins;
            info.losses = teamLost;

            info.winrate = ((double) teamWins) / ((double) info.amMatches);

            mostPlayedTeammates.add(info);

        }
        stats.mostPlayedCompanions = mostPlayedTeammates;

        return stats;
    }

}

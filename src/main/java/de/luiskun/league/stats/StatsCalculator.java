package de.luiskun.league.stats;

import com.google.gson.Gson;
import de.luiskun.Helpers;
import de.luiskun.league.DataPaths;
import de.luiskun.apis.LeagueAPI;
import de.luiskun.league.objects.*;
import de.luiskun.log.Log;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class StatsCalculator {

    private static final Color backgroundColor = Color.decode("#1e1e2e");

    private static final Color redColor = Color.decode("#ff0000");
    private static final Color greenColor = Color.decode("#00ff00");
    private static final Color blueColor = Color.decode("#0000ff");


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

                if (participant.puuid.equals(puuid)) {
                    if (participant.won) {
                        stats.wins += 1;
                        team1OverallKills += participant.kills;
                    } else {
                        stats.losses += 1;
                        team2OverallKills += participant.kills;
                    }

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


    public static BufferedImage getOverallMatchStatsImage(String puuid) {
        AccountInfo info = LeagueAPI.getUniqueAccountInfo(puuid);
        OverallMatchStats stats = getOverallMatchStats(puuid);

        if (info == null) {
            Log.error("Error getting AccountInfos for StatsImage");
            return null;
        }

        if (stats == null) {
            Log.error("Error getting StatsInfos for StatsImage");
            return null;
        }

        BufferedImage profilePic =  LeagueAPI.getProfilePicByPuuid(puuid);

        int W = 700;
        int H = 500;

        BufferedImage result = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = result.createGraphics();

        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, W, H);

        g2.setStroke(new BasicStroke(3));

        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, W-1, H-1);

        g2.setStroke(new BasicStroke(1));

        int x = 5;
        int y = 5;

        int profileS = 50;
        g2.setClip(new Ellipse2D.Float(x, y, profileS, profileS));
        g2.drawImage(profilePic, x, y, profileS, profileS, null);
        g2.setClip(null);


        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(20f));
        String text = info.getGameName() + "#" + info.getTagLine();
        g2.drawString(text, x + profileS + 5, y + getYForCenteredText(g2, text, profileS));


        int xTextGap = 10;
        int yTextGap = 15;
        int startBaseX = 10;
        int baseX = startBaseX;
        int baseY = profileS + 22;

        int lineH = 280;

        x = baseX;
        y = baseY;
        g2.setFont(g2.getFont().deriveFont(16f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON)));
        text = "Overall Stats";
        g2.drawString(text, x, y);


        g2.setFont(g2.getFont().deriveFont(13f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, -1)));

        y += yTextGap + 5;
        text = "Time Played: " + Helpers.getTimeString(stats.totalTimePlayed);
        g2.drawString(text, x, y);


        y += yTextGap;
        text = "Time Dead: " + Helpers.getTimeString(stats.totalTimeSpentDead);
        g2.drawString(text, x, y);

        y += yTextGap;
        text = "Avg Game length: " + Helpers.getTimeString(stats.averageGameLength);
        g2.drawString(text, x, y);

        y += yTextGap;
        text = "Percentage Dead: " + String.format("%.2f", stats.percentageInDeathScreen*100) + "%";
        g2.drawString(text, x, y);

        y += yTextGap + 5;
        g2.setColor(greenColor);
        text = "Wins: " + stats.wins;
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text) + xTextGap;
        text = "Defeats: " + stats.losses;
        g2.setColor(redColor);
        g2.drawString(text, x, y);

        x = baseX;
        y += yTextGap;
        g2.setColor(greenColor);
        text = "Kills: " + stats.totalKills;
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text) + xTextGap;
        text = "Deaths: " + stats.totalDeaths;
        g2.setColor(redColor);
        g2.drawString(text, x, y);

        x +=  getStringWidth(g2, text) + xTextGap;
        text = "Assists: " + stats.totalAssists;
        g2.setColor(blueColor);
        g2.drawString(text, x, y);

        x = baseX;
        y += yTextGap;
        g2.setColor(Color.WHITE);
        text = "K/D: " + round(stats.totalKD, 2) + "  KA/D: " + round(stats.totalKDA, 2);
        g2.drawString(text, x, y);



        y += yTextGap + 18;
        g2.setFont(g2.getFont().deriveFont(16f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON)));
        text = "Milestone Matches";
        g2.drawString(text, x, y);

        y += yTextGap + 5;
        g2.setFont(g2.getFont().deriveFont(13f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, -1)));
        text = "Most Kills) KDA: (";
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(greenColor);
        text = ""+stats.mostKills;
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(Color.WHITE);
        text = "/";
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(redColor);
        text = ""+stats.deathsInMostKills;
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(Color.WHITE);
        text = "/";
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(blueColor);
        text = ""+stats.assistsInMostKills;
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(Color.WHITE);
        text = ")  K/D: "+String.format("%.2f", stats.kdInMostKills);
        g2.drawString(text, x, y);

        x = baseX;
        y += yTextGap;
        g2.setFont(g2.getFont().deriveFont(13f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, -1)));
        text = "Most Deaths) KDA: (";
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(greenColor);
        text = ""+stats.killsInMostDeaths;
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(Color.WHITE);
        text = "/";
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(redColor);
        text = ""+stats.mostDeaths;
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(Color.WHITE);
        text = "/";
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(blueColor);
        text = ""+stats.assistsInMostDeaths;
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(Color.WHITE);
        text = ")  K/D: "+String.format("%.2f", stats.kdInMostDeaths);
        g2.drawString(text, x, y);

        x = baseX;
        y += yTextGap;
        g2.setFont(g2.getFont().deriveFont(13f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, -1)));
        text = "Best K/D) KDA: (";
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(greenColor);
        text = ""+stats.killsInBestKD;
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(Color.WHITE);
        text = "/";
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(redColor);
        text = ""+stats.deathsInBestKD;
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(Color.WHITE);
        text = "/";
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(blueColor);
        text = ""+stats.assistsInBestKD;
        g2.drawString(text, x, y);

        x += getStringWidth(g2, text)+2;
        g2.setColor(Color.WHITE);
        text = ")  K/D: "+String.format("%.2f", stats.bestKD);
        g2.drawString(text, x, y);


        baseX += 285;

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(280, baseY-10, 280, lineH);
        g2.setStroke(new BasicStroke(1));


        x = baseX;
        y = baseY;

        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(16f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON)));
        text = "Gold";
        g2.drawString(text, x, y);

        g2.setFont(g2.getFont().deriveFont(13f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, -1)));

        y += yTextGap + 5;
        text = "Earned: " + Helpers.addIntegerPoints(stats.totalGoldEarned);
        g2.drawString(text, x, y);

        y += yTextGap;
        text = "Spent: " + Helpers.addIntegerPoints(stats.totalGoldSpent);
        g2.drawString(text, x, y);

        y += yTextGap + 18;
        g2.setFont(g2.getFont().deriveFont(16f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON)));
        text = "Minion Kills";
        g2.drawString(text, x, y);

        g2.setFont(g2.getFont().deriveFont(13f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, -1)));

        y += yTextGap + 5;
        text = "Total: " + Helpers.addIntegerPoints(stats.minionsKilled);
        g2.drawString(text, x, y);

        y += yTextGap;
        text = "Average: " + round(stats.averageMinionsKilled, 2);
        g2.drawString(text, x, y);

        y += yTextGap + 18;
        g2.setFont(g2.getFont().deriveFont(16f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON)));
        text = "Multikills";
        g2.drawString(text, x, y);

        y += yTextGap + 5;
        g2.setFont(g2.getFont().deriveFont(13f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, -1)));
        text = "Double-Kills: " + stats.doubleKills;
        g2.drawString(text, x, y);

        y += yTextGap;
        text = "Triple-Kills: " + stats.tripleKills;
        g2.drawString(text, x, y);

        y += yTextGap;
        text = "Quadra-Kills: " + stats.quadraKills;
        g2.drawString(text, x, y);

        y += yTextGap;
        text = "Penta-Kills: " + stats.pentaKills;
        g2.drawString(text, x, y);


        baseX += 175;

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(450, baseY-10, 450, lineH);
        g2.setStroke(new BasicStroke(1));


        x = baseX;
        y = baseY;

        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(16f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON)));
        text = "Spells used";
        g2.drawString(text, x, y);

        y += yTextGap + 5;
        g2.setFont(g2.getFont().deriveFont(13f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, -1)));
        text = "Spell 1: " + stats.spell1;
        g2.drawString(text, x, y);

        y += yTextGap;
        text = "Spell 2: " + stats.spell2;
        g2.drawString(text, x, y);

        y += yTextGap;
        text = "Spell 3: " + stats.spell3;
        g2.drawString(text, x, y);

        y += yTextGap;
        text = "Spell 4: " + stats.spell4;
        g2.drawString(text, x, y);

        y += yTextGap + 18;
        g2.setFont(g2.getFont().deriveFont(16f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON)));
        text = "Vision";
        g2.drawString(text, x, y);

        y += yTextGap + 5;
        g2.setFont(g2.getFont().deriveFont(13f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, -1)));
        text = "Average Score: " + round(stats.averageVisionScore, 2);
        g2.drawString(text, x, y);

        y += yTextGap;
        text = "Wards placed: " + stats.wardsPlaced;
        g2.drawString(text, x, y);

        y += yTextGap + 18;
        g2.setFont(g2.getFont().deriveFont(16f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON)));
        text = "Other";
        g2.drawString(text, x, y);

        y += yTextGap + 5;
        g2.setFont(g2.getFont().deriveFont(13f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, -1)));
        text = "Champ Damage: " + Helpers.addIntegerPoints(stats.totalChampDamage);
        g2.drawString(text, x, y);

        y += yTextGap;
        text = "Pings: " + stats.totalPings;
        g2.drawString(text, x, y);

        y += yTextGap;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(8, y, W-8, y);
        g2.setStroke(new BasicStroke(1));

        g2.setColor(Color.WHITE);

        baseX = startBaseX;
        x = baseX;

        y += yTextGap+18;
        g2.setFont(g2.getFont().deriveFont(16f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON)));
        text = "Most frequent players";
        g2.drawString(text, x, y);

        y += 5;
        g2.setFont(g2.getFont().deriveFont(13f).deriveFont(Collections.singletonMap(TextAttribute.UNDERLINE, -1)));

        int statsStartX = baseX + 200;

        for (TeammateInfos infos : stats.mostPlayedCompanions) {
            x = baseX;
            y += yTextGap;
            text = infos.gameName;
            g2.setColor(Color.WHITE);
            g2.drawString(text, x, y);

            x = statsStartX;
            text = infos.amMatches + " Matches ";
            g2.drawString(text, x, y);

            x += getStringWidth(g2, text)+2;
            text = infos.wins + " Wins ";
            g2.setColor(greenColor);
            g2.drawString(text, x, y);

            x += getStringWidth(g2, text)+2;
            text = infos.losses + " Defeats ";
            g2.setColor(redColor);
            g2.drawString(text, x, y);

            x += getStringWidth(g2, text)+2;
            text = "WR=" + round(infos.winrate, 2);
            g2.setColor(Color.WHITE);
            g2.drawString(text, x, y);

        }


        g2.dispose();

        return result;
    }


    public static FileUpload getOverallMatchStatsByteStream(String puuid) {
        BufferedImage img = getOverallMatchStatsImage(puuid);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", baos);
        } catch (IOException e) {
            Log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }

        byte[] bytes = baos.toByteArray();

        return FileUpload.fromData(bytes, puuid + "_stats.png");
    }


    public static int getYForCenteredText(Graphics2D g2, String text, int h) {
        return (h - g2.getFontMetrics().getAscent() - g2.getFontMetrics().getDescent()) / 2 + g2.getFontMetrics().getAscent();
    }

    public static int getStringWidth(Graphics2D g2, String text) {
        return g2.getFontMetrics().stringWidth(text);
    }

    public static String round(double num, int nachkommastellen) {
        return String.format("%."+nachkommastellen+"f", num);
    }

}

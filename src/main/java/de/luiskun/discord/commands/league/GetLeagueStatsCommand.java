package de.luiskun.discord.commands.league;

import de.luiskun.Helpers;
import de.luiskun.discord.commands.DiscordCommand;
import de.luiskun.league.DataPaths;
import de.luiskun.apis.LeagueAPI;
import de.luiskun.league.objects.OverallMatchStats;
import de.luiskun.league.objects.TeammateInfos;
import de.luiskun.league.stats.StatsCalculator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class GetLeagueStatsCommand implements DiscordCommand {
    @Override
    public void execute(String args, User user, Message message, MessageChannel channel, Guild guild) {

        String riotID;

        if (message.getMentions().getUsers().size() == 1) {
            User mention = message.getMentions().getUsers().get(0);
            String path = DataPaths.DISCORD_USERNAME_CONNECTIONS + "/" + mention.getId() + ".txt";
            if (!Files.exists(Paths.get(path))) {
                channel.sendMessage("User not registered!").queue();
                return;
            }
            try {
                riotID = new String(Files.readAllBytes(Paths.get(path)));
            } catch (Exception e) {
                channel.sendMessage("Error getting userdata!").queue();
                return;
            }
        } else {
            riotID = args;
        }

        if (riotID.equals("")) {
            String path = DataPaths.DISCORD_USERNAME_CONNECTIONS + "/" + user.getId() + ".txt";
            if (!Files.exists(Paths.get(path))) {
                channel.sendMessage("You are not registered!").queue();
                return;
            }
            try {
                riotID = new String(Files.readAllBytes(Paths.get(path)));
            } catch (Exception e) {
                channel.sendMessage("Error getting userdata!").queue();
                return;
            }
        }

        if (riotID == null || riotID.equals("")) {
            channel.sendMessage("Error getting userdata!").queue();
            return;
        }

        String puuid = LeagueAPI.getPUUIDByRiotID(riotID);
        if (puuid == null) {
            channel.sendMessage("This riot ID does not exist! (" + riotID + ")").queue();
            return;
        }

        OverallMatchStats stats = StatsCalculator.getOverallMatchStats(puuid);
        if (stats == null) {
            channel.sendMessage("Error calculating stats").queue();
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("User: " + riotID);
        eb.setColor(Color.decode("#00b0f4"));
        eb.setTimestamp(LocalDateTime.now(ZoneId.of("Europe/Berlin")));
        eb.setAuthor("League Stats");
        eb.addField("Overall Stats",
                "Wins: "+stats.wins + "; Defeats:" + stats.losses + "; Surrenders:"+ stats.surrenders+ "; WR: "+String.format("%.2f", stats.winrate)+"\n"+
                "KDA: ("+stats.totalKills+"/"+stats.totalDeaths+"/"+stats.totalAssists+")\n"+
                "K/D:"+String.format("%.2f", stats.totalKD)+"; KA/D:" + String.format("%.2f", stats.totalKDA) + "; Kill Participation:" + String.format("%.2f", stats.killParticipation*100) + "%\n" +
                "Pings: "+stats.totalPings + "; Champ-Damage:"+Helpers.addIntegerPoints(stats.totalChampDamage) + "\n"+
                "Time played: " + Helpers.getTimeString(stats.totalTimePlayed) + "; Time in Death-Screen: " + Helpers.getTimeString(stats.totalTimeSpentDead) + "\n"+
                "Avg Game length: " + Helpers.getTimeString(stats.averageGameLength) + "; Percentage Dead: " + String.format("%.2f", stats.percentageInDeathScreen*100) + "%\n" +
                "Gold earned: " + Helpers.addIntegerPoints(stats.totalGoldEarned) + "; Gold spent: " + Helpers.addIntegerPoints(stats.totalGoldSpent),
                false);
        /*
        eb.addField("Lanes", "Top: " + stats.topAmount + "\nJungle: " + stats.jungleAmount + "\n"+
                "Mid: " + stats.midAmount + "\n Bot: " + stats.botAmount, false);
        */
        eb.addField("Most Kills Round", "KDA: ("+stats.mostKills + "/" + stats.deathsInMostKills + "/" + stats.assistsInMostKills+"); K/D:"+String.format("%.2f", stats.kdInMostKills), false);
        eb.addField("Most Deaths Round", "KDA: ("+stats.killsInMostDeaths + "/" + stats.mostDeaths + "/" + stats.assistsInMostDeaths+"); K/D:"+String.format("%.2f", stats.kdInMostDeaths), false);
        eb.addField("Best K/D Round", "KDA: ("+stats.killsInBestKD + "/" + stats.deathsInBestKD + "/" + stats.assistsInBestKD+"); K/D:"+String.format("%.2f", stats.bestKD), false);

        eb.addField("Multi-Kills", "Double-Kills: " + stats.doubleKills + "\n"
                + "Triple-Kills: " + stats.tripleKills + "\n"
                + "Quadra-Kills: " + stats.quadraKills + "\n"
                + "Penta-Kills: " + stats.pentaKills, false);

        eb.addField("Minions", "Total Minion Kills: " + stats.minionsKilled + "\nAverage Minions Killed: " + String.format("%.2f", stats.averageMinionsKilled), false);

        eb.addField("Vision", "Average Vision Score: " + String.format("%.2f", stats.averageVisionScore) + "\n"
                + "Wards placed: " + stats.wardsPlaced, false);

        eb.addField("Spells", "Spell 1 used: " + stats.spell1 + "\n"
                + "Spell 2 used: " + stats.spell2 + "\n"
                + "Spell 3 used: " + stats.spell3 + "\n"
                + "Spell 4 used: " + stats.spell4, false);

        String teammateStr = "";
        for (TeammateInfos infos : stats.mostPlayedCompanions) {
            teammateStr += infos.gameName + " (" + infos.amMatches + "\t Matches; " + infos.wins + " Wins; "+ infos.losses + " Defeats; WR=" + String.format("%.2f", infos.winrate) + ")\n";
        }
        eb.addField("Most Frequent Teammates", teammateStr, false);

        eb.setFooter("Request created by " + user.getName());
        channel.sendMessageEmbeds(eb.build()).queue();
    }
}

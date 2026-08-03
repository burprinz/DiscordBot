package de.luiskun.discord.commands.league;

import de.luiskun.Helpers;
import de.luiskun.apis.LeagueAPI;
import de.luiskun.discord.commands.DiscordCommand;
import de.luiskun.league.DataPaths;
import de.luiskun.league.objects.OverallMatchStats;
import de.luiskun.league.objects.TeammateInfos;
import de.luiskun.league.stats.StatsCalculator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.FileUpload;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class GetLeagueStatsImageCommand implements DiscordCommand {

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

        FileUpload stats = StatsCalculator.getOverallMatchStatsByteStream(puuid);
        if (stats == null) {
            channel.sendMessage("Error calculating stats").queue();
            return;
        }

        channel.sendMessage("Stats for " + riotID).addFiles(stats).queue();
    }

}

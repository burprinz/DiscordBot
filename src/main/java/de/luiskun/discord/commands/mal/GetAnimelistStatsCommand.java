package de.luiskun.discord.commands.mal;

import de.luiskun.Helpers;
import de.luiskun.discord.commands.DiscordCommand;
import de.luiskun.league.DataPaths;
import de.luiskun.apis.MyAnimeListAPI;
import de.luiskun.myanimelist.objects.Anime;
import de.luiskun.myanimelist.objects.AnimeList;
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
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class GetAnimelistStatsCommand  implements DiscordCommand {
    @Override
    public void execute(String args, User user, Message message, MessageChannel channel, Guild guild) {
        String profileName;

        if (message.getMentions().getUsers().size() == 1) {
            User mention = message.getMentions().getUsers().get(0);
            String path = DataPaths.DISCORD_MAL_ACCOUNT_CONNECTIONS + "/" + mention.getId() + ".txt";
            if (!Files.exists(Paths.get(path))) {
                channel.sendMessage("User not registered!").queue();
                return;
            }
            try {
                profileName = new String(Files.readAllBytes(Paths.get(path)));
            } catch (Exception e) {
                channel.sendMessage("Error getting userdata!").queue();
                return;
            }
        } else {
            profileName = args;
        }

        if (profileName.equals("")) {
            String path = DataPaths.DISCORD_MAL_ACCOUNT_CONNECTIONS + "/" + user.getId() + ".txt";
            if (!Files.exists(Paths.get(path))) {
                channel.sendMessage("You are not registered!").queue();
                return;
            }
            try {
                profileName = new String(Files.readAllBytes(Paths.get(path)));
            } catch (Exception e) {
                channel.sendMessage("Error getting userdata!").queue();
                return;
            }
        }

        if (profileName == null || profileName.equals("")) {
            channel.sendMessage("Error getting userdata!").queue();
            return;
        }

        AnimeList list = MyAnimeListAPI.getAnimeList(profileName);

        if (list == null) {
            channel.sendMessage("Error getting userdata. Maybe the username is wrong! ("+profileName+")").queue();
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("User: " + profileName);
        eb.setColor(Color.decode("#00b0f4"));
        eb.setTimestamp(LocalDateTime.now(ZoneId.of("Europe/Berlin")));
        eb.setAuthor("MyAnimeList Stats");


        String baseStatsStr = "Overall:\n";
        baseStatsStr += "- Entries: " + list.animes.size();
        baseStatsStr += "\n- Watched Episodes: " + list.overallWatchedEpisodes;
        baseStatsStr += "\n- Watchtime: " + Helpers.getTimeString(list.overallWatchedTime);


        eb.addField("Base Stats", baseStatsStr, false);

        String statusCountStr = "";
        for (int i = 0; i < Anime.STATUS_NAMES.length; i++) {
            statusCountStr += Anime.STATUS_NAMES[i] + ": " + list.statusCount[i] + " Entries\n- "
                    + list.episodesInStatus[i] + " Episodes\n- "
                    + Helpers.getTimeString(list.timeInStatus[i]) + "\n";
        }

        eb.addField("Watching Status", statusCountStr, false);

        String genreStatsStr = "";
        int amStats = 5;
        if (list.genreCount.keySet().size() < 10) amStats = list.genreCount.keySet().size();
        List<Map.Entry<Integer, Integer>> entryList = new ArrayList<>(list.genreCount.entrySet());
        for (int i = 0; i < amStats; i++) {
            Map.Entry<Integer, Integer> entry =  entryList.get(i);
            int id = entry.getKey();
            int value = entry.getValue();
            String name = list.genreNames.get(id);
            genreStatsStr += name + ": " + value + " Entries\n- "
                    + list.genreEpisodes.get(id) + " Episodes\n- "
                    + Helpers.getTimeString(list.genreTime.get(id)) + "\n";
        }

        eb.addField("Genre Stats", genreStatsStr, false);

        eb.setFooter("Request created by " + user.getName());
        channel.sendMessageEmbeds(eb.build()).queue();
    }
}

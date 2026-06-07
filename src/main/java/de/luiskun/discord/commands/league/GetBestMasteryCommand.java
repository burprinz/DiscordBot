package de.luiskun.discord.commands.league;

import de.luiskun.discord.commands.DiscordCommand;
import de.luiskun.league.DataPaths;
import de.luiskun.apis.LeagueAPI;
import de.luiskun.league.objects.Mastery;
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

public class GetBestMasteryCommand implements DiscordCommand {
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
        }

        String puuid = LeagueAPI.getPUUIDByRiotID(riotID);
        if (puuid == null) {
           channel.sendMessage("This riot ID does not exist! (" + riotID + ")").queue();
           return;
        }

        Mastery bestMastery = LeagueAPI.getBestMasteryByPUUID(puuid);
        if (bestMastery == null) {
            channel.sendMessage("Error getting mastery infos. (" + riotID + ").").queue();
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("User: " + riotID);
        eb.setColor(Color.decode("#00b0f4"));
        eb.setTimestamp(LocalDateTime.now(ZoneId.of("Europe/Berlin")));
        eb.setAuthor("Mastery Stats (" + bestMastery.name + ")");
        eb.addField("Last time played", bestMastery.getDateString(), false);
        eb.addField("Level", ""+bestMastery.level, false);
        eb.addField("Mastery Points", ""+bestMastery.points, false);
        eb.addField("Until Next Level", ""+bestMastery.currentLevelPoints + "/" + bestMastery.neededPoints, false);
        eb.setThumbnail("https://statics.koreanbuilds.net/tile_200x200/"+bestMastery.name+".webp");
        eb.setFooter("Request created by " + user.getName());

        channel.sendMessageEmbeds(eb.build()).queue();
    }
}

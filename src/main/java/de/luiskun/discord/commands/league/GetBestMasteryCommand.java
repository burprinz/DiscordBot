package de.luiskun.discord.commands.league;

import de.luiskun.discord.commands.DiscordCommand;
import de.luiskun.league.LeagueAPI;
import de.luiskun.league.Mastery;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;

public class GetBestMasteryCommand implements DiscordCommand {
    @Override
    public void execute(String args, User user, Message message, MessageChannel channel, Guild guild) {

        String[] spl = args.split(" ");
        if (spl.length != 1 || spl[0].length() < 1 || !spl[0].contains("#")) {
            channel.sendMessage("Command used wrongly! <!bestchamp name#ABC>").queue();
            return;
        }

        String riotID = spl[0];
        String puuid = LeagueAPI.getPUUIDByRiotID(riotID);
        if (puuid == null) {
           channel.sendMessage("This riot ID does not exist!").queue();
           return;
        }

        Mastery bestMastery = LeagueAPI.getBestMasteryByPUUID(puuid);
        if (bestMastery == null) {
            channel.sendMessage("Error getting mastery infos.").queue();
            return;
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("User: " + riotID);
        eb.setColor(Color.decode("#00b0f4"));
        eb.setTimestamp(LocalDateTime.now());
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

package de.luiskun.discord.commands.league;

import de.luiskun.discord.commands.DiscordCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public class HelpCommand implements DiscordCommand {

    @Override
    public void execute(String args, User user, Message message, MessageChannel channel, Guild guild) {
        
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Help");

        String lolCommands = "";
        lolCommands += "- !lol-register <name#tag>\n";
        lolCommands += "- !lol-bestchamp <none/name#tag/@discordname>\n";
        lolCommands += "- !lol-stats <none/name#tag/@discordname>\n";
        lolCommands += "- !lol-stats-img <none/name#tag/@discordname>\n";
        eb.addField("League Commands", lolCommands, false);

        String malCommands = "";
        malCommands += "- !mal-register <name>\n";
        malCommands += "- !mal-stats <none/name/@discordname>";
        eb.addField("MyAnimeList Commands", malCommands, false);

        channel.sendMessageEmbeds(eb.build()).queue();
    }
}

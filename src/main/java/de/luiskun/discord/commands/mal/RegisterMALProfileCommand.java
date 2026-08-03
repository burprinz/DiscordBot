package de.luiskun.discord.commands.mal;

import de.luiskun.discord.commands.DiscordCommand;
import de.luiskun.league.DataPaths;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class RegisterMALProfileCommand implements DiscordCommand {
    @Override
    public void execute(String args, User user, Message message, MessageChannel channel, Guild guild) {
        String username = user.getId();
        String malName = args;

        String path = DataPaths.DISCORD_MAL_ACCOUNT_CONNECTIONS + "/" + username + ".txt";

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(malName);
            writer.close();
        } catch (Exception e) {
            channel.sendMessage("Error registering").queue();
            return;
        }
        channel.sendMessage("Successfully registered MyAnimeList Account!").queue();
    }
}

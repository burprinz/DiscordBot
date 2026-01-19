package de.luiskun.discord.commands;

import de.luiskun.discord.commands.league.GetBestMasteryCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {

    public ConcurrentHashMap<String, DiscordCommand> commands;

    public CommandManager() {
        this.commands = new ConcurrentHashMap<>();

        this.commands.put("bestchamp", new GetBestMasteryCommand());

    }

    public boolean performCommand(String command, String args, User user, Message message, MessageChannel channel, Guild guild) {
        DiscordCommand cmd;
        if ((cmd = this.commands.get(command.toLowerCase())) != null) {
            cmd.execute(args, user, message, channel, guild);
            return true;
        }
        return false;
    }

}

package de.luiskun.discord.commands;

import de.luiskun.discord.commands.league.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {

    public ConcurrentHashMap<String, DiscordCommand> commands;

    public CommandManager() {
        this.commands = new ConcurrentHashMap<>();

        this.commands.put("help", new HelpCommand());

        this.commands.put("lol-bestchamp", new GetBestMasteryCommand());
        this.commands.put("lol-register", new RegisterLeagueProfileCommand());
        this.commands.put("lol-stats", new GetLeagueStatsCommand());

        this.commands.put("mal-register", new RegisterMALProfileCommand());
        this.commands.put("mal-stats", new GetAnimelistStatsCommand());
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

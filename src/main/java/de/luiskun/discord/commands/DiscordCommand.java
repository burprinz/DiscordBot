package de.luiskun.discord.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public interface DiscordCommand {

    public void execute(String args, User user, Message message, MessageChannel channel, Guild guild);

}

package de.luiskun.discord.listeners;

import de.luiskun.discord.DiscordBot;
import de.luiskun.log.Log;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        super.onMessageReceived(event);



        if (event.isFromType(ChannelType.TEXT)) {
            User user = event.getAuthor();
            Message message = event.getMessage();
            MessageChannel channel = event.getChannel();
            Guild server = event.getGuild();


            String rawMessage = message.getContentRaw();

            if (rawMessage.startsWith("!")) {
                String withoutPrefix = rawMessage.substring(1);
                String[] args = withoutPrefix.split(" ");
                withoutPrefix = withoutPrefix.substring(args[0].length());
                if (withoutPrefix.length() > 0 && withoutPrefix.charAt(0) == ' ') {
                    withoutPrefix = withoutPrefix.substring(1);
                }
                DiscordBot.INSTANCE.getCmdMan().performCommand(args[0], withoutPrefix, user, message, channel, server);
            }
        }

    }
}

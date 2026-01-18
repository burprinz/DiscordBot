package de.luiskun.discord.listeners;

import de.luiskun.log.Log;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class VoiceStateListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        super.onGuildVoiceUpdate(event);

        Member member = event.getMember();
        AudioChannelUnion before = event.getChannelLeft();
        AudioChannelUnion after = event.getChannelJoined();

        String username = member.getUser().getName();

        if (before == null && after != null) {
            // Person joined channel
            String joinName = after.getName();
            String joinGuild = after.getGuild().getName();
            Log.debug(username + " joined " + joinName + " on " + joinGuild);
        } else if (before != null && after != null) {
            // Person switched channel
            String joinName = after.getName();
            String joinGuild = after.getGuild().getName();
            String discName = before.getName();
            String discGuild = before.getGuild().getName();
            Log.debug(username + " switched from " + discName + " on " + discGuild + " to " + joinName + " on " + joinGuild);
        } else if (before != null && after == null) {
            // Person disconnected
            String discName = before.getName();
            String discGuild = before.getGuild().getName();
            Log.debug(username + " left " + discName + " on " + discGuild);
        }

    }

}

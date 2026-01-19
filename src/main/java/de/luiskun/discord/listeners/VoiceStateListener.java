package de.luiskun.discord.listeners;

import de.luiskun.discord.soundboard.Soundboard;
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
        handleDebugMessage(event);

        Member member = event.getMember();
        AudioChannelUnion before = event.getChannelLeft();
        AudioChannelUnion after = event.getChannelJoined();

        if (before == null && after != null) {
            // User ist neu gejoint
            if (member.getUser().getId().equals("392709166575255573")) {
                Log.debug("Lucas joined -> playing <Mann vom TUS>");
                Soundboard.play(event.getGuild(), after, "src/main/resources/sounds/mann_vom_tus.wav");
            }
        }
    }

    private void handleDebugMessage(GuildVoiceUpdateEvent event) {
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

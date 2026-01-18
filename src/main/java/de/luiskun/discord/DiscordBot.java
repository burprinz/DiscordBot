package de.luiskun.discord;

import de.luiskun.Konstanten;
import de.luiskun.discord.listeners.VoiceStateListener;
import de.luiskun.log.Log;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class DiscordBot {

    private static JDA jda;

    public void start() {

        Log.debug("Discord-Bot starting...");

        jda = JDABuilder.createDefault(Konstanten.DISCORD_TOKEN)
                .setActivity(Activity.playing("nothing."))
                .setStatus(OnlineStatus.ONLINE)
                .build();

        Log.success("Discord-Bot started");

        jda.addEventListener(new VoiceStateListener());
    }


}

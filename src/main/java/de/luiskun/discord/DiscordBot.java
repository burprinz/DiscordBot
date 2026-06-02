package de.luiskun.discord;

import de.luiskun.APIKeys;
import de.luiskun.discord.commands.CommandManager;
import de.luiskun.discord.listeners.MessageListener;
import de.luiskun.discord.listeners.VoiceStateListener;
import de.luiskun.discord.soundboard.Soundboard;
import de.luiskun.log.Log;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordBot {


    public static DiscordBot INSTANCE;
    private static JDA jda;

    private CommandManager cmdMan;

    public DiscordBot() {
        Log.debug("Discord-Bot starting...");

        this.cmdMan = new CommandManager();

        INSTANCE = this;
        jda = JDABuilder.createDefault(APIKeys.DISCORD_API_KEY)
                .setActivity(Activity.playing("nothing."))
                .setStatus(OnlineStatus.ONLINE)
                .enableIntents(GatewayIntent.GUILD_MESSAGES,
                                GatewayIntent.MESSAGE_CONTENT)
                .build();
        Soundboard.init();
        jda.addEventListener(new VoiceStateListener());
        jda.addEventListener(new MessageListener());

        Log.success("Discord-Bot started");
    }

    public CommandManager getCmdMan() {
        return cmdMan;
    }

}

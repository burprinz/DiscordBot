package de.luiskun;

import de.luiskun.discord.DiscordBot;
import de.luiskun.log.Log;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static DiscordBot discordBot;

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                Log.save();
            }
        }));

        Log.start();

        new Main();
    }

    public Main() {
        Log.info("Program started");

        discordBot = new DiscordBot();
        discordBot.start();
    }

}


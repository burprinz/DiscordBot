package de.luiskun;

import de.luiskun.discord.DiscordBot;
import de.luiskun.log.Log;

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

        DiscordBot discordBot = new DiscordBot();
    }

}


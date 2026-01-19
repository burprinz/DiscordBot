package de.luiskun;

import de.luiskun.discord.DiscordBot;
import de.luiskun.league.DataPaths;
import de.luiskun.league.LeagueAPI;
import de.luiskun.league.Mastery;
import de.luiskun.log.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private static DiscordBot discordBot;

    public static void main(String[] args) throws IOException {

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                Log.save();
            }
        }));

        Log.start();

        new Main();
    }

    public Main() throws IOException {
        Log.info("Program started");

        DiscordBot discordBot = new DiscordBot();
    }

}


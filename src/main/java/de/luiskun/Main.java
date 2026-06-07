package de.luiskun;

import de.luiskun.discord.DiscordBot;
import de.luiskun.apis.LeagueAPI;
import de.luiskun.league.stats.StatsCalculator;
import de.luiskun.log.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        Log.start();

        APIKeys.loadAPIKeys();


        ScheduledExecutorService downloadSchedule = Executors.newSingleThreadScheduledExecutor();

        downloadSchedule.scheduleAtFixedRate(Main::downloadRoutine,
                0,
                60,
                TimeUnit.MINUTES);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                Log.save();
                downloadSchedule.shutdown();
            }
        }));

        new Main();
    }

    public Main() {
        Log.info("Program started");

        DiscordBot discordBot = new DiscordBot();
    }

    private static void downloadRoutine() {
        LeagueAPI.downloadMatchesFromRegisteredUsers();
        StatsCalculator.createAccountMatchlists();
    }

}


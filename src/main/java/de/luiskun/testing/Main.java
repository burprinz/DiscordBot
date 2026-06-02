package de.luiskun.testing;

import de.luiskun.league.api.LeagueAPI;
import de.luiskun.league.objects.Matchlist;
import de.luiskun.league.objects.OverallMatchStats;
import de.luiskun.league.stats.MatchEditor;
import de.luiskun.league.stats.StatsAPI;
import de.luiskun.log.Log;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                Log.save();
            }
        }));
        Log.start();
        LeagueAPI.downloadMatchesFromRegisteredUsers();
        StatsAPI.createAccountMatchlists();
        System.exit(0);
    }
}

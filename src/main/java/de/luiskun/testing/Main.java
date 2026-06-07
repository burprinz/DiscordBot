package de.luiskun.testing;

import de.luiskun.APIKeys;
import de.luiskun.log.Log;
import de.luiskun.apis.MyAnimeListAPI;
import de.luiskun.myanimelist.objects.Anime;
import de.luiskun.myanimelist.objects.AnimeGenre;
import de.luiskun.myanimelist.objects.AnimeList;

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
        APIKeys.loadAPIKeys();
        /*
        LeagueAPI.downloadMatchesFromRegisteredUsers();
        StatsAPI.createAccountMatchlists();
        System.exit(0);
         */

        System.exit(0);
    }
}

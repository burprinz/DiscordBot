package de.luiskun.testing;

import de.luiskun.league.api.LeagueAPI;
import de.luiskun.league.objects.Matchlist;
import de.luiskun.league.objects.OverallMatchStats;
import de.luiskun.league.stats.MatchEditor;
import de.luiskun.league.stats.StatsAPI;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        LeagueAPI.downloadMatchesFromRegisteredUsers();
        StatsAPI.createAccountMatchlists();
        //OverallMatchStats stats = StatsAPI.getOverallMatchStats("fQkfLo2L890-QRluzPleewwVsqYhmEWYXFaF39pmLyLE1jt9Y1R1fOT3vg4D7dw-r9LPTKPxk54qfQ");
    }
}

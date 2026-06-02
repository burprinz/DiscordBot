package de.luiskun.league.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import de.luiskun.league.DataPaths;
import de.luiskun.league.api.LeagueAPI;
import de.luiskun.league.objects.AccountInfo;
import de.luiskun.league.objects.Match;
import de.luiskun.league.objects.MatchParticipant;
import de.luiskun.league.objects.Matchlist;
import de.luiskun.log.Log;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MatchEditor {

    private static Gson gson = new Gson();

    public static void createMatchList(String puuid) {

        List<String> matchlist = new ArrayList<>();

        File[] matchFiles = new File(DataPaths.LEAGUE_MATCHES).listFiles();
        int amFiles = matchFiles.length;

        for (int i = 0; i < matchFiles.length; i++) {

            File matchFile = matchFiles[i];
            Match match = null;
            try {
                match = gson.fromJson(new String(Files.readAllBytes(Paths.get(matchFile.getPath()))), Match.class);
            } catch (IOException e) {
                System.out.println("ERROR reading match infos" + e.getMessage());
            }

            if (match == null) {
                System.out.println("Error reading Match: Match is null! ID=" + matchFile.getName());
                continue;
            }

            for (MatchParticipant participant : match.getMatchParticipants()) {
                if (participant.puuid.equals(puuid)) {
                    matchlist.add(match.matchID);
                    break;
                }
            }

        }

        Matchlist mlist = new Matchlist();
        mlist.setMatchIDs(matchlist);

        String filePath = DataPaths.PLAYER_MATCH_LIST + puuid +  ".json";
        ObjectMapper om = new ObjectMapper();
        try {
            om.writeValue(new File(filePath), mlist);
        } catch (IOException e) {
            System.out.println("ERROR writing match list" + e.getMessage());
        }

        System.out.println("Found " + mlist.getMatchIDs().size() + " Matches for " + puuid);

    }

    public static List<String> getMatchIDList(String puuid) {
        Matchlist mlist = createAndGetMatchList(puuid);
        if (mlist == null) {
            return null;
        }
        return mlist.getMatchIDs();
    }

    private static Matchlist createAndGetMatchList(String puuid) {
        String path = DataPaths.PLAYER_MATCH_LIST + puuid + ".json";
        if (!Files.exists(Paths.get(path))) createMatchList(puuid);

        Matchlist list = null;
        try {
            list = gson.fromJson(new String(Files.readAllBytes(Paths.get(path))), Matchlist.class);
        } catch (IOException e) {
            System.out.println("ERROR reading match infos" + e.getMessage());
        }
        return list;
    }

}

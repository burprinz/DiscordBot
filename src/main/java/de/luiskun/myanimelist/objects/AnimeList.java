package de.luiskun.myanimelist.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimeList {

    public List<Anime> animes;

    public int[] statusCount;
    public int[] mediaTypeCount;

    public int[] episodesInStatus;
    public int[] timeInStatus;
    public int[] episodesInMedia;
    public int[] timeInMedia;

    public Map<Integer, String> genreNames;
    public Map<Integer, Integer> genreCount;
    public Map<Integer, Integer> genreEpisodes;
    public Map<Integer, Integer> genreTime;

    public int overallWatchedEpisodes;
    public int overallWatchedTime;

    public AnimeList() {
        animes = new ArrayList<>();
        statusCount = new int[5];
        mediaTypeCount = new int[8];

        episodesInStatus = new int[5];
        episodesInMedia = new int[8];

        timeInStatus = new int[5];
        timeInMedia = new int[8];

        genreNames = new HashMap<>();
        genreCount = new HashMap<>();
        genreEpisodes = new HashMap<>();
        genreTime = new HashMap<>();
    }

}

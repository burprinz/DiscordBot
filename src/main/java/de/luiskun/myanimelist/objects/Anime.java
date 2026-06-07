package de.luiskun.myanimelist.objects;

import java.util.List;

public class Anime {

    public static final int WATCHING = 0;
    public static final int COMPLETED = 1;
    public static final int ON_HOLD = 2;
    public static final int DROPPED = 3;
    public static final int PLAN_TO_WATCH = 4;
    public static final String[] STATUS_NAMES = {
      "Watching",
      "Completed",
      "On Hold",
      "Dropped",
      "Plan To Watch"
    };

    public static final int TV = 0;
    public static final int MOVIE = 1;
    public static final int OVA = 2;
    public static final int ONA = 3;
    public static final int SPECIAL = 4;
    public static final int MUSIC = 5;
    public static final int TV_SPECIAL = 6;
    public static final int CM = 7;

    public int id;
    public String title;
    public int mediaType;
    public  int num_episodes;
    public int avg_episode_length;
    public List<AnimeGenre> genres;
    public int status;
    public int score;
    public int episodes_watched;

}

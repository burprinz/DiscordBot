package de.luiskun.league.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Mastery {

    private static DateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");

    public String puuid;
    public int id;
    public String name;
    public int level;
    public int points;
    public long lastPlay;
    public int currentLevelPoints;
    public int remainingLevelPoints;
    public int neededPoints;
    public int earnedTokens;


    public Mastery() {

    }

    public Mastery(String puuid, int id, String name, int level, int points, long lastPlay, int currentLevelPoints, int remainingLevelPoints, int earnedTokens) {
        this.puuid = puuid;
        this.id = id;
        this.name = name;
        this.level = level;
        this.points = points;
        this.lastPlay = lastPlay;
        this.currentLevelPoints = currentLevelPoints;
        this.remainingLevelPoints = remainingLevelPoints;
        this.earnedTokens = earnedTokens;
        this.neededPoints = this.currentLevelPoints + this.remainingLevelPoints;
    }

    public String getDateString() {
        Date date = new Date(this.lastPlay);
        return timeFormat.format(date);
    }
}

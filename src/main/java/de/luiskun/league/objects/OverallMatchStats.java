package de.luiskun.league.objects;

import java.util.List;

public class OverallMatchStats {

    public String puuid;

    public int matchAmount;
    public double winrate;


    // Total Amounts
    public int totalKills;
    public int totalDeaths;
    public int totalAssists;
    public double totalKD;
    public int totalChampDamage;
    public int totalPings;
    public int wins;
    public int losses;
    public int surrenders;
    public int totalTimePlayed;
    public int totalTimeSpentDead;
    public int totalGoldEarned;
    public int totalGoldSpent;

    // Best
    public int mostKills = -1;
    public int deathsInMostKills;
    public int assistsInMostKills;
    public double kdInMostKills;

    public double bestKD = -1;
    public int killsInBestKD;
    public int deathsInBestKD;
    public int assistsInBestKD;

    // Worst
    public int mostDeaths = -1;
    public int killsInMostDeaths;
    public int assistsInMostDeaths;
    public double kdInMostDeaths;

    public double worstKD = 99999999;


    public double averageVisionScore;
    public int wardsPlaced;

    public int minionsKilled;
    public double averageMinionsKilled;

    // Roles
    public int topAmount;
    public int jungleAmount;
    public int midAmount;
    public int botAmount;

    // Multi-Kills
    public int doubleKills;
    public int tripleKills;
    public int quadraKills;
    public int pentaKills;

    public int spell1;
    public int spell2;
    public int spell3;
    public int spell4;




    public List<TeammateInfos> mostPlayedCompanions;

}

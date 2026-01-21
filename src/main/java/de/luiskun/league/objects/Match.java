package de.luiskun.league.objects;

import java.util.ArrayList;
import java.util.List;


public class Match {
    public String matchID;
    public long gameID;
    public String region;
    public String gameMode;
    public String gameName;
    public String gameVersion;
    public int mapID;

    public long startTime;
    public long endTime;
    public int gameduration;

    public String gameEndingType;

    public List<MatchTeam> teams;
    public List<MatchParticipant> matchParticipants;

    public Match() {
        this.teams = new ArrayList<>();
        this.matchParticipants = new ArrayList<>();
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }

    public long getGameID() {
        return gameID;
    }

    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public int getMapID() {
        return mapID;
    }

    public void setMapID(int mapID) {
        this.mapID = mapID;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getGameduration() {
        return gameduration;
    }

    public void setGameduration(int gameduration) {
        this.gameduration = gameduration;
    }

    public String getGameEndingType() {
        return gameEndingType;
    }

    public void setGameEndingType(String gameEndingType) {
        this.gameEndingType = gameEndingType;
    }

    public List<MatchTeam> getTeams() {
        return teams;
    }

    public void setTeams(List<MatchTeam> teams) {
        this.teams = teams;
    }

    public List<MatchParticipant> getMatchParticipants() {
        return matchParticipants;
    }

    public void setMatchParticipants(List<MatchParticipant> matchParticipants) {
        this.matchParticipants = matchParticipants;
    }
}

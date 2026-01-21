package de.luiskun.league.objects;

import java.util.ArrayList;
import java.util.List;

public class MatchTeam {

    public boolean winner;
    public List<Integer> bans;
    public List<TeamObjective> objectives;

    public MatchTeam() {
        this.bans = new ArrayList<>();
        this.objectives = new ArrayList<>();
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public List<Integer> getBans() {
        return bans;
    }

    public void setBans(List<Integer> bans) {
        this.bans = bans;
    }

    public List<TeamObjective> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<TeamObjective> objectives) {
        this.objectives = objectives;
    }
}

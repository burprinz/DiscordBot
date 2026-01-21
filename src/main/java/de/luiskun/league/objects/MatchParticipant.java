package de.luiskun.league.objects;

public class MatchParticipant {

    // Allgemein
    public String puuid;
    public int championId;
    public boolean gameEndedInEarlySurrender;
    public boolean gameEndedInSurrender;
    public String preferredPosition;
    public String actualPosition;
    public boolean won;
    public int timePlayed;
    public int totalTimeSpentDead;

    // Pings
    public int basicPings;
    public int allInPings;
    public int assistMePings;
    public int commandPings;
    public int dangerPings;
    public int enemyMissingPings;
    public int enemyVisionPings;
    public int getBackPings;
    public int holdPings;
    public int needVisionPings;
    public int onMyWayPings;
    public int pushPings;
    public int retreatPings;
    public int visionClearedPings;

    // Wards
    public int visionScore;
    public int wardsPlaced;
    public int detectorWardsPlaced;
    public int wardsKilled;

    // Economy
    public int visionWardsBoughtInGame;
    public int sightWardsBoughtInGame;
    public int consumablesPurchased;
    public int itemsPurchased;
    public int goldEarned;
    public int goldSpent;

    // DamageStats
    public int damageDealtToBuildings;
    public int damageDealtToObjectives;
    public int damageDealtToTurrets;
    public int magicDamageDealt;
    public int magicDamageDealtToChampions;
    public int magicDamageTaken;
    public int physicalDamageDealt;
    public int physicalDamageDealtToChampions;
    public int physicalDamageTaken;
    public int totalDamageDealt;
    public int totalDamageDealtToChampions;
    public int totalDamageTaken;
    public int trueDamageDealt;
    public int trueDamageDealtToChampions;
    public int trueDamageTaken;
    public int timeCCingOthers;
    public int totalTimeCCDealt;
    public int totalHeal;
    public int totalHealsOnTeammates;

    // Killstats
    public int kills;
    public int deaths;
    public int assists;
    public int totalAllyJungleMinionsKilled;
    public int totalEnemyJungleMinionsKilled;
    public int totalMinionsKilled;
    public int turretKills;
    public int turretTakedowns;
    public int turretsLost;
    public int baronKills;
    public int dragonKills;
    public int killingSprees;
    public int doubleKills;
    public int tripleKills;
    public int quadraKills;
    public int pentaKills;
    public int unrealKills;
    public int inhibitorKills;
    public int inhibitorTakedowns;
    public int inhibitorsLost;
    public int largestCriticalStrike;
    public int largestKillingSpree;
    public int largestMultiKill;
    public int longestTimeSpentLiving;
    public boolean firstBloodAssist;
    public boolean firstBloodKill;
    public boolean firstTowerAssist;
    public boolean firstTowerKill;

    // Itemids
    public int item0;
    public int item1;
    public int item2;
    public int item3;
    public int item4;
    public int item5;
    public int item6;

    // Spells
    public int spell1Casts;
    public int spell2Casts;
    public int spell3Casts;
    public int spell4Casts;

    public MatchParticipant() {

    }

    public String getPuuid() {
        return puuid;
    }

    public void setPuuid(String puuid) {
        this.puuid = puuid;
    }

    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public boolean isGameEndedInEarlySurrender() {
        return gameEndedInEarlySurrender;
    }

    public void setGameEndedInEarlySurrender(boolean gameEndedInEarlySurrender) {
        this.gameEndedInEarlySurrender = gameEndedInEarlySurrender;
    }

    public boolean isGameEndedInSurrender() {
        return gameEndedInSurrender;
    }

    public void setGameEndedInSurrender(boolean gameEndedInSurrender) {
        this.gameEndedInSurrender = gameEndedInSurrender;
    }

    public String getPreferredPosition() {
        return preferredPosition;
    }

    public void setPreferredPosition(String preferredPosition) {
        this.preferredPosition = preferredPosition;
    }

    public String getActualPosition() {
        return actualPosition;
    }

    public void setActualPosition(String actualPosition) {
        this.actualPosition = actualPosition;
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public int getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(int timePlayed) {
        this.timePlayed = timePlayed;
    }

    public int getTotalTimeSpentDead() {
        return totalTimeSpentDead;
    }

    public void setTotalTimeSpentDead(int totalTimeSpentDead) {
        this.totalTimeSpentDead = totalTimeSpentDead;
    }

    public int getBasicPings() {
        return basicPings;
    }

    public void setBasicPings(int basicPings) {
        this.basicPings = basicPings;
    }

    public int getAllInPings() {
        return allInPings;
    }

    public void setAllInPings(int allInPings) {
        this.allInPings = allInPings;
    }

    public int getAssistMePings() {
        return assistMePings;
    }

    public void setAssistMePings(int assistMePings) {
        this.assistMePings = assistMePings;
    }

    public int getCommandPings() {
        return commandPings;
    }

    public void setCommandPings(int commandPings) {
        this.commandPings = commandPings;
    }

    public int getDangerPings() {
        return dangerPings;
    }

    public void setDangerPings(int dangerPings) {
        this.dangerPings = dangerPings;
    }

    public int getEnemyMissingPings() {
        return enemyMissingPings;
    }

    public void setEnemyMissingPings(int enemyMissingPings) {
        this.enemyMissingPings = enemyMissingPings;
    }

    public int getEnemyVisionPings() {
        return enemyVisionPings;
    }

    public void setEnemyVisionPings(int enemyVisionPings) {
        this.enemyVisionPings = enemyVisionPings;
    }

    public int getGetBackPings() {
        return getBackPings;
    }

    public void setGetBackPings(int getBackPings) {
        this.getBackPings = getBackPings;
    }

    public int getHoldPings() {
        return holdPings;
    }

    public void setHoldPings(int holdPings) {
        this.holdPings = holdPings;
    }

    public int getNeedVisionPings() {
        return needVisionPings;
    }

    public void setNeedVisionPings(int needVisionPings) {
        this.needVisionPings = needVisionPings;
    }

    public int getOnMyWayPings() {
        return onMyWayPings;
    }

    public void setOnMyWayPings(int onMyWayPings) {
        this.onMyWayPings = onMyWayPings;
    }

    public int getPushPings() {
        return pushPings;
    }

    public void setPushPings(int pushPings) {
        this.pushPings = pushPings;
    }

    public int getRetreatPings() {
        return retreatPings;
    }

    public void setRetreatPings(int retreatPings) {
        this.retreatPings = retreatPings;
    }

    public int getVisionClearedPings() {
        return visionClearedPings;
    }

    public void setVisionClearedPings(int visionClearedPings) {
        this.visionClearedPings = visionClearedPings;
    }

    public int getVisionScore() {
        return visionScore;
    }

    public void setVisionScore(int visionScore) {
        this.visionScore = visionScore;
    }

    public int getWardsPlaced() {
        return wardsPlaced;
    }

    public void setWardsPlaced(int wardsPlaced) {
        this.wardsPlaced = wardsPlaced;
    }

    public int getDetectorWardsPlaced() {
        return detectorWardsPlaced;
    }

    public void setDetectorWardsPlaced(int detectorWardsPlaced) {
        this.detectorWardsPlaced = detectorWardsPlaced;
    }

    public int getWardsKilled() {
        return wardsKilled;
    }

    public void setWardsKilled(int wardsKilled) {
        this.wardsKilled = wardsKilled;
    }

    public int getVisionWardsBoughtInGame() {
        return visionWardsBoughtInGame;
    }

    public void setVisionWardsBoughtInGame(int visionWardsBoughtInGame) {
        this.visionWardsBoughtInGame = visionWardsBoughtInGame;
    }

    public int getSightWardsBoughtInGame() {
        return sightWardsBoughtInGame;
    }

    public void setSightWardsBoughtInGame(int sightWardsBoughtInGame) {
        this.sightWardsBoughtInGame = sightWardsBoughtInGame;
    }

    public int getConsumablesPurchased() {
        return consumablesPurchased;
    }

    public void setConsumablesPurchased(int consumablesPurchased) {
        this.consumablesPurchased = consumablesPurchased;
    }

    public int getItemsPurchased() {
        return itemsPurchased;
    }

    public void setItemsPurchased(int itemsPurchased) {
        this.itemsPurchased = itemsPurchased;
    }

    public int getGoldEarned() {
        return goldEarned;
    }

    public void setGoldEarned(int goldEarned) {
        this.goldEarned = goldEarned;
    }

    public int getGoldSpent() {
        return goldSpent;
    }

    public void setGoldSpent(int goldSpent) {
        this.goldSpent = goldSpent;
    }

    public int getDamageDealtToBuildings() {
        return damageDealtToBuildings;
    }

    public void setDamageDealtToBuildings(int damageDealtToBuildings) {
        this.damageDealtToBuildings = damageDealtToBuildings;
    }

    public int getDamageDealtToObjectives() {
        return damageDealtToObjectives;
    }

    public void setDamageDealtToObjectives(int damageDealtToObjectives) {
        this.damageDealtToObjectives = damageDealtToObjectives;
    }

    public int getDamageDealtToTurrets() {
        return damageDealtToTurrets;
    }

    public void setDamageDealtToTurrets(int damageDealtToTurrets) {
        this.damageDealtToTurrets = damageDealtToTurrets;
    }

    public int getMagicDamageDealt() {
        return magicDamageDealt;
    }

    public void setMagicDamageDealt(int magicDamageDealt) {
        this.magicDamageDealt = magicDamageDealt;
    }

    public int getMagicDamageDealtToChampions() {
        return magicDamageDealtToChampions;
    }

    public void setMagicDamageDealtToChampions(int magicDamageDealtToChampions) {
        this.magicDamageDealtToChampions = magicDamageDealtToChampions;
    }

    public int getMagicDamageTaken() {
        return magicDamageTaken;
    }

    public void setMagicDamageTaken(int magicDamageTaken) {
        this.magicDamageTaken = magicDamageTaken;
    }

    public int getPhysicalDamageDealt() {
        return physicalDamageDealt;
    }

    public void setPhysicalDamageDealt(int physicalDamageDealt) {
        this.physicalDamageDealt = physicalDamageDealt;
    }

    public int getPhysicalDamageDealtToChampions() {
        return physicalDamageDealtToChampions;
    }

    public void setPhysicalDamageDealtToChampions(int physicalDamageDealtToChampions) {
        this.physicalDamageDealtToChampions = physicalDamageDealtToChampions;
    }

    public int getPhysicalDamageTaken() {
        return physicalDamageTaken;
    }

    public void setPhysicalDamageTaken(int physicalDamageTaken) {
        this.physicalDamageTaken = physicalDamageTaken;
    }

    public int getTotalDamageDealt() {
        return totalDamageDealt;
    }

    public void setTotalDamageDealt(int totalDamageDealt) {
        this.totalDamageDealt = totalDamageDealt;
    }

    public int getTotalDamageDealtToChampions() {
        return totalDamageDealtToChampions;
    }

    public void setTotalDamageDealtToChampions(int totalDamageDealtToChampions) {
        this.totalDamageDealtToChampions = totalDamageDealtToChampions;
    }

    public int getTotalDamageTaken() {
        return totalDamageTaken;
    }

    public void setTotalDamageTaken(int totalDamageTaken) {
        this.totalDamageTaken = totalDamageTaken;
    }

    public int getTrueDamageDealt() {
        return trueDamageDealt;
    }

    public void setTrueDamageDealt(int trueDamageDealt) {
        this.trueDamageDealt = trueDamageDealt;
    }

    public int getTrueDamageDealtToChampions() {
        return trueDamageDealtToChampions;
    }

    public void setTrueDamageDealtToChampions(int trueDamageDealtToChampions) {
        this.trueDamageDealtToChampions = trueDamageDealtToChampions;
    }

    public int getTrueDamageTaken() {
        return trueDamageTaken;
    }

    public void setTrueDamageTaken(int trueDamageTaken) {
        this.trueDamageTaken = trueDamageTaken;
    }

    public int getTimeCCingOthers() {
        return timeCCingOthers;
    }

    public void setTimeCCingOthers(int timeCCingOthers) {
        this.timeCCingOthers = timeCCingOthers;
    }

    public int getTotalTimeCCDealt() {
        return totalTimeCCDealt;
    }

    public void setTotalTimeCCDealt(int totalTimeCCDealt) {
        this.totalTimeCCDealt = totalTimeCCDealt;
    }

    public int getTotalHeal() {
        return totalHeal;
    }

    public void setTotalHeal(int totalHeal) {
        this.totalHeal = totalHeal;
    }

    public int getTotalHealsOnTeammates() {
        return totalHealsOnTeammates;
    }

    public void setTotalHealsOnTeammates(int totalHealsOnTeammates) {
        this.totalHealsOnTeammates = totalHealsOnTeammates;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getTotalAllyJungleMinionsKilled() {
        return totalAllyJungleMinionsKilled;
    }

    public void setTotalAllyJungleMinionsKilled(int totalAllyJungleMinionsKilled) {
        this.totalAllyJungleMinionsKilled = totalAllyJungleMinionsKilled;
    }

    public int getTotalEnemyJungleMinionsKilled() {
        return totalEnemyJungleMinionsKilled;
    }

    public void setTotalEnemyJungleMinionsKilled(int totalEnemyJungleMinionsKilled) {
        this.totalEnemyJungleMinionsKilled = totalEnemyJungleMinionsKilled;
    }

    public int getTotalMinionsKilled() {
        return totalMinionsKilled;
    }

    public void setTotalMinionsKilled(int totalMinionsKilled) {
        this.totalMinionsKilled = totalMinionsKilled;
    }

    public int getTurretKills() {
        return turretKills;
    }

    public void setTurretKills(int turretKills) {
        this.turretKills = turretKills;
    }

    public int getTurretTakedowns() {
        return turretTakedowns;
    }

    public void setTurretTakedowns(int turretTakedowns) {
        this.turretTakedowns = turretTakedowns;
    }

    public int getTurretsLost() {
        return turretsLost;
    }

    public void setTurretsLost(int turretsLost) {
        this.turretsLost = turretsLost;
    }

    public int getBaronKills() {
        return baronKills;
    }

    public void setBaronKills(int baronKills) {
        this.baronKills = baronKills;
    }

    public int getDragonKills() {
        return dragonKills;
    }

    public void setDragonKills(int dragonKills) {
        this.dragonKills = dragonKills;
    }

    public int getKillingSprees() {
        return killingSprees;
    }

    public void setKillingSprees(int killingSprees) {
        this.killingSprees = killingSprees;
    }

    public int getDoubleKills() {
        return doubleKills;
    }

    public void setDoubleKills(int doubleKills) {
        this.doubleKills = doubleKills;
    }

    public int getTripleKills() {
        return tripleKills;
    }

    public void setTripleKills(int tripleKills) {
        this.tripleKills = tripleKills;
    }

    public int getQuadraKills() {
        return quadraKills;
    }

    public void setQuadraKills(int quadraKills) {
        this.quadraKills = quadraKills;
    }

    public int getPentaKills() {
        return pentaKills;
    }

    public void setPentaKills(int pentaKills) {
        this.pentaKills = pentaKills;
    }

    public int getUnrealKills() {
        return unrealKills;
    }

    public void setUnrealKills(int unrealKills) {
        this.unrealKills = unrealKills;
    }

    public int getInhibitorKills() {
        return inhibitorKills;
    }

    public void setInhibitorKills(int inhibitorKills) {
        this.inhibitorKills = inhibitorKills;
    }

    public int getInhibitorTakedowns() {
        return inhibitorTakedowns;
    }

    public void setInhibitorTakedowns(int inhibitorTakedowns) {
        this.inhibitorTakedowns = inhibitorTakedowns;
    }

    public int getInhibitorsLost() {
        return inhibitorsLost;
    }

    public void setInhibitorsLost(int inhibitorsLost) {
        this.inhibitorsLost = inhibitorsLost;
    }

    public int getLargestCriticalStrike() {
        return largestCriticalStrike;
    }

    public void setLargestCriticalStrike(int largestCriticalStrike) {
        this.largestCriticalStrike = largestCriticalStrike;
    }

    public int getLargestKillingSpree() {
        return largestKillingSpree;
    }

    public void setLargestKillingSpree(int largestKillingSpree) {
        this.largestKillingSpree = largestKillingSpree;
    }

    public int getLargestMultiKill() {
        return largestMultiKill;
    }

    public void setLargestMultiKill(int largestMultiKill) {
        this.largestMultiKill = largestMultiKill;
    }

    public int getLongestTimeSpentLiving() {
        return longestTimeSpentLiving;
    }

    public void setLongestTimeSpentLiving(int longestTimeSpentLiving) {
        this.longestTimeSpentLiving = longestTimeSpentLiving;
    }

    public boolean isFirstBloodAssist() {
        return firstBloodAssist;
    }

    public void setFirstBloodAssist(boolean firstBloodAssist) {
        this.firstBloodAssist = firstBloodAssist;
    }

    public boolean isFirstBloodKill() {
        return firstBloodKill;
    }

    public void setFirstBloodKill(boolean firstBloodKill) {
        this.firstBloodKill = firstBloodKill;
    }

    public boolean isFirstTowerAssist() {
        return firstTowerAssist;
    }

    public void setFirstTowerAssist(boolean firstTowerAssist) {
        this.firstTowerAssist = firstTowerAssist;
    }

    public boolean isFirstTowerKill() {
        return firstTowerKill;
    }

    public void setFirstTowerKill(boolean firstTowerKill) {
        this.firstTowerKill = firstTowerKill;
    }

    public int getItem0() {
        return item0;
    }

    public void setItem0(int item0) {
        this.item0 = item0;
    }

    public int getItem1() {
        return item1;
    }

    public void setItem1(int item1) {
        this.item1 = item1;
    }

    public int getItem2() {
        return item2;
    }

    public void setItem2(int item2) {
        this.item2 = item2;
    }

    public int getItem3() {
        return item3;
    }

    public void setItem3(int item3) {
        this.item3 = item3;
    }

    public int getItem4() {
        return item4;
    }

    public void setItem4(int item4) {
        this.item4 = item4;
    }

    public int getItem5() {
        return item5;
    }

    public void setItem5(int item5) {
        this.item5 = item5;
    }

    public int getItem6() {
        return item6;
    }

    public void setItem6(int item6) {
        this.item6 = item6;
    }

    public int getSpell1Casts() {
        return spell1Casts;
    }

    public void setSpell1Casts(int spell1Casts) {
        this.spell1Casts = spell1Casts;
    }

    public int getSpell2Casts() {
        return spell2Casts;
    }

    public void setSpell2Casts(int spell2Casts) {
        this.spell2Casts = spell2Casts;
    }

    public int getSpell3Casts() {
        return spell3Casts;
    }

    public void setSpell3Casts(int spell3Casts) {
        this.spell3Casts = spell3Casts;
    }

    public int getSpell4Casts() {
        return spell4Casts;
    }

    public void setSpell4Casts(int spell4Casts) {
        this.spell4Casts = spell4Casts;
    }
}

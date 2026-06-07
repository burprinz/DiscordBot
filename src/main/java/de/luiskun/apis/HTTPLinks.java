package de.luiskun.apis;

public class HTTPLinks {

    /**
     * LEAGUE
     */

    public static final String LEAGUE_ACCOUNT_BY_RIOT_ID = "https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/";

    public static final String LEAGUE_ACCOUNT_BY_PUUID = "https://europe.api.riotgames.com/riot/account/v1/accounts/by-puuid/";

    public static final String LEAGUE_SUMMONERS_BY_PUUID = "https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/";

    public static final String LEAGUE_MASTERIES_BY_PUUID = "https://euw1.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/";

    public static final String LEAGUE_MATCH_HISTORY_BY_PUUID = "https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/";

    public static final String LEAGUE_MATCH_DATA_BY_MATCH_ID = "https://europe.api.riotgames.com/lol/match/v5/matches/";


    /**
     * MYANIMELIST
     */

    public static final String MAL_BASE_USER_URL =  "https://api.myanimelist.net/v2/users/";
    public static final String MAL_STATS_SETTINGS = "/animelist?nsfw=true&fields=list_status,media_type,num_episodes,average_episode_duration,genres";

    public static String MAL_STATS(String username){
        return HTTPLinks.MAL_BASE_USER_URL + username + HTTPLinks.MAL_STATS_SETTINGS;
    }

    /**
     * GEOCODING
     */

    public static String WEATHER_COORDS(String city) {
        return "https://geocoding-api.open-meteo.com/v1/search?name="+city+"&count=1&language=de&format=json";
    }

    public static String WEATHER_BY_COORDS(int lat, int lon) {
        return "https://api.open-meteo.com/v1/forecast?latitude="+lat+"&longitude="+lon+"&hourly=temperature_2m,precipitation_probability&forecast_days=1";
    }

}


package de.luiskun.apis;

import de.luiskun.APIKeys;

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

    public static String LEAGUE_ACCOUNT_V4_BY_PUUID(String puuid) {
       return "https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-puuid/"
               +puuid
               +"?api_key="+ APIKeys.RIOT_API_KEY;
    }

    /**
     * MYANIMELIST
     */

    public static final String MAL_BASE_USER_URL =  "https://api.myanimelist.net/v2/users/";
    public static final String MAL_STATS_SETTINGS = "/animelist?nsfw=true&fields=list_status,media_type,num_episodes,average_episode_duration,genres";

    public static final String MAL_STATS_ONLY_ANIME_IDS = "/animelist?nsfw=true&fields=list_status";

    public static String MAL_STATS(String username, String fields){
        return HTTPLinks.MAL_BASE_USER_URL + username + fields;
    }

    /**
     * JIKAN
     */
    public static String ANIME_CHARACTER_LIST(int animeID) {
        return "https://api.jikan.moe/v4/anime/"+animeID+"/characters";
    }

    /**
     * GEOCODING
     */

    public static String CITY_COORDS(String city) {
        return "https://geocoding-api.open-meteo.com/v1/search?name="+city+"&count=1&language=de&format=json";
    }

    public static String WEATHER_BY_COORDS(double lat, double lon) {
        return "https://api.open-meteo.com/v1/forecast?latitude="+lat+"&longitude="+lon+"&hourly=temperature_2m,precipitation_probability&forecast_days=1";
    }

    /**
     * DDragon
     */
    public static final String DDRAGON_VERSIONS = "https://ddragon.leagueoflegends.com/api/versions.json";

    public static String DDRAGON_PROFILE_PIC(String apiVersion, String picID) {
        return "https://ddragon.leagueoflegends.com/cdn/"+apiVersion+"/img/profileicon/"+picID+".png";
    }

}


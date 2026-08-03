package de.luiskun.apis;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.luiskun.APIKeys;
import de.luiskun.log.Log;
import de.luiskun.myanimelist.objects.Anime;
import de.luiskun.myanimelist.objects.AnimeGenre;
import de.luiskun.myanimelist.objects.AnimeList;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class MyAnimeListAPI {

    private static final OkHttpClient client = new OkHttpClient();


    public static AnimeList getAnimeList(String username) {
        return jsonToAnimeList(getMyAnimeListJson(username, HTTPLinks.MAL_STATS_SETTINGS));
    }


    public static List<Character> getAnimeCharacters(int animeID) {
        String url = HTTPLinks.ANIME_CHARACTER_LIST(animeID);

        List<Character> result = new ArrayList<>();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String data =  response.body().string();

                System.out.println(data);
            } else {
                Log.error("Error getting infos: " + response.body().string());
                return null;
            }
        } catch (IOException e) {
            Log.error("Could not retrieve data: " + e.getMessage());
            return null;
        }

        return result;
    }


    public static List<Integer> getFinishedAnimeListIDs(String username) {
        return jsonToFinishedIDList(getMyAnimeListJson(username, HTTPLinks.MAL_STATS_ONLY_ANIME_IDS));
    }

    private static String getMyAnimeListJson(String username, String fields) {
        final int limit = 1000;
        int offset = 0;

        List<String> result = new ArrayList<>();
        while (true) {
            String url = HTTPLinks.MAL_STATS(username, fields)
                    +"&limit="+limit
                    +"&offset="+offset;

            Request request = new Request.Builder()
                    .url(url)
                    .header("X-MAL-CLIENT-ID", APIKeys.MAL_API_KEY)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String data =  response.body().string();
                    result.add(data);
                    System.out.println("Page " + (offset/1000 + 1) + " loaded, has next: " + data.contains("\"next\":\"https"));
                    if (!data.contains("\"next\"")) {
                        break;
                    }
                    offset += limit;
                } else {
                    Log.error("Error getting infos: " + response.body().string());
                    break;
                }
            } catch (IOException e) {
                Log.error("Could not retrieve data: " + e.getMessage());
                break;
            }
        }


        if (result.size() == 0) {
            return null;
        }

        return mergePages(result);
    }

    private static List<Integer> jsonToFinishedIDList(String json) {
        JsonObject object = JsonParser.parseString(json).getAsJsonObject();
        List<JsonElement> animes = object.get("data").getAsJsonArray().asList();
        List<Integer> result = new ArrayList<>();
        for (JsonElement je : animes) {
            JsonObject obj = je.getAsJsonObject();
            if (obj.get("list_status").getAsJsonObject().get("status").getAsString().equals("completed")) {
                result.add(obj.getAsJsonObject("node").get("id").getAsInt());
            }
        }
        return result;
    }

    private static AnimeList jsonToAnimeList(String json) {
        JsonObject object = JsonParser.parseString(json).getAsJsonObject();

        AnimeList animeList = new AnimeList();

        List<JsonElement> animes = object.get("data").getAsJsonArray().asList();
        for (JsonElement je : animes) {
            JsonObject obj = je.getAsJsonObject();

            // Data
            Anime anime = new Anime();

            JsonObject listStatus =  obj.getAsJsonObject("list_status");
            anime.status = getStatusID(listStatus.get("status").getAsString());
            anime.score = listStatus.get("score").getAsInt();
            anime.episodes_watched = listStatus.get("num_episodes_watched").getAsInt();

            JsonObject node =  obj.getAsJsonObject("node");
            anime.id = node.get("id").getAsInt();
            anime.title = node.get("title").getAsString();
            anime.mediaType = getMediaTypeID(node.get("media_type").getAsString());
            anime.num_episodes = node.get("num_episodes").getAsInt();
            anime.avg_episode_length = node.get("average_episode_duration").getAsInt();

            anime.genres = new ArrayList<>();
            List<JsonElement> genres = node.get("genres").getAsJsonArray().asList();
            for (JsonElement ge : genres) {
                JsonObject obj2 = ge.getAsJsonObject();

                AnimeGenre g = new AnimeGenre();
                g.id = obj2.get("id").getAsInt();
                g.name = obj2.get("name").getAsString();
                anime.genres.add(g);

                if (anime.status != Anime.PLAN_TO_WATCH) {
                    animeList.genreNames.putIfAbsent(g.id, g.name);
                    animeList.genreCount.put(g.id, animeList.genreCount.getOrDefault(g.id, 0) + 1);
                }
                animeList.genreEpisodes.put(g.id, animeList.genreEpisodes.getOrDefault(g.id, 0) + anime.episodes_watched);
                animeList.genreTime.put(g.id, animeList.genreTime.getOrDefault(g.id, 0) + anime.episodes_watched*anime.avg_episode_length);
            }

            animeList.animes.add(anime);

            // Stats
            animeList.statusCount[anime.status]++;
            animeList.episodesInStatus[anime.status]+=anime.episodes_watched;
            animeList.timeInStatus[anime.status]+=anime.episodes_watched*anime.avg_episode_length;
            if (anime.mediaType != -1) {
                animeList.mediaTypeCount[anime.mediaType]++;
                animeList.episodesInMedia[anime.mediaType]+=anime.episodes_watched;
                animeList.timeInMedia[anime.mediaType]+=anime.episodes_watched*anime.avg_episode_length;
            } else {
                System.out.println("MediaTypeID not found+ " + node.get("media_type").getAsString());
            }

            animeList.overallWatchedEpisodes += anime.episodes_watched;
            animeList.overallWatchedTime += anime.episodes_watched*anime.avg_episode_length;

        }

        animeList.genreCount = animeList.genreCount.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));


        return animeList;
    }

    private static int getMediaTypeID(String type) {
        switch (type) {
            case "tv":
                return Anime.TV;
            case "movie":
                return Anime.MOVIE;
            case "ova":
                return Anime.OVA;
            case "ona":
                return Anime.ONA;
            case "special":
                return Anime.SPECIAL;
            case "music":
                return Anime.MUSIC;
            case "tv_special":
                return Anime.TV_SPECIAL;
            case "cm":
                return Anime.CM;
            default:
                return -1;
        }
    }

    private static int getStatusID(String status) {
        switch (status) {
            case "watching":
                return Anime.WATCHING;
            case "completed":
                return Anime.COMPLETED;
            case "on_hold":
                return Anime.ON_HOLD;
            case "dropped":
                return Anime.DROPPED;
            case "plan_to_watch":
                return Anime.PLAN_TO_WATCH;
            default:
                return -1;
        }
    }

    private static String mergePages(List<String> pages) {
        StringBuilder allEntries = new StringBuilder();

        for (String page : pages) {
            // "data"-Array aus jeder Page extrahieren
            // Format: {"data":[...],"paging":{...}}
            int start = page.indexOf("[");
            int end = page.lastIndexOf("]");

            String data = page.substring(start + 1, end);

            if (!data.isEmpty()) {
                if (allEntries.length() > 0) {
                    allEntries.append(",");
                }
                allEntries.append(data);
            }
        }

        return "{\"data\":[" + allEntries + "]}";
    }

}

package de.luiskun;

import de.luiskun.log.Log;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class APIKeys {
    /*
     * Need to be loaded
     */
    public static String RIOT_API_KEY = "";
    public static String DISCORD_API_KEY = "";

    public static void loadAPIKeys() {
        try {
            RIOT_API_KEY = new String(Files.readAllBytes(Paths.get("apikeys/riot_api_key")));
        } catch (IOException e) {
            Log.error("Error loading Riot API key");
            e.printStackTrace();
        }
        try {
            DISCORD_API_KEY = new String(Files.readAllBytes(Paths.get("apikeys/discord_api_key")));
        } catch (IOException e) {
            Log.error("Error loading Discord API key");
            e.printStackTrace();
        }
        Log.success("Done loading API Keys");
    }

}

package de.luiskun.testing;

import de.luiskun.APIKeys;
import de.luiskun.apis.*;
import de.luiskun.league.objects.AccountInfoV4;
import de.luiskun.league.stats.StatsCalculator;
import de.luiskun.log.Log;
import de.luiskun.myanimelist.objects.Anime;
import de.luiskun.myanimelist.objects.AnimeGenre;
import de.luiskun.myanimelist.objects.AnimeList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    public Main() {

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                Log.save();
            }
        }));
        Log.start();
        APIKeys.loadAPIKeys();

        List<Character> c = MyAnimeListAPI.getAnimeCharacters(33363);
        
    }
}

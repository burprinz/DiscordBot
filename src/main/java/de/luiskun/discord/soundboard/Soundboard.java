package de.luiskun.discord.soundboard;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import de.luiskun.log.Log;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;

import java.util.Arrays;

public class Soundboard {

    private static AudioPlayerManager playerManager;
    private static AudioPlayer player;

    public static void init() {
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerLocalSource(playerManager);
        AudioSourceManagers.registerRemoteSources(playerManager);
        player = playerManager.createPlayer();
    }

    public static void play(Guild guild, AudioChannel channel, String filepath) {
        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(player));
        guild.getAudioManager().openAudioConnection(channel);
        playerManager.loadItem(filepath, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                player.playTrack(track);

                player.addListener(new AudioEventAdapter() {
                    @Override
                    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
                        guild.getAudioManager().closeAudioConnection();
                    }
                });
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                trackLoaded(playlist.getTracks().get(0));
            }

            @Override
            public void noMatches() {
                Log.error("sound nicht gefunden " + filepath);
                guild.getAudioManager().closeAudioConnection();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                Log.error("laden des sounds fehlgeschlagen: " + exception.getMessage());
                guild.getAudioManager().closeAudioConnection();
            }
        });
    }

}

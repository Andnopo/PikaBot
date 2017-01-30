package com.andnopo.PikaBot;

import com.andnopo.PikaBot.info.Command;
import com.andnopo.PikaBot.music.GuildMusicManager;
import com.andnopo.PikaBot.source.Youtube;
import com.andnopo.PikaBot.utils.EasyEmbedBuilder;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.audio.IAudioManager;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.andnopo.PikaBot.info.Keys;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.List;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Bot {
    List<String> ids = new ArrayList<String>();
    //List<String> ids = new ArrayList<String>();
    private static final Logger log = LoggerFactory.getLogger(Bot.class);
    public static IDiscordClient client;

    public static void main(String[] args) throws DiscordException {
        client = new ClientBuilder().withToken(Keys.botKey).setMaxReconnectAttempts(10).login();
        client.getDispatcher().registerListener(new Listener());
        client.getDispatcher().registerListener(new Bot());
    }

    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    private Bot() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(IGuild guild) {
        long guildId = Long.parseLong(guild.getID());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setAudioProvider(musicManager.getAudioProvider());

        return musicManager;
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) throws RateLimitException, DiscordException, MissingPermissionsException, IOException {
        IMessage message = event.getMessage();

        String[] command = message.getContent().split(" ", 2);
        IGuild guild = message.getGuild();

        if (guild != null) {
            if (String.valueOf(Command.getPrefix() + "play").equals(command[0]) && command.length >= 2) {

                JSONObject jsonf = new JSONObject(IOUtils.toString(new URL("https://www.googleapis.com/youtube/v3/search?part=id%2C+snippet&q=" + command[1].replace(" ", "+") + "&key" + Keys.ytKey), "UTF-8"));
                JSONObject j = jsonf.getJSONArray("items").getJSONObject(0).getJSONObject("snippet");

                Youtube.build(j.getString("title"), j.getString("channelTitle"), message.getAuthor(), event);

                ids.add(event.getMessage().getAuthor().getID());

                loadAndPlay(message.getChannel(), jsonf.getJSONArray("items").getJSONObject(0).getJSONObject("id").getString("videoId"));
            } else if (String.valueOf(Command.getPrefix() + "skip").equals(command[0])) {
                skipTrack(message.getChannel());
            }
        }


    }

    private void loadAndPlay(final IChannel channel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                sendMessageToChannel(channel, "Adding to queue " + track.getInfo().title);

                play(channel.getGuild(), musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                sendMessageToChannel(channel, "Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")");

                play(channel.getGuild(), musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                sendMessageToChannel(channel, "Nothing found by " + trackUrl);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                sendMessageToChannel(channel, "Could not play: " + exception.getMessage());
            }
        });
    }

    private void play(IGuild guild, GuildMusicManager musicManager, AudioTrack track) {
        connectToFirstVoiceChannel(guild.getAudioManager());

        JSONObject jsonf = null;
        try {
            jsonf = new JSONObject(IOUtils.toString(new URL("https://www.googleapis.com/youtube/v3/search?part=id%2C+snippet&q=" + track.getIdentifier().replace(" ", "+") + "&key" + Keys.ytKey), "UTF-8"));
            JSONObject j = jsonf.getJSONArray("items").getJSONObject(0).getJSONObject("snippet");

        } catch (IOException e) {
            e.printStackTrace();
        }
        IUser user = Bot.client.getUserByID(ids.get(0));
        JSONObject j = jsonf.getJSONArray("items").getJSONObject(0).getJSONObject("snippet");
        JSONObject obj = new JSONObject();
        obj.put("color", "#E62117");
        obj.put("title", j.getString("title"));
        obj.put("description", "By: " + j.getString("channelTitle"));
        obj.put("footer", "Requested by " + user.getName());
        obj.put("user", user.getID());


        try (FileWriter file = new FileWriter("info.json")) {

            file.write(obj.toString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        musicManager.scheduler.queue(track);
    }

    private void skipTrack(IChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        sendMessageToChannel(channel, "Skipped to next track.");
    }

    private void sendMessageToChannel(IChannel channel, String message) {
        try {
            channel.sendMessage(message);
        } catch (Exception e) {
            log.warn("Failed to send message {} to {}", message, channel.getName(), e);
        }
    }

    private static void connectToFirstVoiceChannel(IAudioManager audioManager) {
        for (IVoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
            if (voiceChannel.isConnected()) {
                return;
            }
        }

        for (IVoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {
            try {
                voiceChannel.join();
            } catch (MissingPermissionsException e) {
                log.warn("Cannot enter voice channel {}", voiceChannel.getName(), e);
            }
        }
    }

}

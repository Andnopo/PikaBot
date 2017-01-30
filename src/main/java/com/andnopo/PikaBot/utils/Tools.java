package com.andnopo.PikaBot.utils;

import org.json.JSONObject;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Status;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.audio.AudioPlayer;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

import java.io.FileWriter;
import java.io.IOException;

public class Tools {
    public static class music {
        public static float volume(MessageReceivedEvent event) {
            AudioPlayer handler = (AudioPlayer)event.getMessage().getGuild().getAudioManager();
            return handler.getVolume();
        }
        public static void changeVolume(MessageReceivedEvent event, float v) {
            AudioPlayer handler = (AudioPlayer)event.getMessage().getGuild().getAudioManager();
            handler.setVolume(v);
        }
    }
    public static class embed {
        public static void cngsong(String song, MessageReceivedEvent event) {
            event.getClient().changeStatus(Status.game(song));
        }
        public static void send(MessageReceivedEvent event, EmbedObject embed) throws RateLimitException, DiscordException, MissingPermissionsException {
            event.getMessage().getChannel().sendMessage("Currently Playing", embed, true);
        }
    }
    public static void register(String color, String title, String description, String footer, IUser user) {
        JSONObject obj = new JSONObject();
        obj.put("color", color);
        obj.put("title", title);
        obj.put("description", description);
        obj.put("footer", footer);
        obj.put("user", user.getID());


        try (FileWriter file = new FileWriter("info.json")) {

            file.write(obj.toString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

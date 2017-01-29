package com.andnopo.PikaBot.source;

import com.andnopo.PikaBot.info.Keys;
import com.andnopo.PikaBot.utils.EasyEmbedBuilder;
import com.andnopo.PikaBot.utils.Tools;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.IOException;
import java.net.URL;


public class Twitch {
    public static void play(String v, IUser user, MessageReceivedEvent event) throws IOException, RateLimitException, DiscordException, MissingPermissionsException {
        String id = v;
        JSONObject j = new JSONObject(IOUtils.toString(new URL("https://api.twitch.tv/kraken/channels/" + id + "?client_id=" + Keys.twKey), "UTF-8"));
        build(j.getString("display_name"), j.getString("status"), user, event);
    }

    public static void build(String song, String by, IUser user, MessageReceivedEvent event) throws RateLimitException, DiscordException, MissingPermissionsException {
        Tools.register("#E62117", song, "By: " + by, "Requested by " + user.getName(), user);
        Tools.embed.send(event, EasyEmbedBuilder.build("#6441A4", song, "Streaming: " + by, "Requested by " + user.getName(), user));
    }
}

package com.andnopo.PikaBot.source;

import com.andnopo.PikaBot.utils.EasyEmbedBuilder;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IUser;

public class Soundcloud {
    public static void play(String v) {
        //build();
    }

    public static EmbedObject build(String song, String by, IUser user) {
        return EasyEmbedBuilder.build("#FF5500", song, "By: " + by, "Requested by " + user.getName(), user);
    }
}

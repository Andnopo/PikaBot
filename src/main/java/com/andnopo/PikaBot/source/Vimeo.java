package com.andnopo.PikaBot.source;

import com.andnopo.PikaBot.utils.EasyEmbedBuilder;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

import java.io.IOException;

public class Vimeo {
    public static void play(String v, IUser user, MessageReceivedEvent event) throws IOException {
        //build();
    }

    public static EmbedObject build(String song, String by, IUser user) {
        return EasyEmbedBuilder.build("#37BEF2", song, "By: " + by, "Requested by " + user.getName(), user);
    }
}

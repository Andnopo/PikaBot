package com.andnopo.PikaBot.utils;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;

public class EasyEmbedBuilder {
    public static EmbedObject build(String color, String title, String description, String footer, IUser user) {
        return new EmbedBuilder().withColor(Color.decode(color)).withAuthorName("Pika Bot").withAuthorIcon("http://i.imgur.com/hFYlfGW.gif").withTitle(title).withDesc(description).withTimestamp(System.currentTimeMillis()).withFooterIcon(user.getAvatarURL()).withFooterText(footer).build();
    }
    public static EmbedObject help(String command) {
        return new EmbedBuilder().withColor(Color.decode("#848484")).withTitle("Unknown Command").withDesc(command).build();
    }
}

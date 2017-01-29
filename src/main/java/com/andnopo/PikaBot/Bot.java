package com.andnopo.PikaBot;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

import com.andnopo.PikaBot.info.Keys;

public class Bot {
    public static IDiscordClient client;
    public static void main(String[] args) throws DiscordException {
        client = new ClientBuilder().withToken(Keys.botKey).setMaxReconnectAttempts(10).login();
        client.getDispatcher().registerListener(new Listener());
    }
}

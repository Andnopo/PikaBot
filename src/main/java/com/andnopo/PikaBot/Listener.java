package com.andnopo.PikaBot;


import com.andnopo.PikaBot.info.Command;
import com.andnopo.PikaBot.source.Twitch;
import com.andnopo.PikaBot.source.Youtube;
import com.andnopo.PikaBot.utils.EasyEmbedBuilder;
import com.andnopo.PikaBot.utils.Tools;
import org.json.JSONArray;
import org.json.JSONObject;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.*;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Listener {
    @EventSubscriber
    public void onReady(ReadyEvent event) throws RateLimitException, DiscordException, IOException, ExecutionException, InterruptedException {
        System.out.println("Bot Status: Online");

        while (true) {
            Bot.client.changeStatus(Status.game(Bot.client.getGuilds().size() +" Guilds with " + Bot.client.getUsers().size() + " Members"));
            Thread.sleep(10000);
            Bot.client.changeStatus(Status.game("Do " + Command.getPrefix() + "join to join the server"));
            Thread.sleep(10000);
            Bot.client.changeStatus(Status.game("Do " + Command.getPrefix() + "invite to invite to get this bot"));
            Thread.sleep(10000);
        }
    }

    @EventSubscriber
    public static void onMessage(MessageReceivedEvent event) throws RateLimitException, DiscordException, MissingPermissionsException, IOException, InterruptedException, UnsupportedAudioFileException {
        IUser user = event.getMessage().getAuthor();
        String channel = event.getMessage().getChannel().getName();
        String guild = event.getMessage().getGuild().getName();
        String message = event.getMessage().getContent();
        String guildID = event.getMessage().getGuild().getID();
        String channelID = event.getMessage().getChannel().getID();
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();

                if (message.startsWith(Command.getPrefix()) && !user.isBot()) {
                    String m = message.replace(Command.getPrefix(), "");

                    if (m.toLowerCase().startsWith("help")) {
                        event.getMessage().getChannel().sendMessage("", new EmbedBuilder().withColor(Color.decode("#ffec23")).withAuthorName("Pika Bot").withAuthorIcon("http://i.imgur.com/hFYlfGW.gif").withTitle("\uD83D\uDD0A Help \uD83D\uDD0A").withDescription("The prefix is " + Command.getPrefix() + "\n\uD83D\uDDA5️Bot Commands\uD83D\uDDA5\n" + Command.getPrefix() + "help | Displays this.\n" + Command.getPrefix() + "info | Gives you info on the bot\n" + Command.getPrefix() + "invite | Gives you a invite link for the bot.\n" + Command.getPrefix() + "join | Gives you a link to join the bot server.\n" + Command.getPrefix() + "ping | Sees the bot response time\n\uD83D\uDD08 Music Commands \uD83D\uDD08\n" + Command.getPrefix() + "play | Finds for music on youtube based on your search").withFooterIcon(event.getMessage().getAuthor().getAvatarURL()).withFooterText("Requested by " + event.getMessage().getAuthor().getName()).withTimestamp(System.currentTimeMillis()).build(), false);

                        return;
                    }

                    if (m.toLowerCase().startsWith("info")) {
                        long uptime = rb.getUptime();
                        event.getMessage().getChannel().sendMessage("", new EmbedBuilder().withColor(Color.decode("#ffec23")).withAuthorName("Pika Bot").withAuthorIcon("http://i.imgur.com/hFYlfGW.gif").withDescription("⌛ Uptime " + String.valueOf(uptime) + "ms.\n\t\uD83D\uDCD6 Library | Discord4J | https://github.com/austinv11/Discord4J\n\uD83D\uDC64Owner | " + Bot.client.getUserByID("176427553169473546").mention() + " " + Bot.client.getUserByID("186627661219495936").mention() + "\n\uD83D\uDC65Devs | " + "\n").withFooterIcon(event.getMessage().getAuthor().getAvatarURL()).withFooterText("Requested by " + event.getMessage().getAuthor().getName()).withTimestamp(System.currentTimeMillis()).build(), false);
                        return;
                    }

                    if (m.toLowerCase().startsWith("invite")) {
                        event.getMessage().getChannel().sendMessage("", new EmbedBuilder().withColor(Color.decode("#ffec23")).withAuthorName("Pika Bot").withAuthorIcon("http://i.imgur.com/hFYlfGW.gif").withDescription("\uD83D\uDCBB Invite the bot \n https://discordapp.com/oauth2/authorize?client_id=226431294010163200&scope=bot&permissions=1278250048").withFooterIcon(event.getMessage().getAuthor().getAvatarURL()).withFooterText("Requested by " + event.getMessage().getAuthor().getName()).withTimestamp(System.currentTimeMillis()).build(), false);
                        return;
                    }

                    if (m.toLowerCase().startsWith("join")) {
                        event.getMessage().getChannel().sendMessage("", new EmbedBuilder().withColor(Color.decode("#ffec23")).withAuthorName("Pika Bot").withAuthorIcon("http://i.imgur.com/hFYlfGW.gif").withDescription("\uD83D\uDCBB Join the server \n https://discord.me/pikabot").withFooterIcon(event.getMessage().getAuthor().getAvatarURL()).withFooterText("Requested by " + event.getMessage().getAuthor().getName()).withTimestamp(System.currentTimeMillis()).build(), false);
                        return;
                    }

                    if (m.toLowerCase().startsWith("ping")) {
                        long uptime = rb.getUptime();
                        event.getMessage().getChannel().sendMessage("", new EmbedBuilder().withColor(Color.decode("#ffec23")).withAuthorName("Pika Bot").withAuthorIcon("http://i.imgur.com/hFYlfGW.gif").withDescription("\uD83C\uDFD3 PONG! " + String.valueOf(rb.getUptime() - uptime) + "ms.").withFooterIcon(event.getMessage().getAuthor().getAvatarURL()).withFooterText("Requested by " + event.getMessage().getAuthor().getName()).withTimestamp(System.currentTimeMillis()).build(), false);
                        return;
                    }

                    if (m.toLowerCase().startsWith("serverinfo")) {
                        event.getMessage().getChannel().sendMessage("", new EmbedBuilder().withColor(Color.decode("#ffec23")).withAuthorName("Pika Bot").withAuthorIcon("http://i.imgur.com/hFYlfGW.gif").withTitle(guild + " Info").withThumbnail(event.getMessage().getGuild().getIconURL()).withDescription("\uD83D\uDC64Owner | " + event.getMessage().getGuild().getOwner().mention()).withFooterIcon(event.getMessage().getAuthor().getAvatarURL()).withFooterText("Requested by " + event.getMessage().getAuthor().getName()).withTimestamp(System.currentTimeMillis()).build(), false);
                        return;
                    }

                    if (m.toLowerCase().startsWith("youtube")) {
                        String cmd = "youtube";
                        String arg = m.replace(cmd + " ", "");
                        Youtube.play(arg, user, event);
                        return;
                    }

                    if (m.toLowerCase().startsWith("twitch")) {
                        String cmd = "twitch";
                        String arg = m.toLowerCase().replace(cmd + " ", "");
                        Twitch.play(arg, user, event);
                        return;
                    }

                    if (m.toLowerCase().startsWith("play ")) {
                        String cmd = "play";
                        String arg = m.toLowerCase().replace(cmd + " ", "");
                        Youtube.find(arg, user, event);
                        return;
                    }

                    if (m.toLowerCase().startsWith("stop")) {
                        return;
                    }

                    if (m.toLowerCase().startsWith("playing")) {
                        try (FileReader file = new FileReader("info.json")) {

                            BufferedReader read = new BufferedReader(file);

                            String filer = read.readLine();

                            JSONObject jsonf = new JSONObject(filer);
                            String color = jsonf.getString("color");
                            String title = jsonf.getString("title");
                            String des = jsonf.getString("description");
                            String footer = jsonf.getString("footer");
                            String id = jsonf.getString("user");

                            Tools.embed.send(event, EasyEmbedBuilder.build(color, title, des, footer, Bot.client.getUserByID(id)));

                            read.close();
                            file.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                    if (m.toLowerCase().startsWith("queue")) {
                        try (FileReader file = new FileReader("queue.json")) {

                            BufferedReader read = new BufferedReader(file);

                            String filer = read.readLine();

                            List<String> ques = new ArrayList<String>();

                            JSONObject jsonf = new JSONObject(filer);
                            JSONArray list = jsonf.getJSONArray("queue");
                            for (int i = 0; i < list.length(); i++) {
                                String url = list.getJSONObject(i).getString("name");
                                ques.add(url);
                            }

                            if (ques.size() > 5) {
                                if (ques.size() == 6) {
                                    EmbedObject em = new EmbedBuilder().withColor(Color.decode("#2bffdb")).withTitle("Queue").withDesc("1⃣ - " + ques.get(0) + "\n2⃣ - " + ques.get(1) + "\n3⃣ - " + ques.get(2) + "\n4⃣ - " + ques.get(3) + "\n5⃣ - " + ques.get(4) + "\n...and " + String.valueOf(ques.size() - 5) + " more song").withTimestamp(System.currentTimeMillis()).withFooterIcon("http://i.imgur.com/hFYlfGW.gif").withFooterText(ques.size() + " total").build();
                                    event.getMessage().getChannel().sendMessage("", em, false);
                                }
                                if (ques.size() > 6) {
                                    EmbedObject em = new EmbedBuilder().withColor(Color.decode("#2bffdb")).withTitle("Queue").withDesc("1⃣ - " + ques.get(0) + "\n2⃣ - " + ques.get(1) + "\n3⃣ - " + ques.get(2) + "\n4⃣ - " + ques.get(3) + "\n5⃣ - " + ques.get(4) + "\n... " + String.valueOf(ques.size() - 5) + " more songs").withTimestamp(System.currentTimeMillis()).withFooterIcon("http://i.imgur.com/hFYlfGW.gif").withFooterText(ques.size() + " total").build();
                                    event.getMessage().getChannel().sendMessage("", em, false);
                                }
                            }

                            if (ques.size() == 5) {

                                EmbedObject em = new EmbedBuilder().withColor(Color.decode("#2bffdb")).withTitle("Queue").withDesc("1⃣ - " + ques.get(0) + "\n2⃣ - " + ques.get(1) + "\n3⃣ - " + ques.get(2) + "\n4⃣ - " + ques.get(3) + "\n5⃣ - " + ques.get(4)).withTimestamp(System.currentTimeMillis()).withFooterIcon("http://i.imgur.com/hFYlfGW.gif").withFooterText(ques.size() + " total").build();
                                event.getMessage().getChannel().sendMessage("", em, false);
                            }

                            read.close();
                            file.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                }
            }
}
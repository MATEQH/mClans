package dev.matthew.clans.util;

import com.google.common.collect.Maps;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomMessage {

    private static Map<String, ChatColor> colors = Maps.newConcurrentMap();

    static {
        for (ChatColor color : ChatColor.values()) {
            colors.put(color.name().toUpperCase(), color);
        }
    }

    private final String message;
    private final ComponentBuilder builder;
    private final Map<String, String> placeholders = Maps.newConcurrentMap();

    public CustomMessage(String message, Map<String, String> placeholders) {
        this.message = message;
        this.builder = new ComponentBuilder();
        Pattern msgPattern = Pattern.compile("<text (.*)>(.*)</text>");
        Matcher msgMatcher = msgPattern.matcher(message);
        while (msgMatcher.find()) {
//            System.out.println("msgMatcher - count - " + msgMatcher.groupCount());
//            System.out.println("msgMatcher - group 1 - " + msgMatcher.group(1));
//            System.out.println("msgMatcher - group 2 - " + msgMatcher.group(2));
            String replacedText = msgMatcher.group(2);
            Iterator<Map.Entry<String, String>> ph = placeholders.entrySet().iterator();
            while (ph.hasNext()) {

            }
            builder.append(msgMatcher.group(2));
            Pattern pattern = Pattern.compile("(hover|run|color)=\\\"([^\\\"]*)\\\"");
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                //builder.append(msgMatcher.group(2));
                if (matcher.group(1).equals("hover")) {
                    builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(matcher.group(2))));
//                    System.out.println("hover - " + matcher.group(2));
                } else if (matcher.group(1).equals("run")) {
                    builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, matcher.group(2)));
//                    System.out.println("run - " + matcher.group(2));
                } else if (matcher.group(1).equals("color")) {
                    if (colors.get(matcher.group(2).toUpperCase()) != null) {
                        builder.color(colors.get(matcher.group(2).toUpperCase()).asBungee());
                    }
//                    System.out.println("color - " + matcher.group(2) + " - " + (colors.get(matcher.group(2).toUpperCase()) != null));
                }
            }
        }
    }

    public String getMessage() {
        return message;
    }

    public ComponentBuilder getBuilder() {
        return builder;
    }

    public BaseComponent[] build() {
        return builder.create();
    }

    public void send(Player player) {
        player.spigot().sendMessage(build());
    }
}

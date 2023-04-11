package dev.matthew.clans.util;

import dev.matthew.clans.file.Config;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class StringUtil {

    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> translate(List<String> stringList) {
        return stringList.stream().map(StringUtil::translate).collect(Collectors.toList());
    }

    public static String formatMillisToChat(long durationInMillis) {
        float millis = (durationInMillis + 0.0f) / 1000.0f;
        long seconds = (durationInMillis / 1000) % 60;
        long minutes = (durationInMillis / (1000 * 60)) % 60;
        long hours = (durationInMillis / (1000 * 60 * 60)) % 24;

        if (durationInMillis < 60000) return Config.FORMAT.SECONDS.replaceAll("%seconds%", String.format("%1$." + 2 + "f", millis).replace(",", "."));
        if (durationInMillis < 3600000) return Config.FORMAT.MINUTES_SECONDS
                    .replaceAll("%minutes%", String.format("%2d", minutes))
                    .replaceAll("%seconds%", String.format("%2d", seconds));

        return Config.FORMAT.HOURS_MINUTES_SECONDS
                .replaceAll("%hours%", String.format("%2d", hours))
                .replaceAll("%minutes%", String.format("%2d", minutes))
                .replaceAll("%seconds%", String.format("%2d", seconds));
    }
}

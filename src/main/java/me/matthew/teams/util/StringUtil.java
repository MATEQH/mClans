package me.matthew.teams.util;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class StringUtil {

    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> translate(List<String> strings) {
        return strings.stream().map(string -> ChatColor.translateAlternateColorCodes('&', string)).collect(Collectors.toList());
    }

    public static List<String> uuidsToStrings(List<UUID> uuids) {
        return uuids.stream().map(uuid -> uuid.toString()).collect(Collectors.toList());
    }

    public static List<UUID> stringsToUuids(List<String> strings) {
        return strings.stream().map(string -> UUID.fromString(string)).collect(Collectors.toList());
    }
}

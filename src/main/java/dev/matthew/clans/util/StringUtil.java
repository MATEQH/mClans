package dev.matthew.clans.util;

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
}

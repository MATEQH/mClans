package com.github.mateqh.clans.util;

import org.bukkit.ChatColor;

public class StringUtil {

    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}

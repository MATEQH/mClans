package dev.matthew.clans.util;

import me.realized.duels.api.Duels;
import org.bukkit.plugin.PluginManager;

public class PluginHook {

    public static Duels DUELS;

    public static void init(PluginManager pluginManager) {
        if (pluginManager.isPluginEnabled("Duels")) {
            DUELS = (Duels) pluginManager.getPlugin("Duels");
        }
    }
}

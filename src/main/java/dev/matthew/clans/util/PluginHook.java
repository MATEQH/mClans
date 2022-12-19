package dev.matthew.clans.util;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class PluginHook {

    public static me.realized.duels.api.Duels DUELS;

    public static void init(PluginManager pluginManager) {
        if (pluginManager.isPluginEnabled("Duels")) {
            DUELS = (me.realized.duels.api.Duels) pluginManager.getPlugin("Duels");
        }
    }
}

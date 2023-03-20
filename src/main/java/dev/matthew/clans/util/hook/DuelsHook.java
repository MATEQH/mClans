package dev.matthew.clans.util.hook;

import me.realized.duels.api.Duels;
import org.bukkit.plugin.PluginManager;

public class DuelsHook {

    public static Duels DUELS;

    public static void init(PluginManager pluginManager) {
        DUELS = (me.realized.duels.api.Duels) pluginManager.getPlugin("Duels");
    }
}

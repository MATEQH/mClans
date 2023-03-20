package dev.matthew.clans.util;

import dev.matthew.clans.util.hook.DuelsHook;
import dev.matthew.clans.util.hook.VaultHook;
import org.bukkit.plugin.PluginManager;

public class PluginHook {

    public static boolean hasDuels = false, hasVault = false;

    public static void init(PluginManager pluginManager) {
        if (pluginManager.isPluginEnabled("Duels")) {
            DuelsHook.init(pluginManager);
            hasDuels = true;
        }
        if (pluginManager.isPluginEnabled("Vault")) {
            VaultHook.init(pluginManager);
            if (VaultHook.ECONOMY != null) hasVault = true;
        }
    }
}

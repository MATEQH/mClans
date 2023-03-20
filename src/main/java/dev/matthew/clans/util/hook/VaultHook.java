package dev.matthew.clans.util.hook;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    public static Economy ECONOMY;

    public static void init(PluginManager pluginManager) {
        ECONOMY = setupEconomy(pluginManager);
    }

    private static Economy setupEconomy(PluginManager pluginManager) {
        if (pluginManager.getPlugin("Vault") == null) {
            return null;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        return rsp == null ? null : rsp.getProvider();
    }
}

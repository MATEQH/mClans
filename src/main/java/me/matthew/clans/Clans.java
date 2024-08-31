package me.matthew.clans;

import org.bukkit.plugin.java.JavaPlugin;

public class Clans extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("The plugin enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("The plugin disabled.");
    }
}

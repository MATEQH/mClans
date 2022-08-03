package dev.matthew.clans;

import dev.matthew.clans.enums.ManagerType;
import dev.matthew.clans.file.Config;
import dev.matthew.clans.file.Message;
import dev.matthew.config.ConfigFile;
import lombok.Getter;
import dev.matthew.clans.command.implement.clan.ClanExecutor;
import dev.matthew.clans.clan.ClanExpansion;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.listener.PlayerListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Clans extends JavaPlugin {

    @Getter
    private static Clans instance;
    @Getter
    private ConfigFile configFile, messagesFile;
    @Getter
    private Economy economy;

    @Override
    public void onEnable() {
        instance = this;
        configFile = new ConfigFile(this, "config")
                .registerClass(Config.class)
                .export()
                .loadConfig()
                .setAnnotatedFields();
        messagesFile = new ConfigFile(this, "messages")
                .registerClass(Message.class)
                .export()
                .loadConfig()
                .setAnnotatedFields();
        economy = setupEconomy();
        ClanHandler.init(ManagerType.getManager(Config.DATABASE.TYPE, this));
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerListener(), this);
        if (pluginManager.isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("PlaceholderAPI found! Registering expansions.");
            new ClanExpansion().register();
        }
        if (pluginManager.isPluginEnabled("LunarClient-API")) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(this, new NametagTask(), 20L, 20L);
        }
        new ClanExecutor("clan").register();
    }

    @Override
    public void onDisable() {
        ClanHandler.getManager().close();
    }

    private Economy setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return null;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        return rsp == null ? null : rsp.getProvider();
    }
}

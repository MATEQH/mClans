package me.matthew.teams;

import lombok.Getter;
import me.matthew.teams.command.implement.team.TeamExecutor;
import me.matthew.teams.handler.manager.Manager;
import me.matthew.teams.listener.PlayerListener;
import me.matthew.teams.handler.team.TeamHandler;
import me.matthew.teams.handler.team.TeamExpansion;
import me.matthew.teams.handler.enums.ManagerType;
import me.matthew.teams.handler.enums.Config;
import me.matthew.teams.util.YamlFile;
import me.matthew.teams.handler.cache.NameCache;
import me.matthew.teams.handler.cache.implement.BukkitCache;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Teams extends JavaPlugin {

    @Getter
    private static Teams instance;
    @Getter
    private YamlFile configFile, messagesFile;
    @Getter
    private NameCache nameCache;
    @Getter
    private Manager manager;
    @Getter
    private Economy economy;

    @Override
    public void onEnable() {
        instance = this;
        configFile = new YamlFile(this, "config.yml").loadConfig(true);
        messagesFile = new YamlFile(this, "messages.yml").loadConfig(true);
        nameCache = new BukkitCache().init();
        manager = ManagerType.getManager(ManagerType.valueOf(Config.getString(Config.DATABASE_TYPE)), this).loadAll();
        TeamHandler.init(manager);
        economy = setupEconomy();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        new TeamExecutor("team").register();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new TeamExpansion().register();
        }
    }

    @Override
    public void onDisable() {

    }

    private Economy setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return null;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return null;
        }
        return rsp.getProvider();
    }
}

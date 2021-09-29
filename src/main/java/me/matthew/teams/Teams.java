package me.matthew.teams;

import lombok.Getter;
import me.matthew.teams.command.TeamExecutor;
import me.matthew.teams.handler.PlayerListener;
import me.matthew.teams.handler.TeamHandler;
import me.matthew.teams.handler.TeamExpansion;
import me.matthew.teams.handler.manager.ManagerType;
import me.matthew.teams.handler.manager.implement.FlatFileManager;
import me.matthew.teams.handler.manager.implement.MongoManager;
import me.matthew.teams.util.Config;
import me.matthew.teams.util.PlayerCache;
import me.matthew.teams.util.YamlFile;
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
    private Economy economy;

    @Override
    public void onEnable() {
        instance = this;
        configFile = new YamlFile(this, "config.yml").loadConfig(true);
        messagesFile = new YamlFile(this, "messages.yml").loadConfig(true);
        setupEconomy();
        PlayerCache.init();
        ManagerType managerType = ManagerType.valueOf(Config.getString(Config.DATABASE_TYPE));
        if (managerType == ManagerType.MONGO) {
            TeamHandler.init(new MongoManager(this));
        } else {
            TeamHandler.init(new FlatFileManager(this));
        }
        System.out.println("[" + getName() + "] " + "Using " + TeamHandler.getManager().getClass().getSimpleName() + " manager.");
        TeamHandler.getManager().loadAll();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        new TeamExecutor("team").register();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new TeamExpansion().register();
        }
    }

    @Override
    public void onDisable() {

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
}

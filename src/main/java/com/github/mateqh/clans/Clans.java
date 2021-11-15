package com.github.mateqh.clans;

import com.github.mateqh.clans.handler.enums.ManagerType;
import com.github.mateqh.clans.handler.file.Config;
import com.github.mateqh.clans.handler.file.Message;
import com.github.mateqh.config.ConfigFile;
import lombok.Getter;
import com.github.mateqh.clans.command.implement.clan.ClanExecutor;
import com.github.mateqh.clans.handler.clan.ClanExpansion;
import com.github.mateqh.clans.handler.clan.ClanHandler;
import com.github.mateqh.clans.listener.PlayerListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
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
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        new ClanExecutor("clan").register();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new ClanExpansion().register();
        }
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

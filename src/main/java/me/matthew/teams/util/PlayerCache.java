package me.matthew.teams.util;

import me.matthew.teams.Teams;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;

public class PlayerCache implements Listener {

    private static PlayerCache instance;
    private static final Map<String, String> nameMap = new HashMap<>();

    public static void init() {
        Bukkit.getPluginManager().registerEvents(getInstance(), Teams.getInstance());
        Executors.newCachedThreadPool().execute(() -> {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                String name = onlinePlayer.getName();
                nameMap.put(name.toUpperCase(), name);
            });
            Arrays.stream(Bukkit.getOfflinePlayers()).forEach(offlinePlayer -> {
                String name = offlinePlayer.getName();
                nameMap.put(name.toUpperCase(), name);
            });
        });
    }

    public static PlayerCache getInstance() {
        if (instance == null) {
            instance = new PlayerCache();
        }
        return instance;
    }

    public static OfflinePlayer getPlayer(String name) {
        name = name.toUpperCase();
        return nameMap.containsKey(name) ? Bukkit.getOfflinePlayer(nameMap.get(name)) : null;
    }

    public static OfflinePlayer getPlayer(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String name = event.getPlayer().getName();
        if (!nameMap.containsKey(name)) {
            nameMap.put(name.toUpperCase(), name);
        }
    }
}

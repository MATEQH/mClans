package me.matthew.teams.handler.cache.implement;

import me.matthew.teams.handler.cache.NameCache;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class BukkitCache implements NameCache {

    private final Map<String, String> nameMap = new HashMap<>();

    @Override
    public NameCache init() {
        // Load players async
        CompletableFuture.supplyAsync(() -> {
            Arrays.asList(Bukkit.getOfflinePlayers()).forEach(offlinePlayer -> nameMap.put(offlinePlayer.getName().toLowerCase(), offlinePlayer.getName()));
            Bukkit.getOnlinePlayers().forEach(player -> nameMap.put(player.getName().toLowerCase(), player.getName()));
            return null;
        });
        return this;
    }

    @Override
    public OfflinePlayer getPlayer(String name) {
        return nameMap.containsKey(name.toLowerCase()) ? Bukkit.getOfflinePlayer(nameMap.get(name.toLowerCase())) : null;
    }

    @Override
    public UUID getUuid(String name) {
        return getPlayer(name) != null ? getPlayer(name).getUniqueId() : null;
    }

    @Override
    public boolean ensure(String name) {
        return nameMap.get(name.toLowerCase()).equals(name);
    }

    @Override
    public void update(String name) {
        nameMap.put(name.toLowerCase(), name);
    }
}

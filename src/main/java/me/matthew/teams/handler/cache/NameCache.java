package me.matthew.teams.handler.cache;

import org.bukkit.OfflinePlayer;

import java.util.UUID;

public interface NameCache {

    NameCache init();
    OfflinePlayer getPlayer(String name);
    UUID getUuid(String name);
    boolean ensure(String name);
    void update(String name);

}

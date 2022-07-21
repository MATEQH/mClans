package dev.matthew.clans.clans.handler.clan;

import dev.matthew.clans.clans.handler.manager.Manager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ClanHandler {

    @Getter
    private static final Map<String, Clan> clanMap = new HashMap<>();
    @Getter
    private static final Map<UUID, Clan> playerMap = new HashMap<>();
    @Getter
    @Setter
    private static Manager manager;

    public static void init(Manager manager) {
        setManager(manager);
        manager.loadAll();
        System.out.println("[" + manager.getPlugin().getName() + "] " + "Using " + manager.getClass().getSimpleName() + " manager.");
    }

    public static Clan getByName(String name) {
        return clanMap.get(name.toLowerCase());
    }

    public static Clan getByUniqueId(UUID uniqueId) {
        return uniqueId == null ? null : playerMap.get(uniqueId);
    }

    public static Clan getByPlayer(Player player) {
        return player == null ? null : getByUniqueId(player.getUniqueId());
    }

    public static void createClan(Clan clan) {
        String name = clan.getName().toLowerCase();
        if (clanMap.containsKey(name)) {
            return;
        }
        clanMap.put(name, clan);
        clan.getMembers().keySet().forEach(uuid -> playerMap.put(uuid, clan));
        save(clan);
    }

    public static void removeClan(Clan clan) {
        String name = clan.getName().toLowerCase();
        if (!clanMap.containsKey(name)) {
            return;
        }
        clanMap.remove(name);
        clan.getMembers().keySet().forEach(uuid -> playerMap.put(uuid, null));
        remove(clan);
    }

    public static void save(Clan clan) {
        manager.save(clan);
    }

    public static void remove(Clan clan) {
        manager.remove(clan);
    }

    public static List<Clan> getSortedClans() {
        return clanMap.isEmpty() ? new ArrayList<>() : clanMap.values().stream().sorted(Comparator.comparing(clan -> ((Clan) clan).getPoints()).reversed()).collect(Collectors.toList());
    }
}

package dev.matthew.clans.clan;

import dev.matthew.clans.manager.Manager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ClanHandler {

    @Getter
    private static final Map<UUID, Clan> clanMap = new HashMap<>();
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
        return clanMap.values().stream().filter(clan -> clan.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static Clan getById(UUID id) {
        return clanMap.get(id);
    }

    public static UUID getIdByName(String name) {
        return getByName(name).getId();
    }

    public static Clan getByUniqueId(UUID uniqueId) {
        return uniqueId == null ? null : playerMap.get(uniqueId);
    }

    public static Clan getByPlayer(Player player) {
        return player == null ? null : getByUniqueId(player.getUniqueId());
    }

    public static void createClan(String name, UUID leader) {
        UUID id = UUID.randomUUID();
        while (clanMap.containsKey(id)) {
            id = UUID.randomUUID();
        }
        Clan clan = new Clan(id, name, leader);
        clanMap.put(id, clan);
        clan.getMembers().keySet().forEach(uuid -> playerMap.put(uuid, clan));
        save(clan);
    }

    public static void renameClan(Clan clan, String newName) {
        if (!clanMap.containsKey(clan.getId())) {
            return;
        }
        clan.setName(newName);
        save(clan);
    }

    public static void removeClan(Clan clan) {
        if (!clanMap.containsKey(clan.getId())) {
            return;
        }
        clanMap.remove(clan.getId());
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

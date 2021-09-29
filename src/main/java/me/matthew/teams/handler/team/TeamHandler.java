package me.matthew.teams.handler.team;

import lombok.Getter;
import lombok.Setter;
import me.matthew.teams.handler.manager.Manager;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class TeamHandler {

    @Getter
    private static Map<String, Team> teamMap = new HashMap<>();
    @Getter
    private static Map<UUID, Team> playerMap = new HashMap<>();
    @Getter
    @Setter
    private static Manager manager;
    @Getter
    private static List<Team> lastSortedTeam;

    public static void init(Manager manager) {
        setManager(manager);
        System.out.println("[" + manager.getPlugin().getName() + "] " + "Using " + manager.getClass().getSimpleName() + " manager.");
    }

    public static Team getByName(String name) {
        return teamMap.get(name.toLowerCase());
    }

    public static Team getByUniqueId(UUID uniqueId) {
        return playerMap.get(uniqueId);
    }

    public static Team getByPlayer(Player player) {
        return getByUniqueId(player.getUniqueId());
    }

    public static boolean createTeam(Team team) {
        String name = team.getName().toLowerCase();
        if (teamMap.containsKey(name)) {
            return false;
        }
        teamMap.put(name, team);
        team.getMembers().keySet().forEach(uuid -> playerMap.put(uuid, team));
        save(team);
        return true;
    }

    public static boolean removeTeam(Team team) {
        String name = team.getName().toLowerCase();
        if (!teamMap.containsKey(name)) {
            return false;
        }
        teamMap.remove(name);
        team.getMembers().keySet().forEach(uuid -> playerMap.put(uuid, null));
        remove(team);
        return true;
    }

    public static void save(Team team) {
        manager.save(team);
    }

    public static void remove(Team team) {
        manager.remove(team);
    }

    public static void sortTeams() {
        lastSortedTeam = teamMap.values().stream().sorted(Comparator.comparing(team -> ((Team) team).getPoints()).reversed()).collect(Collectors.toList());
    }
}

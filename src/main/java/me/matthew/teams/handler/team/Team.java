package me.matthew.teams.handler.team;

import com.google.gson.annotations.Expose;
import lombok.Data;
import me.matthew.teams.handler.enums.Role;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Team {

    @Expose
    private String name;
    @Expose
    private Map<UUID, Role> members = new LinkedHashMap<>();
    @Expose
    private int points = 0, kills = 0;
    @Expose
    private double balance = 0.0D;
    @Expose(deserialize = false, serialize = false)
    private Map<UUID, Long> invitedPlayers = new HashMap<>();

    public Team(String name, UUID leader) {
        this.name = name;
        this.members.put(leader, Role.LEADER);
    }

    public UUID getLeader() {
        return this.members.keySet().stream().filter(uuid -> members.get(uuid) == Role.LEADER).findFirst().get();
    }

    public List<UUID> getOnlineMembers() {
        return this.members.keySet().stream().filter(uuid -> Bukkit.getPlayer(uuid) != null).collect(Collectors.toList());
    }

    public List<UUID> getOnlineMembers(Player player) {
        return members.keySet().stream().filter(uuid -> Bukkit.getPlayer(uuid) != null && player.canSee(Bukkit.getPlayer(uuid))).collect(Collectors.toList());
    }

    public boolean isMember(UUID uniqueId) {
        return this.members.keySet().contains(uniqueId);
    }

    public boolean isMember(OfflinePlayer player) {
        return isMember(player.getUniqueId());
    }

    public boolean invited(UUID uniquedId) {
        return this.invitedPlayers.containsKey(uniquedId) && this.invitedPlayers.get(uniquedId).longValue() > System.currentTimeMillis();
    }

    public boolean invited(OfflinePlayer player) {
        return invited(player.getUniqueId());
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public void sendMessage(String message) {
        this.members.keySet().stream().filter(uuid -> Bukkit.getPlayer(uuid) != null).forEach(uuid -> Bukkit.getPlayer(uuid).sendMessage(message));
    }

    public Role getRole(UUID uniqueId) {
        return this.members.get(uniqueId);
    }

    public Role getRole(OfflinePlayer player) {
        return getRole(player.getUniqueId());
    }
}

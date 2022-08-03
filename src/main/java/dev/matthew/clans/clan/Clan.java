package dev.matthew.clans.clan;

import dev.matthew.clans.enums.Role;
import dev.matthew.clans.file.Message;
import dev.matthew.clans.util.ClassSerializer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class Clan implements ClassSerializer {

    private final String name;
    private Map<UUID, Role> members = new HashMap<>();
    private int kills = 0, points = 0;
    private double balance = 0;
    private final Map<UUID, Long> invitedPlayers = new HashMap<>();

    public Clan(String name, UUID leader) {
        this.name = name;
        this.members.put(leader, Role.LEADER);
    }

    public Clan(Document document) {
        this.name = document.getString("name");
        this.members.clear();
        Document membersDocument = document.get("members", Document.class);
        this.members = membersDocument.keySet().stream().collect(Collectors.toMap(
                UUID::fromString,
                member -> Role.valueOf(membersDocument.getString(member))
        ));
        this.kills = document.getInteger("kills");
        this.points = document.getInteger("points");
        this.balance = document.getDouble("balance");
    }

    @Override
    public Document serialize() {
        Document document = new Document();
        document.put("name", name);
        document.put("members", members.keySet().stream().collect(Collectors.toMap(
                UUID::toString,
                member -> members.get(member).toString())
        ));
        document.put("kills", kills);
        document.put("points", points);
        document.put("balance", balance);
        return document;
    }

    public UUID getLeader() {
        return this.members.keySet().stream().filter(uuid -> members.get(uuid) == Role.LEADER).findFirst().orElse(null);
    }

    public List<UUID> getOnlineMembersAsList() {
        return this.members.keySet().stream().filter(uuid -> Bukkit.getPlayer(uuid) != null).collect(Collectors.toList());
    }

    public List<UUID> getOnlineMembersAsList(Player player) {
        return members.keySet().stream().filter(uuid -> Bukkit.getPlayer(uuid) != null && player.canSee(Bukkit.getPlayer(uuid)))
                .sorted(Comparator.comparingInt(uuid -> Bukkit.getOfflinePlayer((UUID) uuid).isOnline() ? 1 : 0).reversed())
                .collect(Collectors.toList());
//        return members.keySet().stream().filter(uuid -> Bukkit.getPlayer(uuid) != null && player.canSee(Bukkit.getPlayer(uuid))).collect(Collectors.toList());
    }

    public List<UUID> getMembersAsList() {
        return members.keySet().stream()
                .sorted(Comparator.comparingInt(uuid -> Bukkit.getOfflinePlayer((UUID) uuid).isOnline() ? 1 : 0).reversed())
                .collect(Collectors.toList());
    }

    public List<UUID> getMembersAsList(Collection<Role> excludedRoles) {
        return members.keySet().stream().filter(uuid -> !excludedRoles.contains(members.get(uuid)))
                .sorted(Comparator.comparingInt(uuid -> Bukkit.getOfflinePlayer((UUID) uuid).isOnline() ? 1 : 0).reversed())
                .collect(Collectors.toList());
    }

    public boolean isMember(UUID uniqueId) {
        return this.members.containsKey(uniqueId);
    }

    public boolean isMember(OfflinePlayer player) {
        return isMember(player.getUniqueId());
    }

    public boolean invited(UUID uniquedId) {
        return this.invitedPlayers.containsKey(uniquedId) && this.invitedPlayers.get(uniquedId) > System.currentTimeMillis();
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

    public void sendColoredMessage(String message) {
        this.members.keySet().stream().filter(uuid -> Bukkit.getPlayer(uuid) != null).forEach(uuid -> Message.send(Bukkit.getPlayer(uuid), message));
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

    public String getName() {
        return name;
    }

    public Map<UUID, Role> getMembers() {
        return members;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public double getBalance() {
        return balance;
    }

    public Map<UUID, Long> getInvitedPlayers() {
        return invitedPlayers;
    }
}

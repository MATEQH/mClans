package dev.matthew.clans.command.implement.clan.argument;

import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.enums.Role;
import dev.matthew.clans.file.Config;
import dev.matthew.clans.file.Message;
import dev.matthew.clans.util.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class InfoArgument extends ExecutorArgument {

    public InfoArgument(String name) {
        super(name, "mclans.use", new String[]{"who"});
    }

    @Override
    public List<Role> getRoles() {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                Message.send(sender, Message.COMMAND_ONLY_FOR_PLAYERS);
                return true;
            }
            Clan clan = ClanHandler.getByPlayer((Player) sender);
            if (clan == null) {
                Message.send(sender, Message.NOT_IN_CLAN_SELF);
                return true;
            }
            printDetails(sender, clan);
        } else if (args.length == 2) {
            boolean found = false;
            Clan clanByName = ClanHandler.getByName(args[1]);
            if (clanByName != null) {
                found = true;
                Message.send(sender, Message.INFO_COMMAND.FOUND_BY.CLAN);
                printDetails(sender, clanByName);
            }
            OfflinePlayer target = BukkitUtil.getPlayer(args[1]);
            if (target != null) {
                Clan clanByPlayer = ClanHandler.getByUniqueId(target.getUniqueId());
                if (clanByPlayer != null) {
                    found = true;
                    Message.send(sender, Message.INFO_COMMAND.FOUND_BY.PLAYER);
                    printDetails(sender, clanByPlayer);
                }
            }
            if (!found) {
                Message.send(sender, Message.CLAN_DOES_NOT_EXISTS);
                return true;
            }
        } else {
            Message.send(sender, Message.INFO_COMMAND.USAGE.replaceAll("%label%", label));
        }
        return true;
    }

    public void printDetails(CommandSender sender, Clan clan) {
        StringJoiner membersJoiner = new StringJoiner("&7, ");
        StringJoiner captainsJoiner = new StringJoiner("&7, ");
        AtomicReference<String> leader = new AtomicReference<>();
        clan.getMembersAsList().forEach(uuid -> {
            if (clan.getRole(uuid) == Role.MEMBER) {
                membersJoiner.add(getPrefixedName(sender, Bukkit.getOfflinePlayer(uuid)));
            } else if (clan.getRole(uuid) == Role.CAPTAIN) {
                captainsJoiner.add(getPrefixedName(sender, Bukkit.getOfflinePlayer(uuid)));
            } else {
                leader.set(getPrefixedName(sender, Bukkit.getOfflinePlayer(uuid)));
            }
        });
        for (String message : Message.INFO_COMMAND.DETAILS) {
            if ((message.equals("%captains%") && captainsJoiner.toString().equals("")) || (message.equals("%members%") && membersJoiner.toString().equals(""))) {
                continue;
            }
            Message.send(sender, message
                    .replaceAll("%name%", clan.getName())
                    .replaceAll("%leader%", Message.INFO_COMMAND.LEADER.replaceAll("%leader%", leader.get()))
                    .replaceAll("%captains%", captainsJoiner.length() == 0 ? "" : Message.INFO_COMMAND.CAPTAINS.replaceAll("%captains%", captainsJoiner.toString()))
                    .replaceAll("%members%", membersJoiner.length() == 0 ? "" : Message.INFO_COMMAND.MEMBERS.replaceAll("%members%", membersJoiner.toString()))
                    .replaceAll("%balance%", String.valueOf(clan.getBalance()))
                    .replaceAll("%points%", String.valueOf(clan.getPoints()))
                    .replaceAll("%kills%", String.valueOf(clan.getKills()))
                    .replaceAll("%onlineSize%", String.valueOf(sender instanceof Player ?
                            clan.getOnlineMembersAsList((Player) sender).size() :
                            clan.getOnlineMembersAsList().size()
                    ))
                    .replaceAll("%size%", String.valueOf(clan.getMembers().size()))
            );
        }
    }

    public String getPrefixedName(CommandSender sender, OfflinePlayer offlinePlayer) {
        if (offlinePlayer.isOnline() && sender instanceof Player && ((Player) sender).canSee(offlinePlayer.getPlayer())) {
            return Config.ONLINE_PREFIX + offlinePlayer.getName();
        }
        return (offlinePlayer.isOnline() ? Config.ONLINE_PREFIX : Config.ONLINE_PREFIX) + offlinePlayer.getName();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return BukkitUtil.getCompletions(args, Bukkit.getOnlinePlayers().stream().filter(online -> ClanHandler.getByPlayer(online) != null).map(HumanEntity::getName).collect(Collectors.toList()));
    }
}

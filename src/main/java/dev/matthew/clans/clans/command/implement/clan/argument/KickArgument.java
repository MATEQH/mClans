package dev.matthew.clans.clans.command.implement.clan.argument;

import dev.matthew.clans.clans.command.ExecutorArgument;
import dev.matthew.clans.clans.handler.clan.Clan;
import dev.matthew.clans.clans.handler.clan.ClanHandler;
import dev.matthew.clans.clans.handler.enums.Role;
import dev.matthew.clans.clans.handler.file.Message;
import dev.matthew.clans.clans.util.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class KickArgument extends ExecutorArgument {

    public KickArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return Arrays.asList(Role.LEADER, Role.CAPTAIN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Message.send(sender, Message.COMMAND_ONLY_FOR_PLAYERS);
            return true;
        }
        Player player = (Player) sender;
        Clan clan = ClanHandler.getByPlayer(player);
        if (clan == null) {
            Message.send(player, Message.NOT_IN_CLAN_SELF);
            return true;
        }
        if (args.length != 2) {
            Message.send(player, Message.KICK_COMMAND.USAGE.replaceAll("%label%", label));
            return true;
        }
        if (clan.getRole(player) != Role.LEADER && clan.getRole(player) != Role.CAPTAIN) {
            Message.send(player, Message.CLAN_NO_PERMISSION.replaceAll("%role%", "captain or leader"));
            return true;
        }
        OfflinePlayer target = BukkitUtil.getPlayer(args[1]);
        if (target == null) {
            Message.send(player, Message.PLAYER_NOT_FOUND.replaceAll("%targetName%", args[1]));
            return true;
        }
        if (target.getUniqueId().equals(player.getUniqueId())) {
            Message.send(player, Message.CANNOT_DO_THIS_YOURSELF);
            return true;
        }
        if (!clan.isMember(target.getUniqueId())) {
            Message.send(player, Message.NOT_IN_CLAN_OTHERS.replaceAll("%targetName%", target.getName()));
            return true;
        }
        if (clan.getRole(player) == Role.LEADER) {
            if (clan.getRole(target) == Role.CAPTAIN) {
                clan.getMembers().put(target.getUniqueId(), Role.MEMBER);
            }
            clan.getMembers().remove(target.getUniqueId());
            ClanHandler.getPlayerMap().put(target.getUniqueId(), null);
            ClanHandler.save(clan);
            clan.sendColoredMessage(Message.KICK_COMMAND.KICKED_OTHERS.replaceAll("%targetName%", target.getName()));
            if (target.isOnline()) {
                Message.send(Bukkit.getPlayer(target.getUniqueId()), Message.KICK_COMMAND.KICKED_SELF.replaceAll("%name%", clan.getName()).replaceAll("%playerName%", player.getName()));
            }
        } else if (clan.getRole(player) == Role.CAPTAIN) {
            if (clan.getRole(target) == Role.LEADER || clan.getRole(target) == Role.CAPTAIN) {
                Message.send(player, Message.CLAN_NO_PERMISSION_ROLE);
                return true;
            }
            clan.getMembers().remove(target.getUniqueId());
            ClanHandler.getPlayerMap().put(target.getUniqueId(), null);
            ClanHandler.save(clan);
            clan.sendColoredMessage(Message.KICK_COMMAND.KICKED_OTHERS.replaceAll("%targetName%", target.getName()));
            if (target.isOnline()) {
                Message.send(Bukkit.getPlayer(target.getUniqueId()), Message.KICK_COMMAND.KICKED_SELF.replaceAll("%name%", clan.getName()).replaceAll("%playerName%", player.getName()));
            }
        } else {
            Message.send(player, Message.CLAN_NO_PERMISSION.replaceAll("%role%", "captain or leader"));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Clan clan = ClanHandler.getByPlayer(player);
        if (clan != null && (clan.getRole(player) == Role.LEADER || clan.getRole(player) == Role.CAPTAIN)) {
            if (args.length == 2) {
                if (clan.getRole(player) == Role.LEADER) {
                    return BukkitUtil.getCompletions(args, clan.getMembersAsList(Collections.singletonList(Role.LEADER)).stream().map(uuid -> Bukkit.getOfflinePlayer(uuid).getName()).collect(Collectors.toList()));
                } else if (clan.getRole(player) == Role.CAPTAIN) {
                    return BukkitUtil.getCompletions(args, clan.getMembersAsList(Arrays.asList(Role.LEADER, Role.CAPTAIN)).stream().map(uuid -> Bukkit.getOfflinePlayer(uuid).getName()).collect(Collectors.toList()));
                }
            }
        }
        return super.onTabComplete(sender, command, label, args);
    }
}

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

public class CaptainsArgument extends ExecutorArgument {

    public CaptainsArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return Collections.singletonList(Role.LEADER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Message.send(sender, Message.COMMAND_ONLY_FOR_PLAYERS);
            return  true;
        }
        Player player = (Player) sender;
        Clan clan = ClanHandler.getByPlayer(player);
        if (clan == null) {
            Message.send(player, Message.NOT_IN_CLAN_SELF);
            return true;
        }
        if (clan.getRole(player) != Role.LEADER) {
            Message.send(player, Message.MUST_BE_LEADER);
            return true;
        }
        if (args.length < 2) {
            Message.send(player, Message.CAPTAINS_COMMAND.USAGE.replaceAll("%label%", label));
            return true;
        }
        switch (args[1]) {
            case "add": {
                if (args.length != 3) {
                    Message.send(player, Message.CAPTAINS_COMMAND.USAGE.replaceAll("%label%", label));
                    return true;
                }
                OfflinePlayer target = BukkitUtil.getPlayer(args[2]);
                if (target == null) {
                    Message.send(player, Message.PLAYER_NOT_FOUND.replaceAll("%targetName%", args[2]));
                    return true;
                }
                if (target.getUniqueId().equals(player.getUniqueId())) {
                    Message.send(player, Message.CAPTAINS_COMMAND.CANNOT_PROMOTE_SELF);
                    return true;
                }
                if (!clan.isMember(target.getUniqueId())) {
                    Message.send(player, Message.NOT_IN_CLAN_OTHERS.replaceAll("%targetName%", target.getName()));
                    return true;
                }
                if (clan.getRole(target) == Role.CAPTAIN) {
                    Message.send(player, Message.CAPTAINS_COMMAND.ALREADY_ADDED.replaceAll("%targetName%", target.getName()));
                    return true;
                }
                clan.getMembers().put(target.getUniqueId(), Role.CAPTAIN);
                ClanHandler.save(clan);
                clan.sendColoredMessage(Message.CAPTAINS_COMMAND.ADDED.replaceAll("%targetName%", target.getName()).replaceAll("%playerName%", player.getName()));
                break;
            }
            case "remove": {
                if (args.length != 3) {
                    Message.send(player, Message.CAPTAINS_COMMAND.USAGE.replaceAll("%label%", label));
                    return true;
                }
                OfflinePlayer target = BukkitUtil.getPlayer(args[2]);
                if (target == null) {
                    Message.send(player, Message.PLAYER_NOT_FOUND.replaceAll("%targetName%", args[2]));
                    return true;
                }
                if (target.getUniqueId().equals(player.getUniqueId())) {
                    Message.send(player, Message.CAPTAINS_COMMAND.CANNOT_PROMOTE_SELF);
                    return true;
                }
                if (!clan.isMember(target.getUniqueId())) {
                    Message.send(player, Message.NOT_IN_CLAN_OTHERS.replaceAll("%targetName%", target.getName()));
                    return true;
                }
                if (clan.getRole(target) != Role.CAPTAIN) {
                    Message.send(player, Message.CAPTAINS_COMMAND.NOT_ADDED.replaceAll("%targetName%", target.getName()));
                    return true;
                }
                clan.getMembers().put(target.getUniqueId(), Role.MEMBER);
                ClanHandler.save(clan);
                clan.sendColoredMessage(Message.CAPTAINS_COMMAND.REMOVED.replaceAll("%targetName%", target.getName()).replaceAll("%playerName%", player.getName()));
                break;
            }
            default:
                Message.send(player, Message.CAPTAINS_COMMAND.USAGE.replaceAll("%label%", label));
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Clan clan = ClanHandler.getByPlayer(player);
        if (clan != null && clan.getRole(player) == Role.LEADER) {
            if (args.length == 2) {
                return BukkitUtil.getCompletions(args, Arrays.asList("add", "remove"));
            } else if (args.length == 3) {
                if (args[1].equalsIgnoreCase("add")) {
                    return BukkitUtil.getCompletions(args, clan.getMembersAsList(Arrays.asList(Role.LEADER, Role.CAPTAIN)).stream().map(uuid -> Bukkit.getOfflinePlayer(uuid).getName()).collect(Collectors.toList()));
                } else if (args[1].equalsIgnoreCase("remove")) {
                    return BukkitUtil.getCompletions(args, clan.getMembersAsList(Arrays.asList(Role.LEADER, Role.MEMBER)).stream().map(uuid -> Bukkit.getOfflinePlayer(uuid).getName()).collect(Collectors.toList()));
                }
            }
        }
        return super.onTabComplete(sender, command, label, args);
    }
}

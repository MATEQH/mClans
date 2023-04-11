package dev.matthew.clans.command.implement.clan.argument.leader;

import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.enums.Role;
import dev.matthew.clans.file.Message;
import dev.matthew.clans.util.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LeaderArgument extends ExecutorArgument {

    public LeaderArgument(String name) {
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
            return true;
        }
        Player player = (Player) sender;
        Clan clan = ClanHandler.getByPlayer(player);
        if (clan == null) {
            Message.send(player, Message.NOT_IN_CLAN_SELF);
            return true;
        }
        if (args.length != 2) {
            Message.send(player, Message.LEADER_COMMAND.USAGE.replaceAll("%label%", label));
            return true;
        }
        if (clan.getRole(player) != Role.LEADER) {
            Message.send(player, Message.MUST_BE_LEADER);
            return true;
        }
        OfflinePlayer target = BukkitUtil.getPlayer(args[1]);
        if (target == null) {
            Message.send(player, Message.PLAYER_NOT_FOUND.replaceAll("%targetName%", args[1]));
            return true;
        }
        if (target.getUniqueId().equals(player.getUniqueId())) {
            Message.send(player, Message.LEADER_COMMAND.ALREADY_LEADER);
            return true;
        }
        if (!clan.isMember(target.getUniqueId())) {
            Message.send(player, Message.NOT_IN_CLAN_OTHERS.replaceAll("%targetName%", target.getName()));
            return true;
        }
        clan.getMembers().put(target.getUniqueId(), Role.LEADER);
        clan.getMembers().put(player.getUniqueId(), Role.CAPTAIN);
        ClanHandler.save(clan);
        clan.sendColoredMessage(Message.LEADER_COMMAND.LEADER_CHANGED.replaceAll("%playerName%", player.getName()).replaceAll("%targetName%", target.getName()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Clan clan = ClanHandler.getByPlayer(player);
        if (args.length == 2 && clan != null && clan.getRole(player) == Role.LEADER && args.length == 2) {
            return BukkitUtil.getCompletions(args, clan.getMembersAsList(Collections.singletonList(Role.LEADER)).stream().map(uuid -> Bukkit.getOfflinePlayer(uuid).getName()).collect(Collectors.toList()));
        }
        return new ArrayList<>();
    }
}

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InviteArgument extends ExecutorArgument {

    public InviteArgument(String name) {
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
        if (clan.getRole(player) != Role.LEADER && clan.getRole(player) != Role.CAPTAIN) {
            Message.send(player, Message.CLAN_NO_PERMISSION.replaceAll("%role%", "captain or leader"));
            return true;
        }
        if (args.length != 2) {
            Message.send(player, Message.INVITE_COMMAND.USAGE.replaceAll("%label%", label));
            return true;
        }
        if (clan.getMembers().size() >= Config.CLAN.MAX_SIZE) {
            Message.send(player, Message.INVITE_COMMAND.CLAN_FULL.replaceAll("%name%", clan.getName()));
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
        Clan targetClan = ClanHandler.getByUniqueId(target.getUniqueId());
        if (targetClan != null) {
            if (targetClan.equals(clan)) {
                Message.send(player, Message.ALREADY_IN_CLAN_OTHERS.replaceAll("%targetName%", target.getName()));
                return true;
            }
            Message.send(player, Message.ALREADY_IN_CLAN.replaceAll("%targetName%", target.getName()));
            return true;
        }
        if (clan.invited(target.getUniqueId())) {
            Message.send(player, Message.INVITE_COMMAND.ALREADY_INVITED.replaceAll("%targetName%", target.getName()));
            return true;
        }
        clan.getInvitedPlayers().put(target.getUniqueId(), System.currentTimeMillis() + Config.CLAN.INVITATION_EXPIRE);
        Message.send(player, Message.INVITE_COMMAND.INVITED_OTHERS.replaceAll("%targetName%", target.getName()));
        if (target.isOnline()) {
            Message.send(Bukkit.getPlayer(target.getUniqueId()), Message.INVITE_COMMAND.INVITED_SELF.replaceAll("%name%", clan.getName()).replaceAll("%playerName%", player.getName()));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Clan clan = ClanHandler.getByPlayer(player);
        return (clan != null && (clan.getRole(player) == Role.LEADER || clan.getRole(player) == Role.CAPTAIN)) ? BukkitUtil.getCompletions(args, Bukkit.getOnlinePlayers().stream().filter(online -> !clan.isMember(online)).map(HumanEntity::getName).collect(Collectors.toList())) : new ArrayList<>();
    }
}

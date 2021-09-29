package me.matthew.teams.command.sub;

import me.matthew.teams.command.ExecutorArgument;
import me.matthew.teams.handler.Role;
import me.matthew.teams.handler.Team;
import me.matthew.teams.handler.TeamHandler;
import me.matthew.teams.util.BukkitUtil;
import me.matthew.teams.util.Messages;
import me.matthew.teams.util.PlayerCache;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
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
        Player player = (Player) sender;
        Team team = TeamHandler.getByPlayer(player);
        if (team == null) {
            player.sendMessage(Messages.get(Messages.NOT_IN_TEAM_SELF));
            return true;
        }
        if (args.length != 2) {
            player.sendMessage(Messages.get(Messages.KICK_COMMAND_USAGE).replace("%label%", label));
            return true;
        }
        if (team.getRole(player) != Role.LEADER && team.getRole(player) != Role.CAPTAIN) {
            player.sendMessage(Messages.get(Messages.TEAM_NO_PERMISSION).replace("%role%", "captain or leader"));
            return true;
        }
        OfflinePlayer target = PlayerCache.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(Messages.get(Messages.PLAYER_NOT_FOUND).replace("%targetName%", args[1]));
            return true;
        }
        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(Messages.get(Messages.CANNOT_DO_THIS_YOURSELF));
            return true;
        }
        if (!team.isMember(target.getUniqueId())) {
            player.sendMessage(Messages.get(Messages.NOT_IN_TEAM_OTHERS).replace("%targetName%", target.getName()));
            return true;
        }
        if (team.getRole(player) == Role.LEADER) {
            if (team.getRole(target) == Role.CAPTAIN) {
                team.getMembers().put(target.getUniqueId(), Role.MEMBER);
            }
            team.getMembers().remove(target.getUniqueId());
            TeamHandler.getPlayerMap().put(target.getUniqueId(), null);
            TeamHandler.save(team);
            team.sendMessage(Messages.get(Messages.KICK_COMMAND_KICKED_OTHERS).replace("%targetName%", target.getName()));
            if (target.isOnline()) {
                Bukkit.getPlayer(target.getUniqueId()).sendMessage(Messages.get(Messages.KICK_COMMAND_KICKED_SELF).replace("%teamName%", team.getName()).replace("%playerName%", player.getName()));
            }
        } else if (team.getRole(player) == Role.CAPTAIN) {
            if (team.getRole(target) == Role.LEADER || team.getRole(target) == Role.CAPTAIN) {
                player.sendMessage(Messages.get(Messages.TEAM_NO_PERMISSION_ROLE));
                return true;
            }
            team.getMembers().remove(target.getUniqueId());
            TeamHandler.getPlayerMap().put(target.getUniqueId(), null);
            TeamHandler.save(team);
            team.sendMessage(Messages.get(Messages.KICK_COMMAND_KICKED_OTHERS).replace("%targetName%", target.getName()));
            if (target.isOnline()) {
                Bukkit.getPlayer(target.getUniqueId()).sendMessage(Messages.get(Messages.KICK_COMMAND_KICKED_SELF).replace("%teamName%", team.getName()).replace("%playerName%", player.getName()));
            }
        } else {
            player.sendMessage(Messages.get(Messages.TEAM_NO_PERMISSION).replace("%role%", "captain or leader"));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player =  (Player) sender;
        Team team = TeamHandler.getByPlayer(player);
        if (team != null && (team.getRole(player) == Role.LEADER || team.getRole(player) == Role.CAPTAIN)) {
            if (args.length == 2) {
                if (team.getRole(player) == Role.LEADER) {
                    return BukkitUtil.getCompletions(args, team.getMembers().keySet().stream().filter(uuid -> team.getRole(uuid) != Role.LEADER).map(uuid -> Bukkit.getOfflinePlayer(uuid).getName()).collect(Collectors.toList()));
                } else if (team.getRole(player) == Role.CAPTAIN) {
                    return BukkitUtil.getCompletions(args, team.getMembers().keySet().stream().filter(uuid -> team.getRole(uuid) != Role.LEADER && team.getRole(uuid) != Role.CAPTAIN).map(uuid -> Bukkit.getOfflinePlayer(uuid).getName()).collect(Collectors.toList()));
                }
            }
        }
        return super.onTabComplete(sender, command, label, args);
    }
}

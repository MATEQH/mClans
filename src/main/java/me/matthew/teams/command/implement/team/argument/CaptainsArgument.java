package me.matthew.teams.command.implement.team.argument;

import me.matthew.teams.Teams;
import me.matthew.teams.command.ExecutorArgument;
import me.matthew.teams.handler.enums.Role;
import me.matthew.teams.handler.team.Team;
import me.matthew.teams.handler.team.TeamHandler;
import me.matthew.teams.util.BukkitUtil;
import me.matthew.teams.handler.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CaptainsArgument extends ExecutorArgument {

    public CaptainsArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return Arrays.asList(Role.LEADER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player =  (Player) sender;
        Team team = TeamHandler.getByPlayer(player);
        if (team == null) {
            player.sendMessage(Messages.get(Messages.NOT_IN_TEAM_SELF));
            return true;
        }
        if (team.getRole(player) != Role.LEADER) {
            player.sendMessage(Messages.get(Messages.MUST_BE_LEADER));
            return true;
        }
        if (args.length < 2) {
            player.sendMessage(Messages.get(Messages.CAPTAINS_COMMAND_USAGE).replace("%label%", label));
            return true;
        }
        switch (args[1]) {
            case "add": {
                if (args.length != 3) {
                    player.sendMessage(Messages.get(Messages.CAPTAINS_COMMAND_USAGE).replace("%label%", label));
                    return true;
                }
                OfflinePlayer target = Teams.getInstance().getNameCache().getPlayer(args[2]);
                if (target == null) {
                    player.sendMessage(Messages.get(Messages.PLAYER_NOT_FOUND).replace("%targetName%", args[2]));
                    return true;
                }
                if (target.getUniqueId().equals(player.getUniqueId())) {
                    player.sendMessage(Messages.get(Messages.CAPTAINS_COMMAND_CANNOT_PROMOTE_SELF));
                    return true;
                }
                if (!team.isMember(target.getUniqueId())) {
                    player.sendMessage(Messages.get(Messages.NOT_IN_TEAM_OTHERS).replace("%targetName%", target.getName()));
                    return true;
                }
                if (team.getRole(target) == Role.CAPTAIN) {
                    player.sendMessage(Messages.get(Messages.CAPTAINS_COMMAND_ALREADY_ADDED).replace("%targetName%", target.getName()));
                    return true;
                }
                team.getMembers().put(target.getUniqueId(), Role.CAPTAIN);
                TeamHandler.save(team);
                team.sendMessage(Messages.get(Messages.CAPTAINS_COMMAND_ADDED).replace("%targetName%", target.getName()).replace("%playerName%", player.getName()));
                break;
            }
            case "remove": {
                if (args.length != 3) {
                    player.sendMessage(Messages.get(Messages.CAPTAINS_COMMAND_USAGE).replace("%label%", label));
                    return true;
                }
                OfflinePlayer target = Teams.getInstance().getNameCache().getPlayer(args[2]);
                if (target == null) {
                    player.sendMessage(Messages.get(Messages.PLAYER_NOT_FOUND).replace("%targetName%", args[2]));
                    return true;
                }
                if (target.getUniqueId().equals(player.getUniqueId())) {
                    player.sendMessage(Messages.get(Messages.CAPTAINS_COMMAND_CANNOT_PROMOTE_SELF));
                    return true;
                }
                if (!team.isMember(target.getUniqueId())) {
                    player.sendMessage(Messages.get(Messages.NOT_IN_TEAM_OTHERS).replace("%targetName%", target.getName()));
                    return true;
                }
                if (team.getRole(target) != Role.CAPTAIN) {
                    player.sendMessage(Messages.get(Messages.CAPTAINS_COMMAND_NOT_ADDED).replace("%targetName%", target.getName()));
                    return true;
                }
                team.getMembers().put(target.getUniqueId(), Role.MEMBER);
                TeamHandler.save(team);
                team.sendMessage(Messages.get(Messages.CAPTAINS_COMMAND_REMOVED).replace("%targetName%", target.getName()).replace("%playerName%", player.getName()));
                break;
            }
            default:
                player.sendMessage(Messages.get(Messages.CAPTAINS_COMMAND_USAGE).replace("%label%", label));
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player =  (Player) sender;
        Team team = TeamHandler.getByPlayer(player);
        if (team != null && team.getRole(player) == Role.LEADER) {
            if (args.length == 2) {
                return BukkitUtil.getCompletions(args, Arrays.asList("add", "remove"));
            } else if (args.length == 3) {
                if (args[1].equalsIgnoreCase("add")) {
                    return BukkitUtil.getCompletions(args, team.getMembers().keySet().stream().filter(uuid -> team.getRole(uuid) != Role.LEADER && team.getRole(uuid) != Role.CAPTAIN).map(uuid -> Bukkit.getOfflinePlayer(uuid).getName()).collect(Collectors.toList()));
                } else if (args[1].equalsIgnoreCase("remove")) {
                    return BukkitUtil.getCompletions(args, team.getMembers().keySet().stream().filter(uuid -> team.getRole(uuid) == Role.CAPTAIN).map(uuid -> Bukkit.getOfflinePlayer(uuid).getName()).collect(Collectors.toList()));
                }
            }
        }
        return super.onTabComplete(sender, command, label, args);
    }
}

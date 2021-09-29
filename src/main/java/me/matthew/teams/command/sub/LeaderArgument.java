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

public class LeaderArgument extends ExecutorArgument {

    public LeaderArgument(String name) {
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
        if (args.length != 2) {
            player.sendMessage(Messages.get(Messages.LEADER_COMMAND_USAGE).replace("%label%", label));
            return true;
        }
        if (team.getRole(player) != Role.LEADER) {
            player.sendMessage(Messages.get(Messages.MUST_BE_LEADER));
            return true;
        }
        OfflinePlayer target = PlayerCache.getPlayer(args[1]);
        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(Messages.get(Messages.LEADER_COMMAND_ALREADY_LEADER));
            return true;
        }
        if (!team.isMember(target.getUniqueId())) {
            player.sendMessage(Messages.get(Messages.NOT_IN_TEAM_OTHERS).replace("%targetName%", target.getName()));
            return true;
        }
        team.getMembers().put(target.getUniqueId(), Role.LEADER);
        team.getMembers().put(player.getUniqueId(), Role.CAPTAIN);
        TeamHandler.save(team);
        team.sendMessage(Messages.get(Messages.LEADER_COMMAND_LEADER_CHANGED).replace("%playerName%", player.getName()).replace("%targetName%", target.getName()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player =  (Player) sender;
        Team team = TeamHandler.getByPlayer(player);
        if (team != null && team.getRole(player) == Role.LEADER && args.length == 2) {
            return BukkitUtil.getCompletions(args, team.getMembers().keySet().stream().filter(uuid -> team.getRole(uuid) != Role.LEADER).map(uuid -> Bukkit.getOfflinePlayer(uuid).getName()).collect(Collectors.toList()));
        }
        return super.onTabComplete(sender, command, label, args);
    }
}

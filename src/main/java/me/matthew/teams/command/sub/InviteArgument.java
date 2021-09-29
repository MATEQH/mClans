package me.matthew.teams.command.sub;

import me.matthew.teams.command.ExecutorArgument;
import me.matthew.teams.handler.Role;
import me.matthew.teams.handler.Team;
import me.matthew.teams.handler.TeamHandler;
import me.matthew.teams.util.BukkitUtil;
import me.matthew.teams.util.Config;
import me.matthew.teams.util.Messages;
import me.matthew.teams.util.PlayerCache;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
        Player player =  (Player) sender;
        Team team = TeamHandler.getByPlayer(player);
        if (team == null) {
            player.sendMessage(Messages.get(Messages.NOT_IN_TEAM_SELF));
            return true;
        }
        if (args.length != 2) {
            player.sendMessage(Messages.get(Messages.INVITE_COMMAND_USAGE).replace("%label%", label));
            return true;
        }
        if (team.getRole(player) != Role.LEADER && team.getRole(player) != Role.CAPTAIN) {
            player.sendMessage(Messages.get(Messages.TEAM_NO_PERMISSION).replace("%role%", "captain or leader"));
            return true;
        }
        if (team.getMembers().size() >= Config.getInt(Config.TEAM_SIZE)) {
            player.sendMessage(Messages.get(Messages.INVITE_COMMAND_TEAM_FULL).replace("%teamName%", team.getName()));
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
        Team targetTeam = TeamHandler.getByUniqueId(target.getUniqueId());
//        if (targetTeam.equals(team)) {
//            player.sendMessage(Message.get(Message.ALREADY_IN_TEAM_OTHERS).replace("%targetName%", target.getName()));
//            return true;
//        }
        if (targetTeam != null) {
            player.sendMessage(Messages.get(Messages.ALREADY_IN_TEAM).replace("%targetName%", target.getName()));
            return true;
        }
        if (team.invited(target.getUniqueId())) {
            player.sendMessage(Messages.get(Messages.INVITE_COMMAND_ALREADY_INVITED).replace("%targetName%", target.getName()));
            return true;
        }
        team.getInvitedPlayers().put(target.getUniqueId(), System.currentTimeMillis() + Config.getLong(Config.INVITATION_EXPIRE));
        player.sendMessage(Messages.get(Messages.INVITE_COMMAND_INVITED_OTHERS).replace("%targetName%", target.getName()));
        if (target.isOnline()) {
            Bukkit.getPlayer(target.getUniqueId()).sendMessage(Messages.get(Messages.INVITE_COMMAND_INVITED_SELF).replace("%teamName%", team.getName()).replace("%playerName%", player.getName()));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player =  (Player) sender;
        Team team = TeamHandler.getByPlayer(player);
        return (team != null && (team.getRole(player) == Role.LEADER || team.getRole(player) == Role.CAPTAIN)) ? BukkitUtil.getCompletions(args, Bukkit.getOnlinePlayers().stream().filter(online -> !team.isMember(online)).map(online -> online.getName()).collect(Collectors.toList())) : new ArrayList<>();
    }
}

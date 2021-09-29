package me.matthew.teams.command.implement.team.argument;

import me.matthew.teams.Teams;
import me.matthew.teams.command.ExecutorArgument;
import me.matthew.teams.handler.enums.Config;
import me.matthew.teams.handler.enums.Messages;
import me.matthew.teams.handler.enums.Role;
import me.matthew.teams.handler.team.Team;
import me.matthew.teams.handler.team.TeamHandler;
import me.matthew.teams.util.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
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
        Player player = (Player) sender;
        if (args.length == 1) {
            Team team = TeamHandler.getByPlayer(player);
            if (team == null) {
                player.sendMessage(Messages.get(Messages.NOT_IN_TEAM_SELF));
                return true;
            }
            printDetails(player, team);
        } else if (args.length == 2) {
            boolean found = false;
            Team teamByName = TeamHandler.getByName(args[1]), teamByPlayer;
            if (teamByName != null) {
                found = true;
                player.sendMessage(Messages.get(Messages.INFO_COMMAND_FOUND_BY_TEAM));
            }
            OfflinePlayer target = Teams.getInstance().getNameCache().getPlayer(args[1]);
            teamByPlayer = null;
            if (target != null) {
                teamByPlayer = TeamHandler.getByUniqueId(target.getUniqueId());
                if (teamByPlayer != null) {
                    found = true;
                    player.sendMessage(Messages.get(Messages.INFO_COMMAND_FOUND_BY_PLAYER));
                }
            }
            if (!found) {
                player.sendMessage(Messages.get(Messages.TEAM_DOES_NOT_EXISTS));
                return true;
            }
            if (teamByName != null) {
                printDetails(player, teamByName);
            }
            if (teamByPlayer != null) {
                printDetails(player, teamByPlayer);
            }
        } else {
            player.sendMessage(Messages.get(Messages.INFO_COMMAND_USAGE).replace("%label%", label));
        }
        return true;
    }

    public void printDetails(CommandSender sender, Team team) {
        List<UUID> members = team.getMembers().keySet().stream().filter(uuid -> team.getRole(uuid) != Role.LEADER && team.getRole(uuid) != Role.CAPTAIN).collect(Collectors.toList()),
                captains = team.getMembers().keySet().stream().filter(uuid -> team.getRole(uuid) == Role.CAPTAIN).collect(Collectors.toList());
        String membersString = "", captainsString = "";
        for (int i = 0; i < members.size(); i++) {
            OfflinePlayer member = Bukkit.getOfflinePlayer(members.get(i));
            membersString += Config.getString(member.isOnline() ? Config.ONLINE_PREFIX : Config.OFFLINE_PREFIX) + member.getName() + (i + 1 < members.size() ? "&7, " : "");
        }
        for (int i = 0; i < captains.size(); i++) {
            OfflinePlayer captain = Bukkit.getOfflinePlayer(captains.get(i));
            captainsString += Config.getString(captain.isOnline() ? Config.ONLINE_PREFIX : Config.OFFLINE_PREFIX) + captain.getName() + (i + 1 < captains.size() ? "&7, " : "");
        }
        OfflinePlayer leader = Bukkit.getOfflinePlayer(team.getLeader());
        String leaderString = Config.getString(leader.isOnline() ? Config.ONLINE_PREFIX : Config.OFFLINE_PREFIX) + leader.getName();
        for (String message : Messages.getList(Messages.INFO_COMMAND_DETAILS)) {
            if ((message.equals("%captains%") && captainsString.equals("")) || (message.equals("%members%") && membersString.equals(""))) {
                continue;
            }
            sender.sendMessage(message
                    .replace("%teamName%", team.getName())
                    .replace("%leader%", StringUtil.translate(Messages.get(Messages.INFO_COMMAND_LEADER).replace("%leader%", leaderString)))
                    .replace("%captains%", captainsString.equals("") ? "" : StringUtil.translate(Messages.get(Messages.INFO_COMMAND_CAPTAINS).replace("%captains%", captainsString)))
                    .replace("%members%", membersString.equals("") ? "" : StringUtil.translate(Messages.get(Messages.INFO_COMMAND_MEMBERS).replace("%members%", membersString)))
                    .replace("%balance%", team.getBalance() + "")
                    .replace("%points%", team.getPoints() + "")
                    .replace("%kills%", team.getKills() + "")
                    .replace("%onlineSize%", team.getOnlineMembers().size() + "")
                    .replace("%teamSize%", team.getMembers().size() + ""));
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return BukkitUtil.getCompletions(args, Bukkit.getOnlinePlayers().stream().filter(online -> TeamHandler.getByPlayer(online) != null).map(online -> online.getName()).collect(Collectors.toList()));
    }

}

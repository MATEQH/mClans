package me.matthew.teams.command.sub;

import me.matthew.teams.command.ExecutorArgument;
import me.matthew.teams.handler.Role;
import me.matthew.teams.handler.Team;
import me.matthew.teams.handler.TeamHandler;
import me.matthew.teams.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class LeaveArgument extends ExecutorArgument {

    public LeaveArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return Arrays.asList(Role.CAPTAIN, Role.MEMBER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Team team = TeamHandler.getByPlayer(player);
        if (team == null) {
            player.sendMessage(Messages.get(Messages.NOT_IN_TEAM_SELF));
            return true;
        }
        if (team.getRole(player) == Role.LEADER) {
            player.sendMessage(Messages.get(Messages.LEAVE_COMMAND_LEADER_LEAVE));
            return true;
        }
        team.getMembers().remove(player.getUniqueId());
        TeamHandler.getPlayerMap().put(player.getUniqueId(), null);
        TeamHandler.save(team);
        player.sendMessage(Messages.get(Messages.LEAVE_COMMAND_LEFT_SELF).replace("%teamName%", team.getName()));
        team.sendMessage(Messages.get(Messages.LEAVE_COMMAND_LEFT_OTHERS).replace("%playerName%", player.getName()));
        return true;
    }
}

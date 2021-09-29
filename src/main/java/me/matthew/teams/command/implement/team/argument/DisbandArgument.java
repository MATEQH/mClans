package me.matthew.teams.command.implement.team.argument;

import me.matthew.teams.command.ExecutorArgument;
import me.matthew.teams.handler.enums.Role;
import me.matthew.teams.handler.team.Team;
import me.matthew.teams.handler.team.TeamHandler;
import me.matthew.teams.handler.enums.Config;
import me.matthew.teams.handler.enums.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class DisbandArgument extends ExecutorArgument {

    public DisbandArgument(String name) {
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
        TeamHandler.removeTeam(team);
        TeamHandler.sortTeams();
//            player.sendMessage(Message.get(Message.DISBAND_SUCCESS).replace("%name%", team.getName()));
        if (Config.getBoolean(Config.DISBAND_BROADCAST)) {
            Bukkit.broadcastMessage(Messages.get(Messages.DISBAND_COMMAND_DISBANDED).replace("%teamName%", team.getName()).replace("%playerName%", player.getName()));
        } else {
            player.sendMessage(Messages.get(Messages.DISBAND_COMMAND_DISBANDED).replace("%teamName%", team.getName()).replace("%playerName%", player.getName()));
        }
        return true;
    }
}

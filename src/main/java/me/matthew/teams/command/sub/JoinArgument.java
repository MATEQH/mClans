package me.matthew.teams.command.sub;

import me.matthew.teams.command.ExecutorArgument;
import me.matthew.teams.handler.Role;
import me.matthew.teams.handler.Team;
import me.matthew.teams.handler.TeamHandler;
import me.matthew.teams.util.Config;
import me.matthew.teams.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class JoinArgument extends ExecutorArgument {

    public JoinArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Team team = TeamHandler.getByPlayer(player);
        if (team != null) {
            player.sendMessage(Messages.get(Messages.ALREADY_IN_TEAM_SELF));
            return true;
        }
        if (args.length != 2) {
            player.sendMessage(Messages.get(Messages.JOIN_COMMAND_USAGE).replace("%label%", label));
            return true;
        }
        team = TeamHandler.getByName(args[1]);
        if (team == null) {
            player.sendMessage(Messages.get(Messages.TEAM_DOES_NOT_EXISTS));
            return true;
        }
        if (!team.invited(player)) {
            player.sendMessage(Messages.get(Messages.JOIN_COMMAND_NOT_INVITED).replace("%teamName%", team.getName()));
            return true;
        }
        if (team.getMembers().size() >= Config.getInt(Config.TEAM_SIZE)) {
            player.sendMessage(Messages.get(Messages.JOIN_COMMAND_TEAM_FULL).replace("%teamName%", team.getName()));
            return true;
        }
        team.getMembers().put(player.getUniqueId(), Role.MEMBER);
        team.getInvitedPlayers().remove(player.getUniqueId());
        TeamHandler.getPlayerMap().put(player.getUniqueId(), team);
        TeamHandler.save(team);
        team.sendMessage(Messages.get(Messages.JOIN_COMMAND_JOINED).replace("%playerName%", player.getName()));
        return true;
    }
}

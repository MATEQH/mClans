package me.matthew.teams.command.sub;

import me.matthew.teams.command.ExecutorArgument;
import me.matthew.teams.handler.Role;
import me.matthew.teams.handler.Team;
import me.matthew.teams.handler.TeamHandler;
import me.matthew.teams.util.Config;
import me.matthew.teams.util.Messages;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateArgument extends ExecutorArgument {

    public CreateArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;
        Team team = TeamHandler.getByPlayer((Player) sender);
        if (team != null) {
            player.sendMessage(Messages.get(Messages.ALREADY_IN_TEAM_SELF));
            return true;
        }
        if (args.length < 2) {
            player.sendMessage(Messages.get(Messages.CREATE_COMMAND_USAGE).replace("%label%", label));
            return true;
        }
        String name = args[1];
        int value = Config.getInt(Config.NAME_MIN_LENGTH);
        if (name.length() < value) {
            player.sendMessage(Messages.get(Messages.NAME_TOO_SHORT).replace("%teamName%", name));
            return true;
        }
        value = Config.getInt(Config.NAME_MAX_LENGTH);
        if (name.length() > value) {
            player.sendMessage(Messages.get(Messages.NAME_TOO_LONG).replace("%teamName%", name));
            return true;
        }
        if (!StringUtils.isAlphanumeric(name)) {
            player.sendMessage(Messages.get(Messages.NAME_NOT_ALPHANUMERIC).replace("%teamName%", name));
            return true;
        }
        if (TeamHandler.getByName(name) != null) {
            player.sendMessage(Messages.get(Messages.TEAM_ALREADY_EXISTS).replace("%teamName%", name));
            return true;
        }
        team = new Team(name, player.getUniqueId());
        TeamHandler.createTeam(team);
        TeamHandler.sortTeams();
        if (Config.getBoolean(Config.CREATE_BROADCAST)) {
            Bukkit.broadcastMessage(Messages.get(Messages.CREATE_COMMAND_CREATED).replace("%teamName%", args[1]).replace("%playerName%", player.getName()));
        } else {
            player.sendMessage(Messages.get(Messages.CREATE_COMMAND_CREATED).replace("%teamName%", args[1]).replace("%playerName%", player.getName()));
        }
        return true;
    }
}

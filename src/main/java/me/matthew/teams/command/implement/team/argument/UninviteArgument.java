package me.matthew.teams.command.implement.team.argument;

import me.matthew.teams.Teams;
import me.matthew.teams.command.ExecutorArgument;
import me.matthew.teams.handler.enums.Role;
import me.matthew.teams.handler.team.Team;
import me.matthew.teams.handler.team.TeamHandler;
import me.matthew.teams.handler.enums.Messages;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class UninviteArgument extends ExecutorArgument {

    public UninviteArgument(String name) {
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
            player.sendMessage(Messages.get(Messages.UNINVITE_COMMAND_USAGE).replace("%label%", label));
            return true;
        }
        if (team.getRole(player) != Role.LEADER && team.getRole(player) != Role.CAPTAIN) {
            player.sendMessage(Messages.get(Messages.TEAM_NO_PERMISSION).replace("%role%", "captain or leader"));
            return true;
        }
        OfflinePlayer target = Teams.getInstance().getNameCache().getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(Messages.get(Messages.PLAYER_NOT_FOUND).replace("%targetName%", args[1]));
            return true;
        }
        if (target.getUniqueId().equals(player.getUniqueId())) {
            player.sendMessage(Messages.get(Messages.CANNOT_DO_THIS_YOURSELF));
            return true;
        }
        if (!team.invited(target.getUniqueId())) {
            player.sendMessage(Messages.get(Messages.UNINVITE_COMMAND_NOT_INVITED).replace("%targetName%", target.getName()));
            return true;
        }
        team.getInvitedPlayers().remove(target.getUniqueId());
        player.sendMessage(Messages.get(Messages.UNINVITE_COMMAND_UNINVITED).replace("%targetName%", target.getName()));
        return true;
    }
}

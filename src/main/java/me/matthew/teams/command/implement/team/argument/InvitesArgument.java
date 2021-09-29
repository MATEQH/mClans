package me.matthew.teams.command.implement.team.argument;

import me.matthew.teams.command.ExecutorArgument;
import me.matthew.teams.handler.enums.Role;
import me.matthew.teams.handler.team.Team;
import me.matthew.teams.handler.team.TeamHandler;
import me.matthew.teams.handler.enums.Config;
import me.matthew.teams.handler.enums.Messages;
import me.matthew.teams.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class InvitesArgument extends ExecutorArgument {

    public InvitesArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return Arrays.asList(Role.LEADER, Role.CAPTAIN, Role.MEMBER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player =  (Player) sender;
        Team team = TeamHandler.getByPlayer(player);
        if (team == null) {
            player.sendMessage(Messages.get(Messages.NOT_IN_TEAM_SELF));
            return true;
        }
        team.getInvitedPlayers().keySet().removeIf(uuid -> team.getInvitedPlayers().get(uuid) < System.currentTimeMillis());
        List<UUID> invites = team.getInvitedPlayers().keySet().stream().collect(Collectors.toList());
        if (invites.isEmpty()) {
            player.sendMessage(Messages.get(Messages.INVITES_COMMAND_NO_PENDING));
            return true;
        }
        List<String> inviteNames = invites.stream().map(uuid -> {
            OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
            return (target.isOnline() ? Config.getString(Config.ONLINE_PREFIX) : Config.getString(Config.OFFLINE_PREFIX)) + target.getName();
        }).collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < inviteNames.size(); i++) {
            stringBuilder.append(inviteNames.get(i) + (i + 1 < inviteNames.size() ? "&7, &r" : "&r"));
        }
        player.sendMessage(Messages.get(Messages.INVITES_COMMAND_INVITES).replace("%invites%", StringUtil.translate(stringBuilder.toString())));
        return true;
    }
}

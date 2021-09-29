package me.matthew.teams.command.implement.team.argument;

import me.matthew.teams.command.ExecutorArgument;
import me.matthew.teams.handler.enums.Role;
import me.matthew.teams.handler.team.Team;
import me.matthew.teams.handler.team.TeamHandler;
import me.matthew.teams.handler.enums.Config;
import me.matthew.teams.handler.enums.Messages;
import me.matthew.teams.util.NumberUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListArgument extends ExecutorArgument {

    public ListArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length != 2) {
            printDetails(player, 1, Config.getInt(Config.TEAMS_PER_PAGE_LIST), label);
            return true;
        }
        if (!NumberUtil.isInt(args[1])) {
            player.sendMessage(Messages.get(Messages.LIST_COMMAND_PAGE_NOT_FOUND));
            return true;
        }
        int page = Integer.parseInt(args[1]);
        if (page < 1) {
            player.sendMessage(Messages.get(Messages.LIST_COMMAND_PAGE_NOT_FOUND));
            return true;
        }
        printDetails(player, page, Config.getInt(Config.TEAMS_PER_PAGE_LIST), label);
        return true;
    }

    public void printDetails(Player player, int currentPage, int teamsPerPage, String label) {
        if (TeamHandler.getTeamMap().isEmpty()) {
            player.sendMessage(Messages.get(Messages.LIST_COMMAND_NO_TEAMS));
            return;
        }
        List<Team> sorted = TeamHandler.getTeamMap().values().stream().sorted(Comparator.comparing(team -> ((Team) team).getOnlineMembers(player).size()).reversed()).collect(Collectors.toList());
        int maxPage = (int) Math.ceil((double) sorted.size() / (double) teamsPerPage);
        if (currentPage > maxPage) {
            player.sendMessage(Messages.get(Messages.LIST_COMMAND_PAGE_NOT_FOUND));
            return;
        }
        int minTeam = (currentPage - 1) * teamsPerPage, maxTeam = (currentPage - 1) * teamsPerPage + teamsPerPage;
        List<String> messages = Messages.getList(Messages.LIST_COMMAND_LISTED);
        for (String message : messages) {
            if (message.equals("%teams%")) {
                for (int i = minTeam; i < (maxTeam > sorted.size() ? sorted.size() : maxTeam); i++) {
                    Team team = sorted.get(i);
                    player.sendMessage(Messages.get(Messages.LIST_COMMAND_TEAM_FORMAT)
                            .replace("%position%", (i + 1) + "")
                            .replace("%teamName%", team.getName())
                            .replace("%onlineSize%", team.getOnlineMembers().size() + "")
                            .replace("%maxSize%", team.getMembers().size() + ""));
                }
            } else {
                player.sendMessage(message
                        .replace("%currentPage%", currentPage + "")
                        .replace("%maxPage%", maxPage + "")
                        .replace("%label%", label));
            }
        }
    }
}

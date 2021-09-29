package me.matthew.teams.command;

import me.matthew.teams.Teams;
import me.matthew.teams.command.sub.*;
import me.matthew.teams.handler.Team;
import me.matthew.teams.handler.TeamHandler;
import me.matthew.teams.util.BukkitUtil;
import me.matthew.teams.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamExecutor extends ArgumentExecutor {

    private final ExecutorArgument helpArgument;

    public TeamExecutor(String label) {
        super(label, "mclans.use");
        addArgument(new CaptainsArgument("captains"));
        addArgument(new CreateArgument("create"));
        addArgument(new DisbandArgument("disband"));
        addArgument(helpArgument = new HelpArgument("help"));
        addArgument(new InfoArgument("info"));
        addArgument(new InviteArgument("invite"));
        addArgument(new InvitesArgument("invites"));
        addArgument(new JoinArgument("join"));
        addArgument(new KickArgument("kick"));
        addArgument(new LeaderArgument("leader"));
        addArgument(new LeaveArgument("leave"));
        addArgument(new ListArgument("list"));
        addArgument(new TopArgument("top"));
        addArgument(new UninviteArgument("uninvite"));
        if (Teams.getInstance().getEconomy() == null) {
            Bukkit.getLogger().warning("[" + Teams.getInstance().getName() + "] " + Messages.get(Messages.VAULT_NOT_FOUND));
            return;
        }
        addArgument(new DepositArgument("deposit"));
        addArgument(new WithdrawArgument("withdraw"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Can only be used as player.");
            return true;
        }
        if (!sender.hasPermission(getPermission())) {
            sender.sendMessage(Messages.get(Messages.NO_PERMISSION));
            return true;
        }
        if (args.length < 1) {
            helpArgument.onCommand(sender, command, label, args);
            return true;
        }
        ExecutorArgument argument = getArgument(args[0]);
        if (argument != null) {
            if (argument.getPermission() == null || sender.hasPermission(argument.getPermission())) {
                argument.onCommand(sender, command, label, args);
                return true;
            }
        }
        helpArgument.onCommand(sender, command, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return Arrays.asList("Can", "only", "be", "used", "as", "player");
        }
        // null = aki nincs teambe
        // empty = mindenki
        List<String> results = new ArrayList<>();
        if (args.length < 2) {
            for (ExecutorArgument argument : getArguments()) {
                if (argument.getPermission() == null || sender.hasPermission(argument.getPermission())) {
                    Player player = (Player) sender;
                    Team team = TeamHandler.getByPlayer(player);
                    if (team == null && (argument.getRoles() == null || argument.getRoles().isEmpty())) {
                        addResult(argument, results);
                    } else if (team != null && (argument.getRoles() != null && (argument.getRoles().contains(team.getRole(player)) || argument.getRoles().isEmpty()))) {
                        addResult(argument, results);
                    }
                }
            }
        } else {
            ExecutorArgument argument = getArgument(args[0]);
            if (argument == null) {
                return results;
            }
            if (argument.getPermission() == null || sender.hasPermission(argument.getPermission())) {
                results = argument.onTabComplete(sender, command, label, args);
                if (results == null) {
                    return null;
                }
            }
        }
        return BukkitUtil.getCompletions(args, results);
    }

    private void addResult(ExecutorArgument argument, List<String> stringList) {
        stringList.add(argument.getName());
//        if (argument.getAliases() != null) {
//            for (String alias : argument.getAliases()) {
//                stringList.add(alias);
//            }
//        }
    }
}

package me.matthew.teams.command.sub;

import me.matthew.teams.Teams;
import me.matthew.teams.command.ExecutorArgument;
import me.matthew.teams.handler.Role;
import me.matthew.teams.handler.Team;
import me.matthew.teams.handler.TeamHandler;
import me.matthew.teams.util.Messages;
import me.matthew.teams.util.NumberUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class WithdrawArgument extends ExecutorArgument {

    public WithdrawArgument(String name) {
        super(name, "mclans.use", new String[]{"w"});
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
            player.sendMessage(Messages.get(Messages.WITHDRAW_COMMAND_USAGE).replace("%label%", label));
            return true;
        }
        if (Teams.getInstance().getEconomy() == null) {
            player.sendMessage(Messages.get(Messages.VAULT_NOT_FOUND));
            return true;
        }
        if (NumberUtil.isDouble(args[1])) {
            double amount = Double.parseDouble(args[1]);
            if (amount <= 0) {
                player.sendMessage(Messages.get(Messages.WITHDRAW_COMMAND_CAN_NOT_DEPOSIT_ZERO));
                return true;
            }
            Economy economy = Teams.getInstance().getEconomy();
            if (amount > team.getBalance()) {
                player.sendMessage(Messages.get(Messages.WITHDRAW_COMMAND_NOT_ENOUGH_MONEY));
                return true;
            }
            team.withdraw(amount);
            economy.withdrawPlayer(player, amount);
            TeamHandler.save(team);
            team.sendMessage(Messages.get(Messages.WITHDRAW_COMMAND_WITHDRAWN).replace("%playerName%", player.getName()).replace("%amount%", amount + ""));
            return true;
        }
        player.sendMessage(Messages.get(Messages.INVALID_ARGUMENTS));
        return true;
    }
}

package dev.matthew.clans.clans.command.implement.clan.argument;

import dev.matthew.clans.clans.Clans;
import dev.matthew.clans.clans.command.ExecutorArgument;
import dev.matthew.clans.clans.handler.clan.Clan;
import dev.matthew.clans.clans.handler.clan.ClanHandler;
import dev.matthew.clans.clans.handler.enums.Role;
import dev.matthew.clans.clans.handler.file.Message;
import dev.matthew.clans.clans.util.NumberUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class DepositArgument extends ExecutorArgument {

    public DepositArgument(String name) {
        super(name, "mclans.use", new String[]{"d"});
    }

    @Override
    public List<Role> getRoles() {
        return Arrays.asList(Role.LEADER, Role.CAPTAIN, Role.MEMBER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Message.send(sender, Message.COMMAND_ONLY_FOR_PLAYERS);
            return  true;
        }
        Player player = (Player) sender;
        Clan clan = ClanHandler.getByPlayer(player);
        if (clan == null) {
            Message.send(player, Message.NOT_IN_CLAN_SELF);
            return true;
        }
        if (Clans.getInstance().getEconomy() == null) {
            Message.send(player, Message.VAULT_NOT_FOUND);
            return true;
        }
        Economy economy = Clans.getInstance().getEconomy();
        if (args.length != 2) {
            Message.send(player, Message.DEPOSIT_COMMAND.USAGE.replaceAll("%label%", label));
            return true;
        }
        if (args[1].equalsIgnoreCase("all")) {
            double amount = economy.getBalance(player);
            if (amount <= 0) {
                Message.send(player, Message.DEPOSIT_COMMAND.CAN_NOT_DEPOSIT_ZERO);
                return true;
            }
            clan.deposit(amount);
            economy.withdrawPlayer(player, amount);
            ClanHandler.save(clan);
            clan.sendColoredMessage(Message.DEPOSIT_COMMAND.DEPOSITED.replaceAll("%playerName%", player.getName()).replaceAll("%amount%", amount + ""));
            return true;
        } else if (NumberUtil.isDouble(args[1])) {
            double amount = Double.parseDouble(args[1]);
            if (amount <= 0) {
                Message.send(player, Message.DEPOSIT_COMMAND.CAN_NOT_DEPOSIT_ZERO);
                return true;
            }
            if (amount > economy.getBalance(player)) {
                Message.send(player, Message.DEPOSIT_COMMAND.NOT_ENOUGH_MONEY);
                return true;
            }
            clan.deposit(amount);
            economy.withdrawPlayer(player, amount);
            ClanHandler.save(clan);
            clan.sendColoredMessage(Message.DEPOSIT_COMMAND.DEPOSITED.replaceAll("%playerName%", player.getName()).replaceAll("%amount%", amount + ""));
            return true;
        }
        Message.send(player, Message.INVALID_ARGUMENTS);
        return true;
    }
}

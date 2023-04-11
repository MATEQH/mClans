package dev.matthew.clans.command.implement.clan.argument.captain;

import dev.matthew.clans.enums.Role;
import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.file.Message;
import dev.matthew.clans.util.BukkitUtil;
import dev.matthew.clans.util.NumberUtil;
import dev.matthew.clans.util.hook.VaultHook;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
        if (args.length != 2) {
            Message.send(player, Message.WITHDRAW_COMMAND.USAGE.replaceAll("%label%", label));
            return true;
        }
//        if (VaultHook.ECONOMY == null) {
//            Message.send(player, Message.VAULT_NOT_FOUND);
//            return true;
//        }
        Economy economy = VaultHook.ECONOMY;
        if (args[1].equalsIgnoreCase("all")) {
            double amount = clan.getBalance();
            if (amount <= 0) {
                Message.send(player, Message.WITHDRAW_COMMAND.BALANCE_ZERO);
                return true;
            }
            clan.withdraw(amount);
            economy.depositPlayer(player, amount);
            ClanHandler.save(clan);
            clan.sendColoredMessage(Message.WITHDRAW_COMMAND.WITHDRAWN.replaceAll("%playerName%", player.getName()).replaceAll("%amount%", amount + ""));
            return true;
        } else if (NumberUtil.isDouble(args[1])) {
            double amount = Double.parseDouble(args[1]);
            if (amount <= 0) {
                Message.send(player, Message.WITHDRAW_COMMAND.CAN_NOT_WITHDRAW_ZERO);
                return true;
            }
            if (amount > clan.getBalance()) {
                Message.send(player, Message.WITHDRAW_COMMAND.NOT_ENOUGH_MONEY);
                return true;
            }
            clan.withdraw(amount);
            economy.withdrawPlayer(player, amount);
            ClanHandler.save(clan);
            clan.sendColoredMessage(Message.WITHDRAW_COMMAND.WITHDRAWN.replaceAll("%playerName%", player.getName()).replace("%amount%", amount + ""));
            return true;
        }
        Message.send(player, Message.INVALID_ARGUMENTS);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Clan clan = ClanHandler.getByPlayer(player);
        if (args.length == 2 && clan != null && (clan.getRole(player) == Role.LEADER || clan.getRole(player) == Role.CAPTAIN)) {
            return BukkitUtil.getCompletions(args, Arrays.asList("all", "100", "1000", "10000", "100000", "1000000"));
        }
        return new ArrayList<>();
    }
}

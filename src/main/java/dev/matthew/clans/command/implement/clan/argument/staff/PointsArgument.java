package dev.matthew.clans.command.implement.clan.argument.staff;

import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.enums.Role;
import dev.matthew.clans.file.Message;
import dev.matthew.clans.util.BukkitUtil;
import dev.matthew.clans.util.NumberUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PointsArgument extends ExecutorArgument {

    public PointsArgument(String name) {
        super(name, "mclans.staff");
    }

    @Override
    public List<Role> getRoles() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            Message.send(sender, Message.POINTS_COMMAND.USAGE.replaceAll("%label%", label));
            return true;
        }
        switch (args[1]) {
            case "add": {
                if (args.length != 4) {
                    Message.send(sender, Message.POINTS_COMMAND.ADD.USAGE.replaceAll("%label%", label));
                    return true;
                }
                Clan clan = ClanHandler.getByName(args[2]);
                if (clan == null) {
                    Message.send(sender, Message.CLAN_DOES_NOT_EXISTS);
                    return true;
                }
                if (!NumberUtil.isInt(args[3])) {
                    Message.send(sender, Message.POINTS_COMMAND.INVALID_NUMBER);
                    return true;
                }
                int amount = Integer.parseInt(args[3]);
                if (amount < 1) {
                    Message.send(sender, Message.POINTS_COMMAND.TOO_LESS);
                    return true;
                }
                clan.setPoints(clan.getPoints() + amount);
                ClanHandler.save(clan);
                Message.send(sender, Message.POINTS_COMMAND.ADD.ADDED
                        .replaceAll("%name%", clan.getName())
                        .replaceAll("%amount%", String.valueOf(amount))
                        .replaceAll("%balance%", String.valueOf(clan.getBalance())));
                break;
            }
            case "set": {
                if (args.length != 4) {
                    Message.send(sender, Message.POINTS_COMMAND.SET.USAGE.replaceAll("%label%", label));
                    return true;
                }
                Clan clan = ClanHandler.getByName(args[2]);
                if (clan == null) {
                    Message.send(sender, Message.CLAN_DOES_NOT_EXISTS);
                    return true;
                }
                if (!NumberUtil.isInt(args[3])) {
                    Message.send(sender, Message.POINTS_COMMAND.INVALID_NUMBER);
                    return true;
                }
                int amount = Integer.parseInt(args[3]);
                if (amount < 0) {
                    Message.send(sender, Message.POINTS_COMMAND.TOO_LESS);
                    return true;
                }
                clan.setPoints(amount);
                ClanHandler.save(clan);
                Message.send(sender, Message.POINTS_COMMAND.SET.SETTED
                        .replaceAll("%name%", clan.getName())
                        .replaceAll("%amount%", String.valueOf(amount))
                        .replaceAll("%balance%", String.valueOf(clan.getBalance())));
                break;
            }
            case "remove": {
                if (args.length != 4) {
                    Message.send(sender, Message.POINTS_COMMAND.REMOVE.USAGE.replaceAll("%label%", label));
                    return true;
                }
                Clan clan = ClanHandler.getByName(args[2]);
                if (clan == null) {
                    Message.send(sender, Message.CLAN_DOES_NOT_EXISTS);
                    return true;
                }
                if (!NumberUtil.isInt(args[3])) {
                    Message.send(sender, Message.POINTS_COMMAND.INVALID_NUMBER);
                    return true;
                }
                int amount = Integer.parseInt(args[3]);
                if (amount > clan.getPoints()) {
                    Message.send(sender, Message.POINTS_COMMAND.TOO_MUCH);
                    return true;
                }
                clan.setPoints(clan.getPoints() - amount);
                ClanHandler.save(clan);
                Message.send(sender, Message.POINTS_COMMAND.REMOVE.REMOVED
                        .replaceAll("%name%", clan.getName())
                        .replaceAll("%amount%", String.valueOf(amount))
                        .replaceAll("%balance%", String.valueOf(clan.getBalance())));
                break;
            }
            default:
                Message.send(sender, Message.POINTS_COMMAND.USAGE.replaceAll("%label%", label));
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return args.length == 2 ? BukkitUtil.getCompletions(args, Arrays.asList("add", "set", "remove")) : new ArrayList<>();
    }
}

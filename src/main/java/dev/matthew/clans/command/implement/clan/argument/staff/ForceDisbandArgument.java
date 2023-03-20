package dev.matthew.clans.command.implement.clan.argument.staff;

import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.enums.Role;
import dev.matthew.clans.file.Config;
import dev.matthew.clans.file.Message;
import dev.matthew.clans.util.BukkitUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ForceDisbandArgument extends ExecutorArgument {

    public ForceDisbandArgument(String name) {
        super(name, "mclans.staff");
    }

    @Override
    public List<Role> getRoles() {
        return Collections.singletonList(Role.LEADER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            Message.send(sender, Message.FORCE_DISBAND_COMMAND.USAGE.replaceAll("%label%", label));
            return true;
        }
        Clan clan = ClanHandler.getByName(args[1]);
        if (clan == null) {
            Message.send(sender, Message.CLAN_DOES_NOT_EXISTS);
            return true;
        }
        ClanHandler.removeClan(clan);
        if (Config.BROADCAST_DISBAND) {
            Message.sendGlobal(Message.DISBAND_COMMAND.DISBANDED
                    .replaceAll("%name%", clan.getName())
                    .replaceAll("%playerName%", sender.getName())
            );
        } else {
            Message.send(sender, Message.DISBAND_COMMAND.DISBANDED
                    .replaceAll("%name%", clan.getName())
                    .replaceAll("%playerName%", sender.getName())
            );
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return BukkitUtil.getCompletions(args, Bukkit.getOnlinePlayers().stream().filter(online -> ClanHandler.getByPlayer(online) != null).map(HumanEntity::getName).collect(Collectors.toList()));
    }
}

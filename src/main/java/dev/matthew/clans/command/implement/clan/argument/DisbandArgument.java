package dev.matthew.clans.command.implement.clan.argument;

import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.enums.Role;
import dev.matthew.clans.file.Config;
import dev.matthew.clans.file.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class DisbandArgument extends ExecutorArgument {

    public DisbandArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return Collections.singletonList(Role.LEADER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Message.send(sender, Message.COMMAND_ONLY_FOR_PLAYERS);
            return true;
        }
        Player player = (Player) sender;
        Clan clan = ClanHandler.getByPlayer(player);
        if (clan == null) {
            Message.send(player, Message.NOT_IN_CLAN_SELF);
            return true;
        }
        if (clan.getRole(player) != Role.LEADER) {
            Message.send(player, Message.MUST_BE_LEADER);
            return true;
        }
        ClanHandler.removeClan(clan);
        if (Config.BROADCAST_DISBAND) {
            Message.sendGlobal(Message.DISBAND_COMMAND.DISBANDED.replaceAll("%name%", clan.getName()).replace("%playerName%", player.getName()));
        } else {
            Message.send(player, Message.DISBAND_COMMAND.DISBANDED.replaceAll("%name%", clan.getName()).replace("%playerName%", player.getName()));
        }
        return true;
    }
}

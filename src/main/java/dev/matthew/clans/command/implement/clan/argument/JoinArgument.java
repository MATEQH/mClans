package dev.matthew.clans.command.implement.clan.argument;

import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.enums.Role;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.file.Config;
import dev.matthew.clans.file.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JoinArgument extends ExecutorArgument {

    public JoinArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return Collections.singletonList(Role.NONE);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Message.send(sender, Message.COMMAND_ONLY_FOR_PLAYERS);
            return  true;
        }
        Player player = (Player) sender;
        Clan clan = ClanHandler.getByPlayer(player);
        if (clan != null) {
            Message.send(player, Message.ALREADY_IN_CLAN_SELF);
            return true;
        }
        if (args.length != 2) {
            Message.send(player, Message.JOIN_COMMAND.USAGE.replaceAll("%label%", label));
            return true;
        }
        clan = ClanHandler.getByName(args[1]);
        if (clan == null) {
            Message.send(player, Message.CLAN_DOES_NOT_EXISTS);
            return true;
        }
        clan.getInvitedPlayers().entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis());
        if (player.hasPermission("mclans.staff")) {
            if (clan.invited(player)) clan.getInvitedPlayers().remove(player.getUniqueId());
            clan.getMembers().put(player.getUniqueId(), Role.MEMBER);
            ClanHandler.getPlayerMap().put(player.getUniqueId(), clan);
            ClanHandler.save(clan);
            clan.sendColoredMessage(Message.JOIN_COMMAND.JOINED.replaceAll("%playerName%", player.getName()));
            return true;
        }
        if (!clan.invited(player)) {
            Message.send(player, Message.JOIN_COMMAND.NOT_INVITED.replaceAll("%name%", clan.getName()));
            return true;
        }
        if (clan.getMembers().size() >= Config.CLAN.MAX_SIZE) {
            Message.send(player, Message.JOIN_COMMAND.CLAN_FULL.replaceAll("%name%", clan.getName()));
            return true;
        }
        clan.getMembers().put(player.getUniqueId(), Role.MEMBER);
        clan.getInvitedPlayers().remove(player.getUniqueId());
        ClanHandler.getPlayerMap().put(player.getUniqueId(), clan);
        ClanHandler.save(clan);
        clan.sendColoredMessage(Message.JOIN_COMMAND.JOINED.replaceAll("%playerName%", player.getName()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
}

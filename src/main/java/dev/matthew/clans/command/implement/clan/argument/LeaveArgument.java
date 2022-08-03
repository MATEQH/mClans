package dev.matthew.clans.command.implement.clan.argument;

import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.enums.Role;
import dev.matthew.clans.file.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class LeaveArgument extends ExecutorArgument {

    public LeaveArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return Arrays.asList(Role.CAPTAIN, Role.MEMBER);
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
        if (clan.getRole(player) == Role.LEADER) {
            Message.send(player, Message.LEAVE_COMMAND.LEADER_LEAVE);
            return true;
        }
        clan.getMembers().remove(player.getUniqueId());
        ClanHandler.getPlayerMap().put(player.getUniqueId(), null);
        ClanHandler.save(clan);
        Message.send(player, Message.LEAVE_COMMAND.LEFT_SELF.replaceAll("%name%", clan.getName()));
        clan.sendColoredMessage(Message.LEAVE_COMMAND.LEFT_OTHERS.replaceAll("%playerName%", player.getName()));
        return true;
    }
}

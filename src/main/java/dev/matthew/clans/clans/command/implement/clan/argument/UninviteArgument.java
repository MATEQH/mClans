package dev.matthew.clans.clans.command.implement.clan.argument;

import dev.matthew.clans.clans.command.ExecutorArgument;
import dev.matthew.clans.clans.handler.clan.Clan;
import dev.matthew.clans.clans.handler.clan.ClanHandler;
import dev.matthew.clans.clans.handler.enums.Role;
import dev.matthew.clans.clans.handler.file.Message;
import dev.matthew.clans.clans.util.BukkitUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class UninviteArgument extends ExecutorArgument {

    public UninviteArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return Arrays.asList(Role.LEADER, Role.CAPTAIN);
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
        if (args.length != 2) {
            Message.send(player, Message.UNINVITE_COMMAND.USAGE.replaceAll("%label%", label));
            return true;
        }
        if (clan.getRole(player) != Role.LEADER && clan.getRole(player) != Role.CAPTAIN) {
            Message.send(player, Message.CLAN_NO_PERMISSION.replaceAll("%role%", "captain or leader"));
            return true;
        }
        OfflinePlayer target = BukkitUtil.getPlayer(args[1]);
        if (target == null) {
            Message.send(player, Message.PLAYER_NOT_FOUND.replaceAll("%targetName%", args[1]));
            return true;
        }
        if (target.getUniqueId().equals(player.getUniqueId())) {
            Message.send(player, Message.CANNOT_DO_THIS_YOURSELF);
            return true;
        }
        if (!clan.invited(target.getUniqueId())) {
            Message.send(player, Message.UNINVITE_COMMAND.NOT_INVITED.replaceAll("%targetName%", target.getName()));
            return true;
        }
        clan.getInvitedPlayers().remove(target.getUniqueId());
        Message.send(player, Message.UNINVITE_COMMAND.UNINVITED.replaceAll("%targetName%", target.getName()));
        return true;
    }
}

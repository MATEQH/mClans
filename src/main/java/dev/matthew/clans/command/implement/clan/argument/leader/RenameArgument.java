package dev.matthew.clans.command.implement.clan.argument.leader;

import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.enums.Role;
import dev.matthew.clans.file.Config;
import dev.matthew.clans.file.Message;
import dev.matthew.clans.util.StringUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenameArgument extends ExecutorArgument {

    public RenameArgument(String name) {
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
        if (System.currentTimeMillis() - Config.RENAME_COOLDOWN < clan.getLastRename()) {
            Message.send(sender, Message.RENAME_COMMAND.ON_COOLDOWN
                    .replaceAll("%label%", label)
                    .replaceAll("%remaining%", StringUtil.formatMillisToChat(clan.getLastRename() - (System.currentTimeMillis() - Config.RENAME_COOLDOWN))));
            return true;
        }
        if (args.length != 2) {
            Message.send(sender, Message.RENAME_COMMAND.USAGE.replaceAll("%label%", label));
            return true;
        }
        String oldName = clan.getName();
        String newName = args[1];
        if (ClanHandler.getByName(newName) != null) {
            Message.send(player, Message.CLAN_ALREADY_EXISTS.replaceAll("%name%", ClanHandler.getByName(newName).getName()));
            return true;
        }
        ClanHandler.renameClan(clan, newName);
        if (Config.BROADCAST_RENAME) {
            Message.sendGlobal(Message.RENAME_COMMAND.RENAMED
                    .replaceAll("%oldName%", oldName)
                    .replaceAll("%name%", clan.getName())
                    .replaceAll("%playerName%", player.getName()));
        } else {
            Message.send(player, Message.RENAME_COMMAND.RENAMED
                    .replaceAll("%oldName%", oldName)
                    .replaceAll("%name%", clan.getName())
                    .replaceAll("%playerName%", player.getName()));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
}

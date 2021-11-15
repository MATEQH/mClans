package com.github.mateqh.clans.command.implement.clan.argument;

import com.github.mateqh.clans.command.ExecutorArgument;
import com.github.mateqh.clans.handler.clan.Clan;
import com.github.mateqh.clans.handler.clan.ClanHandler;
import com.github.mateqh.clans.handler.enums.Role;
import com.github.mateqh.clans.handler.file.Config;
import com.github.mateqh.clans.handler.file.Message;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CreateArgument extends ExecutorArgument {

    public CreateArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Message.send(sender, Message.COMMAND_ONLY_FOR_PLAYERS);
            return true;
        }
        Player player = (Player) sender;
        Clan clan = ClanHandler.getByPlayer((Player) sender);
        if (clan != null) {
            Message.send(player, Message.ALREADY_IN_CLAN_SELF);
            return true;
        }
        if (args.length < 2) {
            Message.send(player, Message.CREATE_COMMAND.USAGE.replaceAll("%label%", label));
            return true;
        }
        String name = args[1];
        int value = Config.CLAN.NAME_MIN_LENGTH;
        if (name.length() < value) {
            Message.send(player, Message.CREATE_COMMAND.NAME_TOO_SHORT.replaceAll("%name%", name));
            return true;
        }
        value = Config.CLAN.NAME_MAX_LENGTH;
        if (name.length() > value) {
            Message.send(player, Message.CREATE_COMMAND.NAME_TOO_LONG.replaceAll("%name%", name));
            return true;
        }
        if (!StringUtils.isAlphanumeric(name)) {
            Message.send(player, Message.CREATE_COMMAND.NAME_NOT_ALPHANUMERIC.replaceAll("%name%", name));
            return true;
        }
        if (ClanHandler.getByName(name) != null) {
            Message.send(player, Message.CLAN_ALREADY_EXISTS.replaceAll("%name%", name));
            return true;
        }
        clan = new Clan(name, player.getUniqueId());
        ClanHandler.createClan(clan);
        if (Config.BROADCAST_CREATE) {
            Message.sendGlobal(Message.CREATE_COMMAND.CREATED.replaceAll("%name%", args[1]).replaceAll("%playerName%", player.getName()));
        } else {
            Message.send(player, Message.CREATE_COMMAND.CREATED.replaceAll("%name%", args[1]).replaceAll("%playerName%", player.getName()));
        }
        return true;
    }
}

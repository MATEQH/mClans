package dev.matthew.clans.command.implement.clan.argument;

import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.enums.Role;
import dev.matthew.clans.file.Config;
import dev.matthew.clans.file.Message;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CreateArgument extends ExecutorArgument {

    public CreateArgument(String name) {
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
        if (Config.CLAN.BLOCKED_NAMES.contains(name.toLowerCase())) {
            Message.send(player, Message.BLOCKED_CLAN_NAME.replaceAll("%name%", name));
            return true;
        }
        if (ClanHandler.getByName(name) != null) {
            Message.send(player, Message.CLAN_ALREADY_EXISTS.replaceAll("%name%", name));
            return true;
        }
        ClanHandler.createClan(name, player.getUniqueId());
        if (Config.BROADCAST_CREATE) {
            Message.sendGlobal(Message.CREATE_COMMAND.CREATED.replaceAll("%name%", args[1]).replaceAll("%playerName%", player.getName()));
        } else {
            Message.send(player, Message.CREATE_COMMAND.CREATED.replaceAll("%name%", args[1]).replaceAll("%playerName%", player.getName()));
        }
        return true;
    }
}

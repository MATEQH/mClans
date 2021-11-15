package com.github.mateqh.clans.command.implement.clan.argument;

import com.github.mateqh.clans.command.ExecutorArgument;
import com.github.mateqh.clans.handler.clan.Clan;
import com.github.mateqh.clans.handler.clan.ClanHandler;
import com.github.mateqh.clans.handler.enums.Role;
import com.github.mateqh.clans.handler.file.Config;
import com.github.mateqh.clans.handler.file.Message;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class InvitesArgument extends ExecutorArgument {

    public InvitesArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return Arrays.asList(Role.LEADER, Role.CAPTAIN, Role.MEMBER);
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
        clan.getInvitedPlayers().keySet().removeIf(uuid -> clan.getInvitedPlayers().get(uuid) < System.currentTimeMillis());
        List<UUID> invites = new ArrayList<>(clan.getInvitedPlayers().keySet());
        if (invites.isEmpty()) {
            Message.send(player, Message.INVITES_COMMAND.NO_PENDING);
            return true;
        }
        List<String> inviteNames = invites.stream().map(uuid -> {
            OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
            return (target.isOnline() ? Config.ONLINE_PREFIX : Config.OFFLINE_PREFIX) + target.getName();
        }).collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < inviteNames.size(); i++) {
            stringBuilder.append(inviteNames.get(i)).append(i + 1 < inviteNames.size() ? "&7, &r" : "&r");
        }
        Message.send(player, Message.INVITES_COMMAND.INVITES.replaceAll("%invites%", stringBuilder.toString()));
        return true;
    }
}

package dev.matthew.clans.command.implement.clan.argument;

import com.google.common.collect.Maps;
import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.enums.Role;
import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.file.Config;
import dev.matthew.clans.file.Message;
import dev.matthew.clans.util.NumberUtil;
import dev.matthew.clans.util.StringUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListArgument extends ExecutorArgument {

    public ListArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            printDetails(sender, 1, Config.CLAN.LIST, label);
            return true;
        }
        if (!NumberUtil.isInt(args[1])) {
            Message.send(sender, Message.LIST_COMMAND.PAGE_NOT_FOUND);
            return true;
        }
        int page = Integer.parseInt(args[1]);
        if (page < 1) {
            Message.send(sender, Message.LIST_COMMAND.PAGE_NOT_FOUND);
            return true;
        }
        printDetails(sender, page, Config.CLAN.LIST, label);
        return true;
    }

    public void printDetails(CommandSender sender, int currentPage, int clansPerPage, String label) {
        if (ClanHandler.getClanMap().isEmpty()) {
            Message.send(sender, Message.LIST_COMMAND.NO_CLANS);
            return;
        }
        List<Clan> sorted = ClanHandler.getClanMap().values().stream().sorted(Comparator.comparing(clan ->
                sender instanceof Player ?
                        ((Clan) clan).getOnlineMembersAsList((Player) sender).size() :
                        ((Clan) clan).getOnlineMembersAsList().size()
        ).reversed()).collect(Collectors.toList());
        int maxPage = (int) Math.ceil((double) sorted.size() / (double) clansPerPage);
        if (currentPage > maxPage) {
            Message.send(sender, Message.LIST_COMMAND.PAGE_NOT_FOUND);
            return;
        }
        int minClan = (currentPage - 1) * clansPerPage, maxClan = (currentPage - 1) * clansPerPage + clansPerPage;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Clan playerClan = ClanHandler.getByPlayer(player);
            for (String message : Message.LIST_COMMAND.LISTED) {
                if (message.equals("%clans%")) {
                    for (int i = minClan; i < (Math.min(maxClan, sorted.size())); i++) {
                        Clan clan = sorted.get(i);
                        MiniMessage mm = MiniMessage.miniMessage();
                        Component parser = mm.deserialize(Message.LIST_COMMAND.CLAN_FORMAT);
                        Map<String, String> placeholders = Maps.newConcurrentMap();
                        placeholders.put("%prefix%", playerClan != null && playerClan == clan ?
                                Message.LIST_COMMAND.PREFIX.OWN : Message.LIST_COMMAND.PREFIX.OTHER);
                        placeholders.put("%points%", String.valueOf(clan.getPoints()));
                        placeholders.put("%kills%", String.valueOf(clan.getKills()));
                        placeholders.put("%position%", String.valueOf(i + 1));
                        placeholders.put("%name%", clan.getName());
                        placeholders.put("%onlineSize%", String.valueOf(clan.getOnlineMembersAsList((Player) sender).size()));
                        placeholders.put("%size%", String.valueOf(clan.getMembers().size()));
                        parser.replaceText()

//                        TextComponent info = new TextComponent(StringUtil.translate(
//                                (playerClan != null && playerClan == clan ?
//                                        Message.LIST_COMMAND.PREFIX.OWN : Message.LIST_COMMAND.PREFIX.OTHER) +
//                                        clan.getName()
//                        ));
//                        info.setHoverEvent(new HoverEvent(
//                                HoverEvent.Action.SHOW_TEXT,
//                                TextComponent.fromLegacyText(StringUtil.translate(
//                                        Message.LIST_COMMAND.HOVER_MESSAGE
//                                                .replaceAll("%points%", String.valueOf(clan.getPoints()))
//                                                .replaceAll("%kills%", String.valueOf(clan.getKills()))
//                                                .replaceAll("%position%", String.valueOf(i + 1))
//                                                .replaceAll("%name%", (playerClan != null && playerClan == clan ?
//                                                        Message.LIST_COMMAND.PREFIX.OWN : Message.LIST_COMMAND.PREFIX.OTHER) + clan.getName())
//                                                .replaceAll("%onlineSize%", String.valueOf(clan.getOnlineMembersAsList((Player) sender).size()))
//                                                .replaceAll("%size%", String.valueOf(clan.getMembers().size()))
//                                ))
//                        ));
//                        info.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " info " + clan.getId()));
//
//                        TextComponent text = new TextComponent();
//                        String[] split = Message.LIST_COMMAND.CLAN_FORMAT.split(" ");
//                        for (int j = 0; j < split.length; j++) {
//                            String arg = split[j];
//                            if (arg.contains("%clickableName%")) {
//                                text.addExtra(info);
//                            } else {
//                                text.addExtra(StringUtil.translate(arg
//                                        .replaceAll("%points%", String.valueOf(clan.getPoints()))
//                                        .replaceAll("%kills%", String.valueOf(clan.getKills()))
//                                        .replaceAll("%position%", String.valueOf(i + 1))
//                                        .replaceAll("%name%", (playerClan != null && playerClan == clan ?
//                                                Message.LIST_COMMAND.PREFIX.OWN : Message.LIST_COMMAND.PREFIX.OTHER) + clan.getName())
//                                        .replaceAll("%onlineSize%", String.valueOf(clan.getOnlineMembersAsList((Player) sender).size()))
//                                        .replaceAll("%size%", String.valueOf(clan.getMembers().size())))
//                                );
//                            }
//                            if (j + 1 < split.length) text.addExtra(" ");
//                        }
//                        player.spigot().sendMessage(text);
                    }
                } else {
                    Message.send(sender, message
                            .replaceAll("%currentPage%", String.valueOf(currentPage))
                            .replaceAll("%maxPage%", String.valueOf(maxPage))
                            .replaceAll("%label%", label)
                    );
                }
            }
            return;
        }
        for (String message : Message.LIST_COMMAND.LISTED) {
            if (message.equals("%clans%")) {
                for (int i = minClan; i < (Math.min(maxClan, sorted.size())); i++) {
                    Clan clan = sorted.get(i);
                    Message.send(sender, Message.LIST_COMMAND.CLAN_FORMAT
                            .replaceAll("%points%", String.valueOf(clan.getPoints()))
                            .replaceAll("%kills%", String.valueOf(clan.getKills()))
                            .replaceAll("%position%", String.valueOf(i + 1))
                            .replaceAll("%name%", clan.getName())
                            .replaceAll("%clickableName%", clan.getName())
                            .replaceAll("%onlineSize%", String.valueOf(clan.getOnlineMembersAsList().size()))
                            .replaceAll("%size%", String.valueOf(clan.getMembers().size()))
                    );
                }
            } else {
                Message.send(sender, message
                        .replaceAll("%currentPage%", String.valueOf(currentPage))
                        .replaceAll("%maxPage%", String.valueOf(maxPage))
                        .replaceAll("%label%", label)
                );
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
}

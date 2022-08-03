package dev.matthew.clans.command.implement.clan.argument;

import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.enums.Role;
import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.file.Config;
import dev.matthew.clans.file.Message;
import dev.matthew.clans.util.NumberUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListArgument extends ExecutorArgument {

    public ListArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return Collections.emptyList();
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
        for (String message : Message.LIST_COMMAND.LISTED) {
            if (message.equals("%clans%")) {
                for (int i = minClan; i < (Math.min(maxClan, sorted.size())); i++) {
                    Clan clan = sorted.get(i);
                    Message.send(sender, Message.LIST_COMMAND.CLAN_FORMAT
                            .replaceAll("%position%", String.valueOf(i + 1))
                            .replaceAll("%name%", clan.getName())
                            .replaceAll("%onlineSize%", String.valueOf(sender instanceof Player ?
                                    clan.getOnlineMembersAsList((Player) sender).size() :
                                    clan.getOnlineMembersAsList().size()
                            ))
                            .replaceAll("%size%",  String.valueOf(clan.getMembers().size()))
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
}

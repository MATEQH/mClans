package dev.matthew.clans.command.implement.clan.argument;

import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.enums.Role;
import dev.matthew.clans.file.Config;
import dev.matthew.clans.file.Message;
import dev.matthew.clans.util.NumberUtil;
import me.minidigger.minimessage.bungee.MiniMessageParser;
import me.minidigger.minimessage.bungee.MiniMessageSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TopArgument extends ExecutorArgument {

    public TopArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            printDetails(sender, 1, Config.CLAN.TOP, label);
            return true;
        }
        if (!NumberUtil.isInt(args[1])) {
            Message.send(sender, Message.TOP_COMMAND.PAGE_NOT_FOUND);
            return true;
        }
        int page = Integer.parseInt(args[1]);
        if (page < 1) {
            Message.send(sender, Message.TOP_COMMAND.PAGE_NOT_FOUND);
            return true;
        }
        printDetails(sender, page, Config.CLAN.TOP, label);
        return true;
    }

    public void printDetails(CommandSender sender, int currentPage, int clansPerPage, String label) {
        if (ClanHandler.getClanMap().isEmpty()) {
            Message.send(sender, Message.TOP_COMMAND.NO_CLANS);
            return;
        }
        List<Clan> sorted = ClanHandler.getSortedClans();
        int maxPage = (int) Math.ceil((double) sorted.size() / (double) clansPerPage);
        if (currentPage > maxPage) {
            Message.send(sender, Message.TOP_COMMAND.PAGE_NOT_FOUND);
            return;
        }
        int minClan = (currentPage - 1) * clansPerPage, maxClan = (currentPage - 1) * clansPerPage + clansPerPage;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Clan playerClan = ClanHandler.getByPlayer(player);
            for (String message : Message.TOP_COMMAND.LISTED) {
                if (message.equals("%clans%")) {
                    for (int i = minClan; i < (Math.min(maxClan, sorted.size())); i++) {
                        Clan clan = sorted.get(i);
                        BaseComponent[] comp = MiniMessageParser.parseFormat(Message.TOP_COMMAND.CLAN_FORMAT
                                .replaceAll("%prefix%", playerClan != null && playerClan == clan ?
                                        Message.TOP_COMMAND.PREFIX.OWN : clan.getOnlineMembersAsList(player).size() < 1 ? Message.TOP_COMMAND.PREFIX.ENEMY :
                                        Message.TOP_COMMAND.PREFIX.ENEMY_ONLINE)
                                .replaceAll("%id%", clan.getId().toString())
                                .replaceAll("%name%", clan.getName())
                                .replaceAll("%points%", String.valueOf(clan.getPoints()))
                                .replaceAll("%kills%", String.valueOf(clan.getKills()))
                                .replaceAll("%position%", String.valueOf(i + 1))
                                .replaceAll("%onlineSize%", String.valueOf(clan.getOnlineMembersAsList(player).size()))
                                .replaceAll("%size%", String.valueOf(clan.getMembers().size())));
                        player.spigot().sendMessage(comp);
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
        for (String message : Message.TOP_COMMAND.LISTED) {
            if (message.equals("%clans%")) {
                for (int i = minClan; i < (Math.min(maxClan, sorted.size())); i++) {
                    Clan clan = sorted.get(i);
                    BaseComponent[] comp = MiniMessageParser.parseFormat(Message.TOP_COMMAND.CLAN_FORMAT
                            .replaceAll("%prefix%", clan.getOnlineMembersAsList().size() < 1 ? Message.TOP_COMMAND.PREFIX.ENEMY :
                                    Message.TOP_COMMAND.PREFIX.ENEMY_ONLINE)
                            .replaceAll("%id%", clan.getId().toString())
                            .replaceAll("%name%", clan.getName())
                            .replaceAll("%points%", String.valueOf(clan.getPoints()))
                            .replaceAll("%kills%", String.valueOf(clan.getKills()))
                            .replaceAll("%position%", String.valueOf(i + 1))
                            .replaceAll("%onlineSize%", String.valueOf(clan.getOnlineMembersAsList().size()))
                            .replaceAll("%size%", String.valueOf(clan.getMembers().size())));
                    sender.sendMessage(MiniMessageParser.stripTokens(MiniMessageSerializer.serialize(comp)));
//                    sender.sendMessage(Message.TOP_COMMAND.CLAN_FORMAT
//                            .replaceAll("%prefix%", clan.getOnlineMembersAsList().size() < 1 ? Message.TOP_COMMAND.PREFIX.ENEMY :
//                                    Message.TOP_COMMAND.PREFIX.ENEMY_ONLINE)
//                            .replaceAll("%id%", clan.getId().toString())
//                            .replaceAll("%name%", clan.getName())
//                            .replaceAll("%points%", String.valueOf(clan.getPoints()))
//                            .replaceAll("%kills%", String.valueOf(clan.getKills()))
//                            .replaceAll("%position%", String.valueOf(i + 1))
//                            .replaceAll("%onlineSize%", String.valueOf(clan.getOnlineMembersAsList().size()))
//                            .replaceAll("%size%", String.valueOf(clan.getMembers().size())));
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

package com.github.mateqh.clans.handler.clan;

import com.github.mateqh.clans.Clans;
import com.github.mateqh.clans.util.NumberUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;

public class ClanExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return Clans.getInstance().getDescription().getName().toLowerCase();
    }

    @Override
    public String getAuthor() {
        return Clans.getInstance().getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return Clans.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null) {
            return "";
        }
        params = params.toLowerCase();
        if (params.startsWith("player_clan_")) {
            Clan clan = ClanHandler.getByPlayer(player);
            switch (params) {
                case "player_clan_name":
                    return returnName(clan);
                case "player_clan_balance":
                    return returnBalance(clan);
                case "player_clan_kills":
                    return returnKills(clan);
                case "player_clan_points":
                    return returnPoints(clan);
                case "player_clan_position":
                    return returnPosition(clan);
                case "player_clan_size":
                    return returnSize(clan);
                case "player_clan_online_size":
                    return returnOnlineSize(player, clan);
                default:
                    return "&cInvalid params";
            }
        } else if (params.startsWith("clan_top_")) {
            String[] args = params.split(":");
            if (args.length != 2) {
                return "&cInvalid params";
            }
            if (!NumberUtil.isInt(args[1])) {
                return "&cInvalid params";
            }
            List<Clan> sorted = ClanHandler.getSortedClans();
            Clan clan = null;
            int position = Integer.parseInt(args[1]) - 1;
            if (sorted != null && !sorted.isEmpty() && position > -1 && position < sorted.size()) {
                clan = sorted.get(position);
            }
            String param = args[0];
            switch (param) {
                case "clan_top_name":
                    return returnName(clan);
                case "clan_top_balance":
                    return returnBalance(clan);
                case "clan_top_kills":
                    return returnKills(clan);
                case "clan_top_points":
                    return returnPoints(clan);
                case "clan_top_position":
                    return String.valueOf(position + 1);
                case "clan_top_size":
                    return returnSize(clan);
                case "clan_top_online_size":
                    return returnOnlineSize(player, clan);
                default:
                    return "&cInvalid params";
            }
        } else if (params.startsWith("clan_")) {
            String[] args = params.split(":");
            if (args.length != 2) {
                return "&cInvalid params";
            }
            Clan clan = ClanHandler.getByName(args[1]);
            String param = args[0];
            switch (param) {
                case "clan_name":
                    return returnName(clan);
                case "clan_balance":
                    return returnBalance(clan);
                case "clan_kills":
                    return returnKills(clan);
                case "clan_points":
                    return returnPoints(clan);
                case "clan_position":
                    return returnPosition(clan);
                case "clan_size":
                    return returnSize(clan);
                case "clan_online_size":
                    return returnOnlineSize(player, clan);
                default:
                    return "&cInvalid params";
            }
        }
        return "&cInvalid params";
    }

    public String returnName(Clan clan) {
        return clan == null ? "N/a" : clan.getName();
    }

    public String returnBalance(Clan clan) {
        return clan == null ? "0" : new DecimalFormat("#.##").format(clan.getBalance());
    }

    public String returnKills(Clan clan) {
        return clan == null ? "0" : String.valueOf(clan.getKills());
    }

    public String returnPoints(Clan clan) {
        return clan == null ? "0" : String.valueOf(clan.getPoints());
    }

    public String returnPosition(Clan clan) {
        return clan == null ? "0" : String.valueOf(ClanHandler.getSortedClans().indexOf(clan) + 1);
    }

    public String returnSize(Clan clan) {
        return clan == null ? "0" : String.valueOf(clan.getMembers().size());
    }

    public String returnOnlineSize(Player player, Clan clan) {
        return clan == null ? "0" : String.valueOf(clan.getOnlineMembersAsList(player).size());
    }
}

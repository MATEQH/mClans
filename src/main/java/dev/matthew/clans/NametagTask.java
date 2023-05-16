package dev.matthew.clans;

import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.file.Config;
import dev.matthew.clans.util.StringUtil;
import com.lunarclient.bukkitapi.LunarClientAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class NametagTask implements Runnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(online -> Bukkit.getOnlinePlayers()
                .forEach(player -> LunarClientAPI.getInstance().overrideNametag(online, getTags(online, player), player)));
    }

    private List<String> getTags(Player player, Player target) {
        List<String> tag = Config.LUNAR.NO_CLAN_TAG.stream().map(string -> StringUtil.translate(string
                .replaceAll("%playerName%", player.getName())
                .replaceAll("%displayName%", player.getDisplayName())
        )).collect(Collectors.toList());
        Clan clan = ClanHandler.getByPlayer(player);
        if (clan != null) {
            Clan targetClan = ClanHandler.getByPlayer(target);
            tag = Config.LUNAR.CLAN_TAG.stream().map(string -> StringUtil.translate(string
                    .replaceAll("%relation%", clan != targetClan ? Config.CLAN.RELATION.ENEMY : Config.CLAN.RELATION.TEAMMATE)
                    .replaceAll("%playerName%", player.getName())
                    .replaceAll("%displayName%", player.getDisplayName())
                    .replaceAll("%name%", clan.getName())
            )).collect(Collectors.toList());
        }

        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") ?
                me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, tag) : tag;
    }
}

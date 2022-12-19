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
                .forEach(player -> LunarClientAPI.getInstance().overrideNametag(online, getTags(online), player)));
    }

    private List<String> getTags(Player player) {
        Clan clan = ClanHandler.getByPlayer(player);

        List<String> hasClan = Config.LUNAR.CLAN_TAG.stream().map(string -> StringUtil.translate(string
                .replaceAll("%name%", player.getName())
                .replaceAll("%displayName%", player.getDisplayName())
                .replaceAll("%clanName%", clan.getName())
        )).collect(Collectors.toList());
        List<String> noClan = Config.LUNAR.NO_CLAN_TAG.stream().map(string -> StringUtil.translate(string
                .replaceAll("%name%", player.getName())
                .replaceAll("%displayName%", player.getDisplayName())
        )).collect(Collectors.toList());

        return StringUtil.translate(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI") ?
                me.clip.placeholderapi.PlaceholderAPI.setPlaceholders(player, clan != null ? hasClan : noClan) :
                clan != null ? hasClan : noClan
        );
    }
}

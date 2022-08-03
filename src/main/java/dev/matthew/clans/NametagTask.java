package dev.matthew.clans;

import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.file.Config;
import dev.matthew.clans.util.StringUtil;
import com.google.common.collect.Lists;
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
        List<String> tags = Lists.newArrayList();

        Clan clan = ClanHandler.getByPlayer(player);
        if (clan != null) {
            tags.addAll(Config.LUNAR.CLAN_TAG.stream().map(string -> StringUtil.translate(string
                    .replaceAll("%name%", player.getName())
                    .replaceAll("%displayName%", player.getDisplayName())
                    .replaceAll("%clanName%", clan.getName())
            )).collect(Collectors.toList()));
        } else {
            tags.addAll(Config.LUNAR.NO_CLAN_TAG.stream().map(string -> StringUtil.translate(string
                    .replaceAll("%name%", player.getName())
                    .replaceAll("%displayName%", player.getDisplayName())
            )).collect(Collectors.toList()));
        }

        return tags;
    }
}

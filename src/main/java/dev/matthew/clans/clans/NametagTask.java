package dev.matthew.clans.clans;

import dev.matthew.clans.clans.handler.clan.Clan;
import dev.matthew.clans.clans.handler.clan.ClanHandler;
import dev.matthew.clans.clans.handler.file.Config;
import dev.matthew.clans.clans.util.StringUtil;
import com.google.common.collect.Lists;
import com.lunarclient.bukkitapi.LunarClientAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class NametagTask implements Runnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(online -> Bukkit.getOnlinePlayers()
                .forEach(player -> LunarClientAPI.getInstance().overrideNametag(online, getTags(online, player), player)));
    }

    private List<String> getTags(Player tagged, Player target) {
        List<String> tags = Lists.newArrayList();

        Clan taggedClan = ClanHandler.getByPlayer(tagged);
        Clan targetClan = ClanHandler.getByPlayer(target);
        String clanTag = taggedClan == null ? null : targetClan == null ?
                Config.LUNAR.ENEMY_NAMETAG.replaceAll("%name%", taggedClan.getName()) : taggedClan.equals(targetClan) ?
                Config.LUNAR.MATE_NAMETAG.replaceAll("%name%", taggedClan.getName()) : Config.LUNAR.ENEMY_NAMETAG.replaceAll("%name%", taggedClan.getName());
        if (clanTag != null) {
            tags.add(StringUtil.translate(clanTag));
        }
        tags.add(0, StringUtil.translate((taggedClan == null || !taggedClan.equals(targetClan) ? Config.LUNAR.ENEMY_COLOR : Config.LUNAR.MATE_COLOR)) + target.getDisplayName());

        return tags;
    }
}

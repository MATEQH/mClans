package dev.matthew.clans.util;

import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.stream.Collectors;

public class BukkitUtil {

    public static List<String> getCompletions(String[] args, List<String> input) {
        return getCompletions(args, input, 80);
    }

    public static List<String> getCompletions(String[] args, List<String> input, int limit) {
        Preconditions.checkNotNull(args);
        Preconditions.checkArgument(args.length != 0);
        String argument = args[(args.length - 1)];
        return input.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length())).limit(limit).collect(Collectors.toList());
    }

    public static OfflinePlayer getPlayer(String name) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
        return offlinePlayer == null ? null : Bukkit.getOfflinePlayer(offlinePlayer.getUniqueId());
    }
}

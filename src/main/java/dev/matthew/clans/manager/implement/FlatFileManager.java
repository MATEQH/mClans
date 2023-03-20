package dev.matthew.clans.manager.implement;

import dev.matthew.clans.manager.Manager;
import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.util.FileUtil;
import org.bson.Document;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

public class FlatFileManager extends Manager {

    public FlatFileManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    public Manager save(Clan clan) {
        ForkJoinPool.commonPool().execute(() -> {
            File file = FileUtil.getOrCreateFile(new File(plugin.getDataFolder(), "clans"), clan.getName().toLowerCase() + ".json");
            FileUtil.writeContent(file, gson.toJson(clan.serialize()));
        });
        return this;
    }

    @Override
    public Manager remove(Clan clan) {
        ForkJoinPool.commonPool().execute(() -> {
            File file = FileUtil.getFile(new File(plugin.getDataFolder(), "clans"), clan.getName().toLowerCase() + ".json");
            if (file != null && file.exists()) {
                file.delete();
            }
        });
        return this;
    }

    @Override
    public Manager loadAll() {
        CompletableFuture.supplyAsync(() -> {
            File path = new File(plugin.getDataFolder(),"clans");
            if (path.exists() && path.listFiles() != null) {
                Arrays.stream(path.listFiles()).forEach(file -> {
                    Clan clan = new Clan(Document.parse(FileUtil.readContent(file)));
                    ClanHandler.getClanMap().put(clan.getId(), clan);
                    clan.getMembers().keySet().forEach(uuid -> ClanHandler.getPlayerMap().put(uuid, clan));
                });
            }
            return null;
        });
        return this;
    }

    @Override
    public Manager saveAll() {
        CompletableFuture.supplyAsync(() -> {
            ClanHandler.getClanMap().values().forEach(clan -> {
                File file = FileUtil.getOrCreateFile(new File(plugin.getDataFolder(), "clans"), clan.getName().toLowerCase() + ".json");
                FileUtil.writeContent(file, gson.toJson(clan.serialize()));
            });
            return null;
        });
        return this;
    }

    @Override
    public void close() {

    }
}

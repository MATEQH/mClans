package com.github.mateqh.clans.handler.manager.implement;

import com.github.mateqh.clans.handler.manager.Manager;
import com.github.mateqh.clans.handler.clan.Clan;
import com.github.mateqh.clans.handler.clan.ClanHandler;
import com.github.mateqh.clans.util.FileUtil;
import org.bson.Document;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

public class FlatFileManager extends Manager {

//    private final File file;

    public FlatFileManager(Plugin plugin) {
        super(plugin);
//        this.file = FileUtil.getOrCreateFile(plugin.getDataFolder(), "clans.json");
    }

//    @Override
//    public Manager save(Clan clan) {
//        saveAll();
//        return this;
//    }
//
//    @Override
//    public Manager remove(Clan clan) {
//        ForkJoinPool.commonPool().execute(() -> FileUtil.writeContent(this.file, gson.toJson(ClanHandler.getClanMap(), typeToken)));
//        return this;
//    }
//
//    @Override
//    public Manager loadAll() {
//        CompletableFuture.supplyAsync(() -> {
//            Map<String, Clan> clanMap = gson.fromJson(FileUtil.readContent(this.file), typeToken);
//            if (clanMap == null || clanMap.isEmpty()) {
//                return null;
//            }
//            ClanHandler.getClanMap().putAll(clanMap);
//            ClanHandler.getClanMap().values().forEach(clan -> {
//                clan.setInvitedPlayers(new HashMap<>());
//                clan.getMembers().keySet().forEach(uuid -> ClanHandler.getPlayerMap().put(uuid, clan));
//            });
//            return clanMap;
//        }).whenComplete((a, b) -> ClanHandler.sortClans());
//        return this;
//    }
//
//    @Override
//    public Manager saveAll() {
//        CompletableFuture.supplyAsync(() -> FileUtil.writeContent(this.file, gson.toJson(ClanHandler.getClanMap(), typeToken)));
//        return this;
//    }

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
                    ClanHandler.getClanMap().put(clan.getName().toLowerCase(), clan);
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

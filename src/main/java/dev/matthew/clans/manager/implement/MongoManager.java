package dev.matthew.clans.manager.implement;

import com.mongodb.*;
import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.file.Config;
import dev.matthew.clans.manager.Manager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

public class MongoManager extends Manager {

    @Getter
    private MongoClient client;
    @Getter
    private MongoDatabase database;
    @Getter
    private MongoCollection<Document> collection;

    public MongoManager(Plugin plugin) {
        super(plugin);
        if (Config.DATABASE.AUTH.LOGIN.ENABLED) {
            MongoCredential credential = MongoCredential.createCredential(Config.DATABASE.AUTH.LOGIN.USER, "admin", Config.DATABASE.AUTH.LOGIN.PASSWORD.toCharArray());
            this.client = new MongoClient(new ServerAddress(Config.DATABASE.AUTH.HOST, Config.DATABASE.AUTH.PORT), credential, MongoClientOptions.builder().build());
        } else {
            this.client = new MongoClient(new ServerAddress(Config.DATABASE.AUTH.HOST, Config.DATABASE.AUTH.PORT), MongoClientOptions.builder().build());
        }


//        String clientUrl = "mongodb://" + Config.DATABASE.AUTH.HOST + ":" + Config.DATABASE.AUTH.PORT +
//                "/" + Config.DATABASE.AUTH.DATABASE;
//        if (Config.DATABASE.AUTH.LOGIN.ENABLED) {
//            clientUrl = "mongodb://" +
//                    Config.DATABASE.AUTH.LOGIN.USER + ":" + Config.DATABASE.AUTH.LOGIN.PASSWORD +
//                    "@" + Config.DATABASE.AUTH.HOST + ":" + Config.DATABASE.AUTH.PORT +
//                    "/" + Config.DATABASE.AUTH.DATABASE;
//
//        }

        this.database = client.getDatabase("mclans");
        this.collection = database.getCollection("clans");
    }

    @Override
    public Manager save(Clan clan) {
        ForkJoinPool.commonPool().execute(() -> this.collection.updateOne(
                Filters.eq("id", clan.getId().toString()),
                new Document("$set", clan.serialize()),
                new UpdateOptions().upsert(true)
        ));
        return this;
    }

    @Override
    public Manager remove(Clan clan) {
        ForkJoinPool.commonPool().execute(() -> this.collection.deleteOne(
                Filters.eq("id", clan.getId().toString())
        ));
        return this;
    }

    @Override
    public Manager loadAll() {
        CompletableFuture.supplyAsync(() -> {
            for (Document document : this.collection.find()) {
                Clan clan = new Clan(document);
                ClanHandler.getClanMap().put(clan.getId(), clan);
                clan.getMembers().keySet().forEach(uuid -> ClanHandler.getPlayerMap().put(uuid, clan));
            }
            return null;
        });
        return this;
    }

    @Override
    public Manager saveAll() {
        CompletableFuture.supplyAsync(() -> {
            ClanHandler.getClanMap().values().forEach(clan -> this.collection.updateOne(
                    Filters.eq("id", clan.getId().toString()),
                    new Document("$set", clan.serialize()),
                    new UpdateOptions().upsert(true)
            ));
            return null;
        });
        return this;
    }

    @Override
    public void close() {
        if (client != null) {
            client.close();
        }
    }
}

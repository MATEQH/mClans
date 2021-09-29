package me.matthew.teams.handler.manager.implement;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import me.matthew.teams.handler.team.Team;
import me.matthew.teams.handler.team.TeamHandler;
import me.matthew.teams.handler.manager.Manager;
import me.matthew.teams.handler.enums.Config;
import org.bson.Document;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
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
        client = Config.getBoolean(Config.DATABASE_AUTH) ? MongoClients.create("mongodb://" +
                Config.getString(Config.DATABASE_USER) + ":" + Config.getString(Config.DATABASE_PASSWORD) +
                "@" + Config.getString(Config.DATABASE_HOST) + ":" + Config.getInt(Config.DATABASE_PORT) +
                "?authSource=" + Config.getString(Config.DATABASE_DATABASE) + "1&ssl=true")
                : MongoClients.create("mongodb://" + Config.getString(Config.DATABASE_HOST) + ":" + Config.getInt(Config.DATABASE_PORT));
        database = client.getDatabase(Config.getString(Config.DATABASE_DATABASE));
        collection = database.getCollection("teams");
    }

    @Override
    public Manager save(Team team) {
        String name = team.getName().toLowerCase();
        ForkJoinPool.commonPool().execute(() -> this.collection.updateOne(
                Filters.eq("_id", name),
                new Document("$set", Document.parse(gson.toJson(team, Team.class))),
                new UpdateOptions().upsert(true)
        ));
        return this;
    }

    @Override
    public Manager remove(Team team) {
        ForkJoinPool.commonPool().execute(() -> this.collection.deleteOne(
                Filters.eq("_id", team.getName().toLowerCase())
        ));
        return this;
    }

    @Override
    public Manager loadAll() {
        CompletableFuture.supplyAsync(() -> {
            Map<String, Team> teams = new HashMap<>();
            for (Document document : this.collection.find()) {
                Team team = gson.fromJson(document.toJson(), Team.class);
                teams.put(team.getName().toLowerCase(), team);
            }
            TeamHandler.getTeamMap().putAll(teams);
            TeamHandler.getTeamMap().values().forEach(team -> {
                team.setInvitedPlayers(new HashMap<>());
                team.getMembers().keySet().forEach(uuid -> TeamHandler.getPlayerMap().put(uuid, team));
            });
            TeamHandler.sortTeams();
            return teams;
        });
        return this;
    }

    @Override
    public Manager saveAll() {
        CompletableFuture.supplyAsync(() -> {
            TeamHandler.getTeamMap().values().forEach(team -> save(team));
            return TeamHandler.getTeamMap();
        });
        return this;
    }
}

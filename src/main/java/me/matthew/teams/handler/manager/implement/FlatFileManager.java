package me.matthew.teams.handler.manager.implement;

import me.matthew.teams.handler.team.Team;
import me.matthew.teams.handler.team.TeamHandler;
import me.matthew.teams.handler.manager.Manager;
import me.matthew.teams.util.FileUtil;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

public class FlatFileManager extends Manager {

    private File file;

    public FlatFileManager(Plugin plugin) {
        super(plugin);
        this.file = FileUtil.getOrCreateFile(plugin.getDataFolder(), "teams.json");
    }

    @Override
    public Manager save(Team team) {
        saveAll();
        return this;
    }

    @Override
    public Manager remove(Team team) {
        ForkJoinPool.commonPool().execute(() -> FileUtil.writeContent(this.file, gson.toJson(TeamHandler.getTeamMap(), typeToken)));
        return this;
    }

    @Override
    public Manager loadAll() {
        CompletableFuture.supplyAsync(() -> {
            Map<String, Team> teamMap = gson.fromJson(FileUtil.readContent(this.file), typeToken);
            if (teamMap == null || teamMap.isEmpty()) {
                return null;
            }
            TeamHandler.getTeamMap().putAll(teamMap);
            TeamHandler.getTeamMap().values().forEach(team -> {
                team.setInvitedPlayers(new HashMap<>());
                team.getMembers().keySet().forEach(uuid -> TeamHandler.getPlayerMap().put(uuid, team));
            });
            TeamHandler.sortTeams();
            return teamMap;
        });
        return this;
    }

    @Override
    public Manager saveAll() {
        CompletableFuture.supplyAsync(() -> FileUtil.writeContent(this.file, gson.toJson(TeamHandler.getTeamMap(), typeToken)));
        return this;
    }

//    @Override
//    public void save(Team team) {
//        super.save(team);
//        String name = team.getName().toLowerCase();
//        File file = FileUtil.getOrCreateFile(new File(plugin.getDataFolder(), "teams"), name + ".json");
//        FileUtil.writeContent(file, gson.toJson(team));
//    }
//
//    @Override
//    public void remove(Team team) {
//        super.remove(team);
//        String name = team.getName().toLowerCase();
//        File file = FileUtil.getFile(new File(plugin.getDataFolder(), "teams"), name + ".json");
//        if (file != null) {
//            file.delete();
//        }
//    }
//
//    @Override
//    public void loadAll() {
//        super.loadAll();
//        File path = new File(plugin.getDataFolder(),"teams");
//        if (path.exists()) {
//            Arrays.stream(path.listFiles()).forEach(teamFile -> {
//                Team team = gson.fromJson(FileUtil.readContent(teamFile), Team.class);
//                team.setInvitedPlayers(new HashMap<>());
//                TeamHandler.createTeam(team);
//            });
//        }
//    }
//
//    @Override
//    public void saveAll() {
//        super.saveAll();
//        TeamHandler.getTeamMap().values().forEach(clan -> save(clan));
//    }
}

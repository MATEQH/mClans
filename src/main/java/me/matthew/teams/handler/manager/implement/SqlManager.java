package me.matthew.teams.handler.manager.implement;

import me.matthew.teams.handler.manager.Manager;
import me.matthew.teams.handler.team.Team;
import org.bukkit.plugin.Plugin;

public class SqlManager extends Manager {

    public SqlManager(Plugin plugin) {
        super(plugin);
    }

    @Override
    public Manager save(Team team) {
        return this;
    }

    @Override
    public Manager remove(Team team) {
        return this;
    }

    @Override
    public Manager loadAll() {
        return this;
    }

    @Override
    public Manager saveAll() {
        return this;
    }
}

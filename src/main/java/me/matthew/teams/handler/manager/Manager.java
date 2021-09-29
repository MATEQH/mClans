package me.matthew.teams.handler.manager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.matthew.teams.handler.Team;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Type;
import java.util.Map;

public abstract class Manager {

    @Getter
    protected Plugin plugin;
    @Getter
    protected static Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    @Getter
    protected static Type typeToken = new TypeToken<Map<String, Team>>() {}.getType();

    public Manager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void save(Team team) {}
    public void remove(Team team) {}
    public void loadAll() {}
    public void saveAll() {}
}

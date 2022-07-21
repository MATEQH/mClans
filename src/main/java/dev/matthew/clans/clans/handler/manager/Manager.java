package dev.matthew.clans.clans.handler.manager;

import dev.matthew.clans.clans.handler.clan.Clan;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Type;
import java.util.Map;

public abstract class Manager {

    @Getter
    protected Plugin plugin;
    @Getter
    protected static Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    @Getter
    protected static Type typeToken = new TypeToken<Map<String, Clan>>() {}.getType();

    public Manager(Plugin plugin) {
        this.plugin = plugin;
    }

    public abstract Manager save(Clan clan);
    public abstract Manager remove(Clan clan);
    public abstract Manager loadAll();
    public abstract Manager saveAll();
    public abstract void close();
}

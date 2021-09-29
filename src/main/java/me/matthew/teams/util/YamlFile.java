package me.matthew.teams.util;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class YamlFile {

    @Getter
    private final Plugin plugin;
    private final File file;
    private YamlConfiguration config;

    public YamlFile(Plugin plugin, File file) {
        this.plugin = plugin;
        this.file = file;
    }

    public YamlFile(Plugin plugin, String file) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), file.replace(".yml", "") + ".yml");
    }

    public YamlFile createFile(boolean saveResource) {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                if (saveResource) {
                    plugin.saveResource(file.getName(), true);
                } else {
                    file.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public YamlFile loadConfig(boolean saveResource) {
        createFile(saveResource);
        config = YamlConfiguration.loadConfiguration(file);
        return this;
    }

    public YamlFile reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
        return this;
    }

    public YamlFile saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean exists() {
        return file.exists();
    }

    public boolean delete() {
        if (file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    public boolean contains(String path) {
        return config.contains(path);
    }

    public ConfigurationSection getConfigSection(String path) {
        return config.getConfigurationSection(path);
    }

    public Object get(String path) {
        return config.get(path);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public float getFloat(String path) {
        return config.getFloat(path);
    }

    public long getLong(String path) {
        return config.getLong(path);
    }

    public List<?> getList(String path) {
        return config.getList(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public List<Boolean> getBooleanList(String path) {
        return config.getBooleanList(path);
    }

    public List<Integer> getIntList(String path) {
        return config.getIntegerList(path);
    }

    public List<Double> getDoubleList(String path) {
        return config.getDoubleList(path);
    }

    public List<Float> getFloatList(String path) {
        return config.getFloatList(path);
    }

    public List<Long> getLongList(String path) {
        return config.getLongList(path);
    }

    public ItemStack getItemStack(String path) {
        return config.getItemStack(path);
    }
}

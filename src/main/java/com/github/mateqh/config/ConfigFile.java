package com.github.mateqh.config;

import com.github.mateqh.config.resolver.ValueResolver;
import com.google.common.collect.Lists;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigFile {

    private final Plugin plugin;
    private final String fileName;
    private final File file;
    private YamlConfiguration configuration;
    private List<Class<?>> annotatedClasses;

    public ConfigFile(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName.replaceAll(".yml", "");
        this.file = new File(plugin.getDataFolder(), fileName + ".yml");
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.annotatedClasses = Lists.newArrayList();
    }

    public ConfigFile(Plugin plugin, String path, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName.replaceAll(".yml", "");
        this.file = new File(new File(plugin.getDataFolder(), path), fileName + ".yml");
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.annotatedClasses = Lists.newArrayList();
    }

    public ConfigFile createFile(boolean saveResource) {
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

    public ConfigFile loadConfig() {
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ConfigFile reloadConfig() {
        loadConfig();
        setAnnotatedFields();
        return this;
    }

    public ConfigFile saveConfig() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ConfigFile registerClass(Class<?> clazz) {
        if (!annotatedClasses.contains(clazz)) {
            annotatedClasses.add(clazz);
        }
        return this;
    }

    public ConfigFile registerClass(Class<?>... classes) {
        return registerClass(Arrays.asList(classes));
    }

    public ConfigFile registerClass(Collection<Class<?>> classes) {
        classes.stream().filter(clazz -> !annotatedClasses.contains(clazz)).forEach(clazz -> {
            annotatedClasses.add(clazz);
        });
        return this;
    }

    public ConfigFile unregisterClass(Class<?> clazz) {
        if (annotatedClasses.contains(clazz)) {
            annotatedClasses.remove(clazz);
        }
        return this;
    }

    public ConfigFile unregisterClass(Class<?>... classes) {
        return unregisterClass(Arrays.asList(classes));
    }

    public ConfigFile unregisterClass(Collection<Class<?>> classes) {
        classes.stream().filter(clazz -> annotatedClasses.contains(clazz)).forEach(clazz -> {
            annotatedClasses.remove(clazz);
        });
        return this;
    }

    public ConfigFile export() {
        return export(false);
    }

    public ConfigFile export(boolean overwrite) {
        createFile(false);
        loadConfig();
        annotatedClasses.forEach(clazz -> {
            List<Field> fields = Lists.newArrayList();
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()).stream().filter(declaredField ->
                    declaredField.isAnnotationPresent(ConfigPath.class))
                    .collect(Collectors.toList()));
            addFieldsRecursively(clazz, fields);
            fields.forEach(field -> {
                ConfigPath configPath = field.getAnnotation(ConfigPath.class);
                String path = configPath.path();
                String file = configPath.name();
                if (!file.equals("") && !file.equalsIgnoreCase(fileName)) {
                    return;
                }
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                if (!overwrite && configuration.contains(path)) {
                    return;
                }
                try {
                    configuration.set(path, field.get(clazz));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        });
        saveConfig();
        return this;
    }

    public ConfigFile export(Field field) {
        return export(field, true);
    }

    public ConfigFile export(Field field, boolean overwrite) {
        createFile(false);
        loadConfig();
        ConfigPath configPath = field.getAnnotation(ConfigPath.class);
        String path = configPath.path();
        String file = configPath.name();
        if (!file.equals("") && !file.equalsIgnoreCase(fileName)) {
            return this;
        }
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        if (!overwrite && configuration.contains(path)) {
            return this;
        }
        try {
            configuration.set(path, field.get(field.getDeclaringClass()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        saveConfig();
        return this;
    }

    public ConfigFile setAnnotatedFields() {
        annotatedClasses.forEach(clazz -> {
            if (!annotatedClasses.contains(clazz)) {
                annotatedClasses.add(clazz);
            }
            List<Field> fields = Lists.newArrayList();
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()).stream().filter(declaredField ->
                    declaredField.isAnnotationPresent(ConfigPath.class))
                    .collect(Collectors.toList()));
            addFieldsRecursively(clazz, fields);
            fields.forEach(field -> {
                ConfigPath configPath = field.getAnnotation(ConfigPath.class);
                String path = configPath.path();
                String file = configPath.name();
                if (!file.equals("") && !file.equalsIgnoreCase(fileName)) {
                    return;
                }
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                ValueResolver<?> resolver = ConfigHelper.getResolverMap().get(field.getType());
                if (resolver == null) {
                    ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                    if (parameterizedType == null) {
                        return;
                    }
                    resolver = ConfigHelper.getResolverTable().get(field.getType(), parameterizedType.getActualTypeArguments()[0]);
                    if (resolver == null) {
                        return;
                    }
                }

                Object value = configuration.get(path);
                if (value == null) {
                    return;
                }
                try {
                    field.set(clazz, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        });
        return this;
    }

    public ConfigFile setAnnotatedField(Field field) {
        return setAnnotatedField(field, true);
    }

    public ConfigFile setAnnotatedField(Field field, boolean overwrite) {
        ConfigPath configPath = field.getAnnotation(ConfigPath.class);
        String path = configPath.path();
        String file = configPath.name();
        if (!file.equals("") && !file.equalsIgnoreCase(fileName)) {
            return this;
        }
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        try {
            if (overwrite && field.get(field.getDeclaringClass()) == null) {
                return this;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        ValueResolver<?> resolver = ConfigHelper.getResolverMap().get(field.getType());
        if (resolver == null) {
            ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
            if (parameterizedType == null) {
                return this;
            }
            resolver = ConfigHelper.getResolverTable().get(field.getType(), parameterizedType.getActualTypeArguments()[0]);
            if (resolver == null) {
                return this;
            }
        }

        Object value = configuration.get(path);
        if (value == null) {
            return this;
        }
        try {
            field.set(field.getDeclaringClass(), value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void addFieldsRecursively(Class<?> clazz, Collection<Field> fields) {
        Arrays.stream(clazz.getDeclaredClasses()).forEach(declaredClass -> {
            addFieldsRecursively(declaredClass, fields);
            fields.addAll(Arrays.asList(declaredClass.getDeclaredFields()).stream().filter(declaredField ->
                    declaredField.isAnnotationPresent(ConfigPath.class))
                    .collect(Collectors.toList())
            );
        });
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }
}

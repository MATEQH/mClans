package com.github.mateqh.config;

import org.bukkit.plugin.java.JavaPlugin;

public class ExampleConfig extends JavaPlugin {

    private ConfigFile simpleConfig, exportConfig;

    @Override
    public void onEnable() {
        simpleConfig = new ConfigFile(this, "com.github.mateqh.config")
                .registerClass(this.getClass())
                .createFile(true)
                .loadConfig();
        simpleConfig = new ConfigFile(this, "exported-config")
                .registerClass(this.getClass())
                .export()
                .loadConfig();
        System.out.println(exampleString1);
        System.out.println(exampleString2);
        System.out.println(exampleInt);
        System.out.println(exampleLong);
        System.out.println(exampleFloat);
    }

    @ConfigPath(path = "example-string1", name = "com.github.mateqh.config")
    public static String exampleString1;
    @ConfigPath(path = "example-string2")
    public static String exampleString2 = "Example string";
    @ConfigPath(path = "example-int")
    public static int exampleInt = 2021;
    @ConfigPath(path = "example-long")
    public static long exampleLong = System.currentTimeMillis();
    @ConfigPath(path = "example-float")
    public static float exampleFloat = 42.5f;
}

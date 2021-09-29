package me.matthew.teams.handler.enums;

import me.matthew.teams.handler.manager.Manager;
import me.matthew.teams.handler.manager.implement.FlatFileManager;
import me.matthew.teams.handler.manager.implement.MongoManager;
import org.bukkit.plugin.Plugin;

public enum ManagerType {

    FLAT,
    MONGO,
    SQL;

    public static Manager getManager(ManagerType managerType, Plugin plugin) {
        return managerType == FLAT ?
                new FlatFileManager(plugin) :
                managerType == MONGO ? new MongoManager(plugin) :
                        managerType == SQL ?
                                null :
                                null;
    }
}

package dev.matthew.clans.clans.handler.enums;

import dev.matthew.clans.clans.handler.manager.Manager;
import dev.matthew.clans.clans.handler.manager.implement.FlatFileManager;
import dev.matthew.clans.clans.handler.manager.implement.MongoManager;
import dev.matthew.clans.clans.handler.manager.implement.SqlManager;
import org.bukkit.plugin.Plugin;

public enum ManagerType {

    FLAT,
    MONGO,
    SQL;

    public static Manager getManager(String type, Plugin plugin) {
        ManagerType managerType = valueOf(type);
        return managerType == FLAT ?
                new FlatFileManager(plugin) :
                managerType == MONGO ? new MongoManager(plugin) :
                        managerType == SQL ?
                                new SqlManager(plugin) :
                                null;
    }
}

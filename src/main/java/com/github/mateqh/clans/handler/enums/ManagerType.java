package com.github.mateqh.clans.handler.enums;

import com.github.mateqh.clans.handler.manager.Manager;
import com.github.mateqh.clans.handler.manager.implement.FlatFileManager;
import com.github.mateqh.clans.handler.manager.implement.MongoManager;
import com.github.mateqh.clans.handler.manager.implement.SqlManager;
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

package me.matthew.teams.handler.enums;

import lombok.Getter;
import me.matthew.teams.Teams;

public enum Config {

    DATABASE_TYPE("DATABASE_TYPE"),
    DATABASE_HOST("DATABASE.HOST"),
    DATABASE_PORT("DATABASE.PORT"),
    DATABASE_DATABASE("DATABASE.DATABASE"),
    DATABASE_AUTH("DATABASE.AUTH.ENABLED"),
    DATABASE_USER("DATABASE.AUTH.USER"),
    DATABASE_PASSWORD("DATABASE.AUTH.PASSWORD"),
    ONLINE_PREFIX("ONLINE_PREFIX"),
    OFFLINE_PREFIX("OFFLINE_PREFIX"),
    CREATE_BROADCAST("CREATE_BROADCAST"),
    DISBAND_BROADCAST("DISBAND_BROADCAST"),
    TEAMS_PER_PAGE_LIST("TEAMS_PER_PAGE.LIST"),
    TEAMS_PER_PAGE_TOP("TEAMS_PER_PAGE.TOP"),
    TEAM_SIZE("TEAM_SIZE"),
    NAME_MIN_LENGTH("NAME_MIN_LENGTH"),
    NAME_MAX_LENGTH("NAME_MAX_LENGTH"),
    CHAT_PREFIX("CHAT_PREFIX"),
    INVITATION_EXPIRE("INVITATION_EXPIRE");

    @Getter
    private String path;

    Config(String path) {
        this.path = path;
    }

    public static boolean getBoolean(Config config) {
        return Teams.getInstance().getConfigFile().getBoolean(config.getPath());
    }

    public static String getString(Config config) {
        return Teams.getInstance().getConfigFile().getString(config.getPath());
    }

    public static int getInt(Config config) {
        return Teams.getInstance().getConfigFile().getInt(config.getPath());
    }

    public static long getLong(Config config) {
        return Teams.getInstance().getConfigFile().getLong(config.getPath());
    }
}

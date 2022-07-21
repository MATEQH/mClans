package dev.matthew.clans.clans.handler.file;

import dev.matthew.clans.config.ConfigPath;

import java.util.Arrays;
import java.util.List;

public class Config {

    public static class DATABASE {

        @ConfigPath(path = "DATABASE.TYPE")
        public static String TYPE = "FLAT";

        public static class AUTH {

            @ConfigPath(path = "DATABASE.AUTH.HOST")
            public static String HOST = "localhost";
            @ConfigPath(path = "DATABASE.AUTH.PORT")
            public static int PORT = 27017;
            @ConfigPath(path = "DATABASE.AUTH.DATABASE")
            public static String DATABASE = "mclans";

            public static class LOGIN {

                @ConfigPath(path = "DATABASE.AUTH.LOGIN.ENABLED")
                public static boolean ENABLED = false;
                @ConfigPath(path = "DATABASE.AUTH.LOGIN.USER")
                public static String USER = "admin";
                @ConfigPath(path = "DATABASE.AUTH.LOGIN.PASSWORD")
                public static String PASSWORD = "";
            }
        }
    }

    @ConfigPath(path = "ONLINE_PREFIX")
    public static String ONLINE_PREFIX = "&a";
    @ConfigPath(path = "OFFLINE_PREFIX")
    public static String OFFLINE_PREFIX = "&7";
    @ConfigPath(path = "BROADCAST_CREATE")
    public static boolean BROADCAST_CREATE = true;
    @ConfigPath(path = "BROADCAST_DISBAND")
    public static boolean BROADCAST_DISBAND = true;

    public static class CLAN {

        @ConfigPath(path = "CLAN.MAX_SIZE")
        public static int MAX_SIZE = 8;
        @ConfigPath(path = "CLAN.LIST")
        public static int LIST = 10;
        @ConfigPath(path = "CLAN.TOP")
        public static int TOP = 10;
        @ConfigPath(path = "CLAN.BLOCKED_NAMES")
        public static List<String> BLOCKED_NAMES = Arrays.asList("staffs");
        @ConfigPath(path = "CLAN.NAME_MIN_LENGTH")
        public static int NAME_MIN_LENGTH = 3;
        @ConfigPath(path = "CLAN.NAME_MAX_LENGTH")
        public static int NAME_MAX_LENGTH = 8;
        @ConfigPath(path = "CLAN.CHAT_PREFIX")
        public static String CHAT_PREFIX = "&8[&a%name%&8]&r ";
        @ConfigPath(path = "CLAN.CLAN_CHAT_COLORED_MESSAGE")
        public static boolean CLAN_CHAT_COLORED_MESSAGE = false;
        @ConfigPath(path = "CLAN.CLAN_CHAT_FORMAT")
        public static String CLAN_CHAT_FORMAT = "&6(Clan) &7%playerName% &7» %message%";
        @ConfigPath(path = "CLAN.INVITATION_EXPIRE")
        public static long INVITATION_EXPIRE = 30000L;
    }

    public static class LUNAR {

        @ConfigPath(path = "LUNAR.MATE_COLOR")
        public static String MATE_COLOR = "&a";
        @ConfigPath(path = "LUNAR.ENEMY_COLOR")
        public static String ENEMY_COLOR = "&c";
        @ConfigPath(path = "LUNAR.MATE_NAMETAG")
        public static String MATE_NAMETAG = "&7[&a%name%&7]";
        @ConfigPath(path = "LUNAR.ENEMY_NAMETAG")
        public static String ENEMY_NAMETAG = "&7[&c%name%&7]";
    }
}

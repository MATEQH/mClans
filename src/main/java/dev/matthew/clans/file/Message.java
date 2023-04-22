package dev.matthew.clans.file;

import dev.matthew.clans.util.StringUtil;
import dev.matthew.config.ConfigPath;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Message {

    public static void send(CommandSender sender, String string) {
        sender.sendMessage(StringUtil.translate(string));
    }

    public static void send(CommandSender sender, Collection<String> collection) {
        collection.forEach(string -> sender.sendMessage(StringUtil.translate(string)));
    }

    public static void sendGlobal(String string) {
        Bukkit.getOnlinePlayers().forEach(online -> online.sendMessage(StringUtil.translate(string)));
    }

    public static void sendGlobal(Collection<String> collection) {
        Bukkit.getOnlinePlayers().forEach(online -> collection.forEach(string -> online.sendMessage(StringUtil.translate(string))));
    }

    @ConfigPath(path = "VAULT_NOT_FOUND")
    public static String VAULT_NOT_FOUND = "Vault plugin not found, deposit and withdraw subcommands are disabled.";
    @ConfigPath(path = "NO_PERMISSION")
    public static String NO_PERMISSION = "&cYou don't have permission.";
    @ConfigPath(path = "INVALID_ARGUMENTS")
    public static String INVALID_ARGUMENTS = "&cInvalid arguments.";
    @ConfigPath(path = "BLOCKED_CLAN_NAME")
    public static String BLOCKED_CLAN_NAME = "&eThis clan name is &cforbidden&e.";
    @ConfigPath(path = "CLAN_ALREADY_EXISTS")
    public static String CLAN_ALREADY_EXISTS = "&cThat clan already exists.";
    @ConfigPath(path = "CLAN_DOES_NOT_EXISTS")
    public static String CLAN_DOES_NOT_EXISTS = "&cClan not found.";
    @ConfigPath(path = "ALREADY_IN_CLAN")
    public static String ALREADY_IN_CLAN = "&ePlayer %targetName% is already in clan.";
    @ConfigPath(path = "ALREADY_IN_CLAN_SELF")
    public static String ALREADY_IN_CLAN_SELF = "&eYou are already in clan.";
    @ConfigPath(path = "ALREADY_IN_CLAN_OTHERS")
    public static String ALREADY_IN_CLAN_OTHERS = "&ePlayer %targetName% is already in your clan.";
    @ConfigPath(path = "NOT_IN_CLAN")
    public static String NOT_IN_CLAN = "&ePlayer %targetName% is not in a &cclan.";
    @ConfigPath(path = "NOT_IN_CLAN_SELF")
    public static String NOT_IN_CLAN_SELF = "&eYou are not in a &cclan.";
    @ConfigPath(path = "NOT_IN_CLAN_OTHERS")
    public static String NOT_IN_CLAN_OTHERS = "&ePlayer %targetName% is not in your &cclan.";
    @ConfigPath(path = "MUST_BE_LEADER")
    public static String MUST_BE_LEADER = "&eYou must be clan leader.";
    @ConfigPath(path = "CLAN_NO_PERMISSION")
    public static String CLAN_NO_PERMISSION = "&eYou must be at least %role%.";
    @ConfigPath(path = "CLAN_NO_PERMISSION_ROLE")
    public static String CLAN_NO_PERMISSION_ROLE = "&ePlayer %targetName% has equal or higher role than you.";
    @ConfigPath(path = "CANNOT_DO_THIS_YOURSELF")
    public static String CANNOT_DO_THIS_YOURSELF = "&cYou can't do this with yourself.";
    @ConfigPath(path = "PLAYER_NOT_FOUND")
    public static String PLAYER_NOT_FOUND = "&cPlayer %targetName% is not found.";
    @ConfigPath(path = "HELP_COMMAND")
    public static List<String> HELP_COMMAND = Arrays.asList(
            "",
            "&6&lClan help",
            "",
            "&6General commands",
            "&e/%label% create <clanName> &7Create a new clan",
            "&e/%label% accept <clanName> &7Accept pending invitation",
            "&e/%label% leave &7Leave your current clan",
            "&e/%label% deposit <amount|all> &7Deposit money into your clan balance",
            "",
            "&6Information commands",
            "&e/%label% info [playerName|clanName] &7Display clan information",
            "&e/%label% list &7Show list of clans online (sorted by most online)",
            "&e/%label% top &7Show list of clans points (sorted by most points)",
            "",
            "&6Captains commands",
            "&e/%label% invite <playerName> &7Invite a player to your clan",
            "&e/%label% uninvite <playerName> &7Revoke an invitation",
            "&e/%label% invites &7List all open invitations",
            "&e/%label% kick <playerName> &7Kick a player from your clan",
            "&e/%label% withdraw <amount|all> &7Withdraw money from your clan's balance",
            "",
            "&6Leader commands",
            "&e/%label% rename <name> &7Rename your clan.",
            "&e/%label% captains <add|remove> <playerName> &7Add or remove a captain.",
            "&e/%label% leader <playerName> &7Sets the new leader for your clan.",
            "&e/%label% disband &7Disband your clan",
            ""
    );
    @ConfigPath(path = "STAFF_COMMAND")
    public static List<String> STAFF_COMMAND = Arrays.asList(
            "",
            "&6&lStaff command",
            "",
            "&e/%label% forcedisband <clanName> &7Force disband a clan",
            "&e/%label% points <clanName> <add|set|remove> <amount> &7Set points of a clan",
            ""
    );
    @ConfigPath(path = "COMMAND_ONLY_FOR_PLAYERS")
    public static String COMMAND_ONLY_FOR_PLAYERS = "&cThis command is only for players.";

    public static class FORCE_DISBAND_COMMAND {

        @ConfigPath(path = "FORCE_DISBAND_COMMAND.USAGE")
        public static String USAGE = "&cUsage: /%label% forcedisband <name>";
        @ConfigPath(path = "FORCE_DISBAND_COMMAND.DISBANDED")
        public static String DISBANDED = "&eClan &c%name% was disbanded by &c%playerName%.";
    }

    public static class CAPTAINS_COMMAND {

        @ConfigPath(path = "CAPTAINS_COMMAND.USAGE")
        public static String USAGE = "&cUsage: /%label% captains <add|remove> <playerName>";
        @ConfigPath(path = "CAPTAINS_COMMAND.CANNOT_PROMOTE_SELF")
        public static String CANNOT_PROMOTE_SELF = "&eYou cannot promote yourself.";
        @ConfigPath(path = "CAPTAINS_COMMAND.NOT_ADDED")
        public static String NOT_ADDED = "&e%targetName% is not captain.";
        @ConfigPath(path = "CAPTAINS_COMMAND.ALREADY_ADDED")
        public static String ALREADY_ADDED = "&e%targetName% is already captain.";
        @ConfigPath(path = "CAPTAINS_COMMAND.REMOVED")
        public static String REMOVED = "&e%targetName% has demoted to member by %playerName%.";
        @ConfigPath(path = "CAPTAINS_COMMAND.ADDED")
        public static String ADDED = "&e%targetName% has promoted to captain by %playerName%.";
    }

    public static class CREATE_COMMAND {

        @ConfigPath(path = "CREATE_COMMAND.USAGE")
        public static String USAGE = "&cUsage: /%label% create <name>";
        @ConfigPath(path = "CREATE_COMMAND.NAME_TOO_SHORT")
        public static String NAME_TOO_SHORT = "&eMinimum clan name size is 3 characters.";
        @ConfigPath(path = "CREATE_COMMAND.NAME_TOO_LONG")
        public static String NAME_TOO_LONG = "&eMaximum clan name size is 8 characters.";
        @ConfigPath(path = "CREATE_COMMAND.NAME_NOT_ALPHANUMERIC")
        public static String NAME_NOT_ALPHANUMERIC = "&eClan name must be &calphanumeric&e.";
        @ConfigPath(path = "CREATE_COMMAND.CREATED")
        public static String CREATED = "&eClan &a%name% &ewas created by &a%playerName%&e.";
    }

    public static class DEPOSIT_COMMAND {

        @ConfigPath(path = "DEPOSIT_COMMAND.USAGE")
        public static String USAGE = "&cUsage: /%label% deposit <amount>";
        @ConfigPath(path = "DEPOSIT_COMMAND.CAN_NOT_DEPOSIT_ZERO")
        public static String CAN_NOT_DEPOSIT_ZERO = "&eYou cannot deposit $0.";
        @ConfigPath(path = "DEPOSIT_COMMAND.NOT_ENOUGH_MONEY")
        public static String NOT_ENOUGH_MONEY = "&eYou only have $%amount%.";
        @ConfigPath(path = "DEPOSIT_COMMAND.DEPOSITED")
        public static String DEPOSITED = "&e%playerName% deposited $%amount% into clan's balance.";
    }

    public static class DISBAND_COMMAND {

        @ConfigPath(path = "DISBAND_COMMAND.DISBANDED")
        public static String DISBANDED = "&eClan &c%name% was disbanded by &c%playerName%.";
    }

    public static class INFO_COMMAND {

        @ConfigPath(path = "INFO_COMMAND.USAGE")
        public static String USAGE = "&cUsage: /%label% info [playerName|clanName]";
        @ConfigPath(path = "INFO_COMMAND.LEADER")
        public static String LEADER = "&eLeader: %leader%";
        @ConfigPath(path = "INFO_COMMAND.CAPTAINS")
        public static String CAPTAINS = "&eCaptains: %captains%";
        @ConfigPath(path = "INFO_COMMAND.MEMBERS")
        public static String MEMBERS = "&eMembers: %members%";
        @ConfigPath(path = "INFO_COMMAND.DETAILS")
        public static List<String> DETAILS = Arrays.asList(
                "&6%name% &7[%onlineSize%/%size%]",
                "%leader%",
                "%captains%",
                "%members%",
                "&eBalance: &6$%balance%",
                "&ePoints: &6%points%",
                "&eKills: &6%kills%"
        );

        public static class FOUND_BY {

            @ConfigPath(path = "INFO_COMMAND.FOUND_BY.ID")
            public static String ID = "&eClan found by id.";
            @ConfigPath(path = "INFO_COMMAND.FOUND_BY.CLAN")
            public static String CLAN = "&eClan found by clan name.";
            @ConfigPath(path = "INFO_COMMAND.FOUND_BY.PLAYER")
            public static String PLAYER = "&eClan found by player name.";
        }
    }

    public static class INVITE_COMMAND {

        @ConfigPath(path = "INVITE_COMMAND.USAGE")
        public static String USAGE = "&cUsage: /%label% invite <playerName>";
        @ConfigPath(path = "INVITE_COMMAND.ALREADY_INVITED")
        public static String ALREADY_INVITED = "&ePlayer &c%targetName% &eis already invited to clan.";
        @ConfigPath(path = "INVITE_COMMAND.CLAN_FULL")
        public static String CLAN_FULL = "&eYour clan has already has max member count.";
        @ConfigPath(path = "INVITE_COMMAND.INVITED_TARGET")
        public static String INVITED_TARGET = "<yellow>You have been invited to clan <red>%name%<yellow>. <click:run_command:/clan join %name%><hover:show_text:\"<gray>Click here to join clan %name%\"><green><bold>JOIN";
        @ConfigPath(path = "INVITE_COMMAND.INVITED")
        public static String INVITED = "&ePlayer &c%targetName% &ehas been invited to clan.";
    }

    public static class INVITES_COMMAND {

        @ConfigPath(path = "INVITES_COMMAND.NO_PENDING")
        public static String NO_PENDING = "&cNo pending invites.";
        @ConfigPath(path = "INVITES_COMMAND.INVITES")
        public static String INVITES = "&ePending invites = &r%invites%";
    }

    public static class JOIN_COMMAND {

        @ConfigPath(path = "JOIN_COMMAND.USAGE")
        public static String USAGE = "&cUsage: /%label% join <clanName>";
        @ConfigPath(path = "JOIN_COMMAND.NOT_INVITED")
        public static String NOT_INVITED = "&eYou are not invited to clan %name%.";
        @ConfigPath(path = "JOIN_COMMAND.CLAN_FULL")
        public static String CLAN_FULL = "&eClan %name% already has max member count.";
        @ConfigPath(path = "JOIN_COMMAND.JOINED")
        public static String JOINED = "&ePlayer %playerName% has joined clan.";
    }

    public static class KICK_COMMAND {

        @ConfigPath(path = "KICK_COMMAND.USAGE")
        public static String USAGE = "&cUsage: /%label% kick <playerName>";
        @ConfigPath(path = "KICK_COMMAND.KICKED_SELF")
        public static String KICKED_SELF = "&eYou have been kicked from clan %name%.";
        @ConfigPath(path = "KICK_COMMAND.KICKED_OTHERS")
        public static String KICKED_OTHERS = "&ePlayer %targetName% has been kicked from clan.";
    }

    public static class LEADER_COMMAND {

        @ConfigPath(path = "LEADER_COMMAND.USAGE")
        public static String USAGE = "&cUsage: /%label% leader <playerName>";
        @ConfigPath(path = "LEADER_COMMAND.ALREADY_LEADER")
        public static String ALREADY_LEADER = "&eYou are already leader.";
        @ConfigPath(path = "LEADER_COMMAND.LEADER_CHANGED")
        public static String LEADER_CHANGED = "&e%playerName% transferred clan leadership to %targetName%.";
    }

    public static class LEAVE_COMMAND {

        @ConfigPath(path = "LEAVE_COMMAND.LEADER_LEAVE")
        public static String LEADER_LEAVE = "&eYou are clan leader and therefore cannot leave this clan.";
        @ConfigPath(path = "LEAVE_COMMAND.LEFT_SELF")
        public static String LEFT_SELF = "&eYou have left clan %name%.";
        @ConfigPath(path = "LEAVE_COMMAND.LEFT_OTHERS")
        public static String LEFT_OTHERS = "&ePlayer %playerName% has left the clan.";
    }

    public static class LIST_COMMAND {

        public static class PREFIX {
            @ConfigPath(path = "LIST_COMMAND.PREFIX.OWN")
            public static String OWN = "<green>";
            @ConfigPath(path = "LIST_COMMAND.PREFIX.ENEMY")
            public static String ENEMY = "<red>";
            @ConfigPath(path = "LIST_COMMAND.PREFIX.ENEMY_ONLINE")
            public static String ENEMY_ONLINE = "<yellow>";
        }

        @ConfigPath(path = "LIST_COMMAND.NO_CLANS")
        public static String NO_CLANS = "&cThere are no clans yet.";
        @ConfigPath(path = "LIST_COMMAND.PAGE_NOT_FOUND")
        public static String PAGE_NOT_FOUND = "&cPage not found.";
        @ConfigPath(path = "LIST_COMMAND.CLAN_FORMAT")
        public static String CLAN_FORMAT = "<gray>%position%. %prefix%<click:run_command:/clan info %id%><hover:show_text:\"<yellow>Click here to view info of clan %prefix%%name%\">%name%</hover> <green>(%onlineSize%/%size%)";
        @ConfigPath(path = "LIST_COMMAND.LISTED")
        public static List<String> LISTED = Arrays.asList(
                "",
                "&6Clan List &7(Page %currentPage%/%maxPage%)",
                "%clans%",
                "&7You are currently on &rPage %currentPage%/%maxPage%&7.",
                "&7To view other pages, use &e/%label% list <page#>",
                ""
        );
    }

    public static class RENAME_COMMAND {

        @ConfigPath(path = "RENAME_COMMAND.USAGE")
        public static String USAGE = "&cUsage: /%label% rename <name>";
        @ConfigPath(path = "RENAME_COMMAND.ON_COOLDOWN")
        public static String ON_COOLDOWN = "&7You cannot rename your clan until &c%remaining%&7.";
        @ConfigPath(path = "RENAME_COMMAND.RENAMED")
        public static String RENAMED = "&eClan %oldName% renamed to %name% by %playerName%.";
    }

    public static class TOP_COMMAND {

        public static class PREFIX {
            @ConfigPath(path = "TOP_COMMAND.PREFIX.OWN")
            public static String OWN = "<green>";
            @ConfigPath(path = "TOP_COMMAND.PREFIX.ENEMY")
            public static String ENEMY = "<red>";
            @ConfigPath(path = "TOP_COMMAND.PREFIX.ENEMY_ONLINE")
            public static String ENEMY_ONLINE = "<yellow>";
        }

        @ConfigPath(path = "TOP_COMMAND.NO_CLANS")
        public static String NO_CLANS = "&cThere are no clans yet.";
        @ConfigPath(path = "TOP_COMMAND.PAGE_NOT_FOUND")
        public static String PAGE_NOT_FOUND = "&cPage not found.";
        @ConfigPath(path = "TOP_COMMAND.CLAN_FORMAT")
        public static String CLAN_FORMAT = "<gray>%position%. %prefix%<click:run_command:/clan info %id%><hover:show_text:\"<yellow>Click here to view info of clan %prefix%%name%\">%name%</hover> <yellow>- <gray>%points%";
        @ConfigPath(path = "TOP_COMMAND.LISTED")
        public static List<String> LISTED = Arrays.asList(
                "",
                "&6Clan Top List &7(Page %currentPage%/%maxPage%)",
                "%clans%",
                "&7You are currently on &rPage %currentPage%/%maxPage%&7.",
                "&7To view other pages, use &e/%label% top <page#>",
                ""
        );
    }

    public static class UNINVITE_COMMAND {

        @ConfigPath(path = "UNINVITE_COMMAND.USAGE")
        public static String USAGE = "&cUsage: /%label% uninvite <playerName>";
        @ConfigPath(path = "UNINVITE_COMMAND.NOT_INVITED")
        public static String NOT_INVITED = "&ePlayer %targetName% is not invited to clan.";
        @ConfigPath(path = "UNINVITE_COMMAND.UNINVITED")
        public static String UNINVITED = "&ePlayer %targetName% has been uninvited from clan.";
    }

    public static class WITHDRAW_COMMAND {

        @ConfigPath(path = "WITHDRAW_COMMAND.USAGE")
        public static String USAGE = "&cUsage: /%label% withdraw <amount>";
        @ConfigPath(path = "WITHDRAW_COMMAND.CAN_NOT_WITHDRAW_ZERO")
        public static String CAN_NOT_WITHDRAW_ZERO = "&eYour cannot withdraw 0 or less.";
        @ConfigPath(path = "WITHDRAW_COMMAND.BALANCE_ZERO")
        public static String BALANCE_ZERO = "&eYour clan doesn't have any money.";
        @ConfigPath(path = "WITHDRAW_COMMAND.NOT_ENOUGH_MONEY")
        public static String NOT_ENOUGH_MONEY = "&eYour clan only has $%amount%.";
        @ConfigPath(path = "WITHDRAW_COMMAND.WITHDRAWN")
        public static String WITHDRAWN = "&e%playerName% withdrawn $%amount% from clan's balance.";
    }

    public static class POINTS_COMMAND {

        @ConfigPath(path = "POINTS_COMMAND.USAGE")
        public static String USAGE = "&cUsage: /%label% points <add|set|remove> <name> <amount>";
        @ConfigPath(path = "POINTS_COMMAND.INVALID_NUMBER")
        public static String INVALID_NUMBER = "&cInvalid number.";
        @ConfigPath(path = "POINTS_COMMAND.TOO_LESS")
        public static String TOO_LESS = "&cToo less amount.";
        @ConfigPath(path = "POINTS_COMMAND.TOO_MUCH")
        public static String TOO_MUCH = "&cToo much amount.";

        public static class ADD {
            @ConfigPath(path = "POINTS_COMMAND.ADD.USAGE")
            public static String USAGE = "&cUsage: /%label% points add <name> <amount>";
            @ConfigPath(path = "POINTS_COMMAND.ADD.ADDED")
            public static String ADDED = "&eAdded &a%amount% &epoint(s) to clan &a%name%&e.";
        }

        public static class SET {
            @ConfigPath(path = "POINTS_COMMAND.SET.USAGE")
            public static String USAGE = "&cUsage: /%label% points set <name> <amount>";
            @ConfigPath(path = "POINTS_COMMAND.SET.SETTED")
            public static String SETTED = "&eSet &a%amount% &epoint(s) to clan &a%name%&e.";
        }

        public static class REMOVE {
            @ConfigPath(path = "POINTS_COMMAND.REMOVE.USAGE")
            public static String USAGE = "&cUsage: /%label% points remove <name> <amount>";
            @ConfigPath(path = "POINTS_COMMAND.REMOVE.REMOVED")
            public static String REMOVED = "&eRemoved &a%amount% &epoint(s) from clan &a%name%&e.";
        }
    }
}

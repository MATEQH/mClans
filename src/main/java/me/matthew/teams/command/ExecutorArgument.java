package me.matthew.teams.command;

import lombok.Getter;
import me.matthew.teams.handler.Role;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class ExecutorArgument {

    @Getter
    private final String name;
    @Getter
    protected String description, permission;
    private String[] aliases;

    public ExecutorArgument(String name) {
        this.name = name;
    }

    public ExecutorArgument(String name, String permission) {
        this.name = name;
        this.permission = permission;
    }

    public ExecutorArgument(String name, String[] aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    public ExecutorArgument(String name, String permission, String[] aliases) {
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    public ExecutorArgument(String name, String description, String permission, String[] aliases) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.aliases = aliases;
    }

    public final String[] getAliases() {
        return aliases == null ? ArrayUtils.EMPTY_STRING_ARRAY : Arrays.copyOf(aliases, aliases.length);
    }

    public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);

    public abstract List<Role> getRoles();

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}

package me.matthew.teams.command;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import me.matthew.teams.util.BukkitUtil;
import me.matthew.teams.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArgumentExecutor implements CommandExecutor, TabCompleter {

    private List<ExecutorArgument> arguments = new ArrayList<>();
    @Getter
    private String label;
    @Getter
    @Setter
    private String description, permission;

    public ArgumentExecutor(String label) {
        this.label = label;
    }

    public ArgumentExecutor(String label, String permission) {
        this.label = label;
        this.permission = permission;
    }

    public ArgumentExecutor(String label, String description, String permission) {
        this.label = label;
        this.description = description;
        this.permission = permission;
    }

    public ArgumentExecutor(List<ExecutorArgument> arguments, String label, String description, String permission) {
        this.arguments = arguments;
        this.label = label;
        this.description = description;
        this.permission = permission;
    }

    public boolean containsArgument(ExecutorArgument argument) {
        return arguments.contains(argument);
    }

    public void addArgument(ExecutorArgument argument) {
        arguments.add(argument);
    }

    public void removeArgument(ExecutorArgument argument) {
        arguments.remove(argument);
    }

    public ExecutorArgument getArgument(String id) {
        return arguments.stream().filter(argument -> argument.getName().equalsIgnoreCase(id) || Arrays.asList(argument.getAliases()).contains(id.toLowerCase())).findFirst().orElse(null);
    }

    public List<ExecutorArgument> getArguments() {
        return ImmutableList.copyOf(arguments);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (permission != null && !sender.hasPermission(permission)) {
            sender.sendMessage(Messages.get(Messages.NO_PERMISSION));
            return true;
        }
        ExecutorArgument argument = getArgument(args[0]);
        if (argument == null) {
            return true;
        }
        if (argument.getPermission() != null && !sender.hasPermission(argument.getPermission())) {
            sender.sendMessage(Messages.get(Messages.NO_PERMISSION));
            return true;
        }
        argument.onCommand(sender, command, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> results = new ArrayList<>();
        if (args.length < 2) {
            for (ExecutorArgument argument : arguments.stream().filter(argument -> argument.getPermission() == null || sender.hasPermission(argument.getPermission())).collect(Collectors.toList())) {
                results.add(argument.getName());
            }
        } else {
            ExecutorArgument argument = getArgument(args[0]);
            if (argument == null) {
                return results;
            }
            if (argument.getPermission() == null || sender.hasPermission(argument.getPermission())) {
                results = argument.onTabComplete(sender, command, label, args);
                if (results == null) {
                    return null;
                }
            }
        }
        return BukkitUtil.getCompletions(args, results);
    }

    public void register() {
        Bukkit.getPluginCommand(label).setExecutor(this);
        Bukkit.getPluginCommand(label).setTabCompleter(this);
    }
}

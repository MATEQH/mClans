package me.matthew.teams.command.implement.team.argument.admin;

import me.matthew.teams.command.ExecutorArgument;
import me.matthew.teams.handler.enums.Role;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ForceDisbandArgument extends ExecutorArgument {

    public ForceDisbandArgument(String name) {
        super(name, "mclans.admin");
    }

    @Override
    public List<Role> getRoles() {
        return Arrays.asList(Role.LEADER);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        return true;
    }
}

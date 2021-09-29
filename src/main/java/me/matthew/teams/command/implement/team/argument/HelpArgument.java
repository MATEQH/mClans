package me.matthew.teams.command.implement.team.argument;

import me.matthew.teams.command.ExecutorArgument;
import me.matthew.teams.handler.enums.Role;
import me.matthew.teams.handler.enums.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class HelpArgument extends ExecutorArgument {

    public HelpArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        Messages.getList(Messages.HELP_COMMAND).forEach(message -> player.sendMessage(message.replace("%label%", label)));
        return true;
    }
}

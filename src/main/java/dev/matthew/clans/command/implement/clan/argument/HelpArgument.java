package dev.matthew.clans.command.implement.clan.argument;

import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.enums.Role;
import dev.matthew.clans.file.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpArgument extends ExecutorArgument {

    public HelpArgument(String name) {
        super(name, "mclans.use");
    }

    @Override
    public List<Role> getRoles() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Message.HELP_COMMAND.forEach(message -> Message.send(sender, message.replaceAll("%label%", label)));
        if (sender.hasPermission("mclans.staff")) {
            Message.STAFF_COMMAND.forEach(message -> Message.send(sender, message.replaceAll("%label%", label)));
        }
        return true;
    }
}

package com.github.mateqh.clans.command.implement.clan.argument;

import com.github.mateqh.clans.command.ExecutorArgument;
import com.github.mateqh.clans.handler.enums.Role;
import com.github.mateqh.clans.handler.file.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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
        Message.HELP_COMMAND.forEach(message -> Message.send(sender, message.replaceAll("%label%", label)));
        return true;
    }
}

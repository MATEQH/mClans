package dev.matthew.clans.command.implement.clan;

import dev.matthew.clans.Clans;
import dev.matthew.clans.command.ArgumentExecutor;
import dev.matthew.clans.command.ExecutorArgument;
import dev.matthew.clans.command.implement.clan.argument.*;
import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.command.implement.clan.argument.staff.ForceDisbandArgument;
import dev.matthew.clans.file.Message;
import dev.matthew.clans.util.BukkitUtil;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.util.hook.VaultHook;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ClanExecutor extends ArgumentExecutor {

    private final ExecutorArgument helpArgument;

    public ClanExecutor(String label) {
        super(label, "mclans.use");
        addArgument(new ForceDisbandArgument("forcedisband"));
        addArgument(new CaptainsArgument("captains"));
        addArgument(new CreateArgument("create"));
        addArgument(new DisbandArgument("disband"));
        addArgument(helpArgument = new HelpArgument("help"));
        addArgument(new InfoArgument("info"));
        addArgument(new InviteArgument("invite"));
        addArgument(new InvitesArgument("invites"));
        addArgument(new JoinArgument("join"));
        addArgument(new KickArgument("kick"));
        addArgument(new LeaderArgument("leader"));
        addArgument(new LeaveArgument("leave"));
        addArgument(new ListArgument("list"));
        addArgument(new RenameArgument("rename"));
        addArgument(new TopArgument("top"));
        addArgument(new UninviteArgument("uninvite"));
        if (VaultHook.ECONOMY == null) {
            Bukkit.getLogger().warning("[" + Clans.getInstance().getName() + "] " + Message.VAULT_NOT_FOUND);
            return;
        }
        addArgument(new DepositArgument("deposit"));
        addArgument(new WithdrawArgument("withdraw"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(getPermission())) {
            Message.send(sender, Message.NO_PERMISSION);
            return true;
        }
        if (args.length < 1) {
            helpArgument.onCommand(sender, command, label, args);
            return true;
        }
        Optional<ExecutorArgument> argument = getArgument(args[0]);
        if (!argument.isPresent()) {
            Message.send(sender, Message.INVALID_ARGUMENTS);
            return true;
        }
        if (argument.get().getPermission() != null && !sender.hasPermission(argument.get().getPermission())) {
            Message.send(sender, Message.NO_PERMISSION);
            return true;
        }
        argument.get().onCommand(sender, command, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return Arrays.asList("Can", "only", "be", "used", "as", "player");
        }
        List<String> results = new ArrayList<>();
        if (args.length < 2) {
            for (ExecutorArgument argument : getArguments()) {
                if (argument.getPermission() == null || sender.hasPermission(argument.getPermission())) {
                    Player player = (Player) sender;
                    Clan clan = ClanHandler.getByPlayer(player);
                    if (clan == null && (argument.getRoles() == null || argument.getRoles().isEmpty())) {
                        addResult(argument, results);
                    } else if (clan != null && (argument.getRoles() != null && (argument.getRoles().contains(clan.getRole(player)) || argument.getRoles().isEmpty()))) {
                        addResult(argument, results);
                    }
                }
            }
        } else {
            Optional<ExecutorArgument> argument = getArgument(args[0]);
            if (!argument.isPresent()) {
                return results;
            }
            if (argument.get().getPermission() == null || sender.hasPermission(argument.get().getPermission())) {
                results = argument.get().onTabComplete(sender, command, label, args);
                if (results == null) {
                    return null;
                }
            }
        }
        return BukkitUtil.getCompletions(args, results);
    }

    private void addResult(ExecutorArgument argument, List<String> stringList) {
        stringList.add(argument.getName());
    }
}

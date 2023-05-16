package dev.matthew.clans.listener;

import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.clan.ClanHandler;
import dev.matthew.clans.event.implement.HitTeammateEvent;
import dev.matthew.clans.file.Config;
import dev.matthew.clans.file.Message;
import dev.matthew.clans.util.StringUtil;
import dev.matthew.clans.util.hook.DuelsHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
//        if (event.getEntity().getKiller() == null) {
//            return;
//        }
        Player entity = event.getEntity(), killer = event.getEntity().getKiller();
        if (entity.equals(killer)) {
            return;
        }
        Clan killerClan = ClanHandler.getByPlayer(killer), entityClan = ClanHandler.getByPlayer(entity);
        if (killerClan != null && !killerClan.equals(entityClan)) {
            killerClan.setKills(killerClan.getKills() + 1);
            killerClan.setPoints(killerClan.getPoints() + 1);
            ClanHandler.save(killerClan);
        }
        if (entityClan != null && !entityClan.equals(killerClan)) {
            entityClan.setPoints(Math.max(entityClan.getPoints() - 1, 0));
            ClanHandler.save(entityClan);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player target = (Player) event.getEntity();
        Clan targetClan = ClanHandler.getByPlayer(target);
        if (targetClan == null) {
            return;
        }
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Clan damagerClan = ClanHandler.getByPlayer(damager);
            if (!targetClan.equals(damagerClan)) {
                return;
            }
            if (targetClan.isTeamFire()) {
                return;
            }
            if (DuelsHook.DUELS != null && DuelsHook.DUELS.getArenaManager().isInMatch(damager)) {
                return;
            }
            HitTeammateEvent hitTeammateEvent = new HitTeammateEvent(damager, target, damagerClan, targetClan);
            Bukkit.getPluginManager().callEvent(hitTeammateEvent);
            if (hitTeammateEvent.isAllowHit()) {
                return;
            }
            event.setCancelled(true);
        } else if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (!(projectile.getShooter() instanceof Player)) {
                return;
            }
            Player damager = (Player) projectile.getShooter();
            Clan damagerClan = ClanHandler.getByPlayer(damager);
            if (!targetClan.equals(damagerClan)) {
                return;
            }
            if (targetClan.isTeamFire()) {
                return;
            }
            if (DuelsHook.DUELS != null && DuelsHook.DUELS.getArenaManager().isInMatch(damager)) {
                return;
            }
            HitTeammateEvent hitTeammateEvent = new HitTeammateEvent(damager, target, damagerClan, targetClan);
            Bukkit.getPluginManager().callEvent(hitTeammateEvent);
            if (hitTeammateEvent.isAllowHit()) {
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        Clan clan = ClanHandler.getByPlayer(player);
        String prefix = StringUtil.translate(Config.CLAN.CHAT.PREFIX_NO_CLAN);
        if (clan != null) {
            String message = event.getMessage();
            if (message.startsWith("@") && message.length() > 1) {
                message = message.substring(1);
                event.setCancelled(true);
                String format = Config.CLAN.CHAT.CLAN_CHAT.FORMAT
                        .replaceAll("%name%", clan.getName())
                        .replaceAll("%playerName%", player.getDisplayName());
                clan.sendMessage(Config.CLAN.CHAT.CLAN_CHAT.COLORED_MESSAGE ?
                        StringUtil.translate(format.replaceAll("%message%", message)) :
                        StringUtil.translate(format).replaceAll("%message%", message));
                return;
            }
            prefix = StringUtil.translate(Config.CLAN.CHAT.PREFIX.replaceAll("%name%", clan.getName()));
        }
        event.setFormat(prefix + event.getFormat());
    }

//    @EventHandler
//    public void onPlayerJoin(PlayerJoinEvent event) {
//        Player player = event.getPlayer();
//        if (Clans.getInstance().getNameCache().ensure(player.getName())) {
//            return;
//        }
//        Clans.getInstance().getNameCache().update(player.getName());
//    }
}

package me.matthew.teams.handler;

import lombok.Getter;
import me.matthew.teams.util.Config;
import me.matthew.teams.util.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerListener implements Listener {

    @Getter
    private static List<UUID> damageList = new ArrayList<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) {
            return;
        }
        Player entity = event.getEntity(), killer = event.getEntity().getKiller();
        if (entity.equals(killer)) {
            return;
        }
        Team killerTeam = TeamHandler.getByPlayer(killer), entityTeam = TeamHandler.getByPlayer(entity);
        if (killerTeam != null) {
            if (entityTeam == null || (entityTeam != null && !entityTeam.equals(killerTeam))) {
                killerTeam.setKills(killerTeam.getKills() + 1);
                killerTeam.setPoints(killerTeam.getPoints() + 1);
                TeamHandler.save(killerTeam);
            }
        }
        if (entityTeam != null) {
            entityTeam.setPoints(entityTeam.getPoints() - 1  < 0 ? 0 : entityTeam.getPoints() - 1);
            TeamHandler.save(entityTeam);
        }
        TeamHandler.sortTeams();
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player target = (Player) event.getEntity();
        Team targetTeam = TeamHandler.getByPlayer(target);
        if (targetTeam == null) {
            return;
        }
        if (event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Team damagerTeam = TeamHandler.getByPlayer(damager);
            if (damagerTeam == null || !targetTeam.equals(damagerTeam)) {
                return;
            }
            event.setCancelled(true);
        } else if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (!(projectile.getShooter() instanceof Player)) {
                return;
            }
            Player damager = (Player) projectile.getShooter();
            Team damagerTeam = TeamHandler.getByPlayer(damager);
            if (damagerTeam == null || !targetTeam.equals(damagerTeam)) {
                return;
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Team team = TeamHandler.getByPlayer(player);
        String prefix = team != null ? StringUtil.translate(Config.getString(Config.CHAT_PREFIX).replace("%teamName%", team.getName())) : "";
        event.setFormat(prefix + event.getFormat());
    }
}

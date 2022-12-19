package dev.matthew.clans.event.implement;

import dev.matthew.clans.clan.Clan;
import dev.matthew.clans.event.CustomEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class HitTeammateEvent extends CustomEvent {

    @Getter
    private final Player attacker, attacked;
    @Getter
    private final Clan attackerClan, attackedClan;
    @Getter
    @Setter
    private boolean allowHit = false;

    public HitTeammateEvent(Player attacker, Player attacked, Clan attackerClan, Clan attackedClan) {
        this.attacker = attacker;
        this.attacked = attacked;
        this.attackerClan = attackerClan;
        this.attackedClan = attackedClan;
    }

    public HitTeammateEvent(Player attacker, Player attacked, Clan attackerClan, Clan attackedClan, boolean allowHit) {
        this.attacker = attacker;
        this.attacked = attacked;
        this.attackerClan = attackerClan;
        this.attackedClan = attackedClan;
        this.allowHit = allowHit;
    }
}

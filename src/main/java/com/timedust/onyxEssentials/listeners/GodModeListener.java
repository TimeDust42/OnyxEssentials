package com.timedust.onyxEssentials.listeners;

import com.timedust.onyxEssentials.managers.GodModeManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class GodModeListener implements Listener {

    private final GodModeManager godModeManager;

    public GodModeListener(GodModeManager godModeManager) {
        this.godModeManager = godModeManager;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {
            if (godModeManager.isGod(player.getUniqueId())) {
                if (e.getCause() != EntityDamageEvent.DamageCause.VOID) {
                    e.setCancelled(true);
                }
            }
        }
    }

}

package com.timedust.onyxEssentials.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class GodModeManager {

    private final Set<UUID> godModePlayers = new HashSet<>();

    public boolean toggleGodMode(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (godModePlayers.contains(uuid)) {
            if (player != null) {
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                player.removePotionEffect(PotionEffectType.WATER_BREATHING);
            }
            godModePlayers.remove(uuid);
            return false;
        } else {
            if (player != null) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, -1, 0, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, -1, 0, false, false));
            }
            godModePlayers.add(uuid);
            return true;
        }
    }

    public boolean isGod(UUID uuid) {
        return godModePlayers.contains(uuid);
    }

    public void removeOnQuit(UUID uuid) {
        godModePlayers.remove(uuid);
    }
}

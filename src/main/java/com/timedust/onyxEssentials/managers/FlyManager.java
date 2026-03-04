package com.timedust.onyxEssentials.managers;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FlyManager {

    private final Set<UUID> flyEnabled = new HashSet<>();

    public void setFly(Player player, boolean enable) {
        UUID uuid = player.getUniqueId();

        if (enable) {
            flyEnabled.add(uuid);
        } else  {
            flyEnabled.remove(uuid);
        }

        player.setAllowFlight(enable);
        if (!enable) {
            player.setFlying(false);
        }
    }

    public boolean toggleFly(Player player) {
        boolean newState = !flyEnabled.contains(player.getUniqueId());
        setFly(player, newState);
        return newState;
    }

    public boolean hasFly(UUID uuid) {
        return flyEnabled.contains(uuid);
    }
}

package com.timedust.onyxEssentials.managers;

import java.util.*;

public class GodModeManager {

    private final Set<UUID> godModePlayers = new HashSet<>();

    public boolean toggleGodMode(UUID uuid) {
        if (godModePlayers.contains(uuid)) {
            godModePlayers.remove(uuid);
            return false;
        } else {
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

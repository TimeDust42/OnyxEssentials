package com.timedust.onyxEssentials.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownsManager {

    private final Map<String, Map<UUID, Long>> cooldowns = new HashMap<>();

    public void setCooldown(String action, UUID uuid, int seconds) {
        cooldowns.computeIfAbsent(action, key -> new HashMap<>())
                .put(uuid, System.currentTimeMillis() + (seconds * 1000L));
    }

    public long getRemainingTime(String action, UUID uuid) {
        Map<UUID, Long> actionMap = cooldowns.get(action);
        if (actionMap == null) return 0;

        long endTime = actionMap.getOrDefault(uuid, 0L);
        return Math.max(0, (endTime - System.currentTimeMillis()) / 1000);
    }

    public boolean isExpired(String action, UUID uuid) {
        return getRemainingTime(action, uuid) <= 0;
    }
}

package com.timedust.onyxEssentials.utils;

import org.bukkit.GameMode;

public class PlayerUtils {

    /**
     *  Преобразует строку в GameMode
     * @param input аргумент для команды
     * @return GameMode или null, если режим не распознан
     */
    public static GameMode parseGameMode(String input) {
        if (input == null) return GameMode.SURVIVAL;

        return switch (input.toLowerCase()) {
            case "survival", "0", "s"  -> GameMode.SURVIVAL;
            case "creative", "1", "c"  -> GameMode.CREATIVE;
            case "adventure", "2", "a" -> GameMode.ADVENTURE;
            case "spectator", "3", "sp" -> GameMode.SPECTATOR;
            default -> GameMode.SURVIVAL;
        };
    }
}

package com.timedust.onyxEssentials.tasks;

import com.timedust.onyxEssentials.managers.FlyManager;
import com.timedust.onyxEssentials.managers.GodModeManager;
import com.timedust.onyxEssentials.managers.VanishManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ActionBarTask extends BukkitRunnable {

    private final VanishManager vanishManager;
    private final GodModeManager  godManager;
    private final FlyManager flyManager;

    public ActionBarTask(VanishManager v, GodModeManager g, FlyManager f) {
        this.vanishManager = v;
        this.godManager = g;
        this.flyManager = f;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            List<Component> statuses = new ArrayList<>();
            UUID uuid = player.getUniqueId();

            if (vanishManager.hasVanish(uuid)) {
                statuses.add(Component.text("VANISH", NamedTextColor.GRAY, TextDecoration.BOLD));
            }
            if (godManager.isGod(uuid)) {
                statuses.add(Component.text("GOD", NamedTextColor.GOLD, TextDecoration.BOLD));
            }
            if (flyManager.hasFly(uuid)) {
                statuses.add(Component.text("FLY", NamedTextColor.AQUA, TextDecoration.BOLD));
            }

            if (!statuses.isEmpty()) {
                Component finalBar = Component.empty();
                finalBar = finalBar.append(Component.text("[ ", NamedTextColor.DARK_GRAY));
                for (int i = 0; i < statuses.size(); i++) {
                    finalBar = finalBar.append(statuses.get(i));
                    if (i != statuses.size() - 1) {
                        finalBar = finalBar.append(Component.text(" | ", NamedTextColor.DARK_GRAY));
                    }
                }
                finalBar = finalBar.append(Component.text(" ]", NamedTextColor.DARK_GRAY));
                player.sendActionBar(finalBar);
            }
        }
    }
}

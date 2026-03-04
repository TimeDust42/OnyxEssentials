package com.timedust.onyxEssentials.tasks;

import com.timedust.onyxEssentials.managers.VanishManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class VanishActionBarTask extends BukkitRunnable {

    private final VanishManager vanishManager;

    public VanishActionBarTask(VanishManager vanishManager) {
        this.vanishManager = vanishManager;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (vanishManager.hasVanish(player.getUniqueId())) {
                player.sendActionBar(Component.text("● Вы в режиме Ваниша ●", NamedTextColor.GRAY));
            }
        }
    }
}

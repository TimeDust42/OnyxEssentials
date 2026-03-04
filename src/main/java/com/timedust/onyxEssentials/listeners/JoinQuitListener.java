package com.timedust.onyxEssentials.listeners;

import com.timedust.onyxEssentials.configuration.ConfigManager;
import com.timedust.onyxEssentials.managers.FlyManager;
import com.timedust.onyxEssentials.managers.GodModeManager;
import com.timedust.onyxEssentials.managers.VanishManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinQuitListener implements Listener {

    private final JavaPlugin plugin;
    private final ConfigManager config;
    private final FlyManager flyManager;
    private final GodModeManager godmode;
    private final VanishManager vanishManager;

    public JoinQuitListener(JavaPlugin plugin, ConfigManager config, FlyManager flyManager, GodModeManager godmode, VanishManager vanishManager) {
        this.plugin = plugin;
        this.config = config;
        this.flyManager = flyManager;
        this.godmode = godmode;
        this.vanishManager = vanishManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player joined = event.getPlayer();

        event.joinMessage(config.getJoinMessage(joined.getName()));

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (vanishManager.hasVanish(onlinePlayer.getUniqueId())) {
                joined.hidePlayer(plugin, onlinePlayer);
            }
        }

        if (flyManager.hasFly(joined.getUniqueId())) {
            joined.setAllowFlight(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.quitMessage(config.getQuitMessage(event.getPlayer().getName()));

        vanishManager.setVanish(event.getPlayer(), false);

        godmode.removeOnQuit(event.getPlayer().getUniqueId());
    }
}

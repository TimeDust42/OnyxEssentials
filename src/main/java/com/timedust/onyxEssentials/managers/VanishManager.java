package com.timedust.onyxEssentials.managers;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VanishManager {

    private final JavaPlugin plugin;

    private final Set<UUID> vanishEnabled = new HashSet<>();

    public VanishManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setVanish(Player player, boolean enable) {
        UUID uuid = player.getUniqueId();

        if (enable) {
            vanishEnabled.add(uuid);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.equals(player)) {
                    onlinePlayer.hidePlayer(plugin, player);
                }
            }

            player.playerListName(MiniMessage.miniMessage()
                    .deserialize("<gray>[V] <player>", Placeholder.parsed("player", player.getName())));

            player.setInvulnerable(true);
        } else {
            vanishEnabled.remove(uuid);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(plugin, player);
            }

            player.playerListName(null);

            player.setInvisible(false);
        }
    }

    public boolean toggleVanish(Player player) {
        boolean newState = !vanishEnabled.contains(player.getUniqueId());
        setVanish(player, newState);
        return newState;
    }

    public boolean hasVanish(UUID uuid) {
        return vanishEnabled.contains(uuid);
    }

}

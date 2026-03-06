package com.timedust.onyxEssentials.commands.supportCommands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NearCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can execute this command!",  NamedTextColor.RED));
            return true;
        }
        if (!player.hasPermission("onyxEssentials.near")) {
            player.sendMessage(Component.text("You do not have permission to execute this command!",  NamedTextColor.RED));
            return true;
        }
        if (args.length == 0) {
            double radius = 250.0;
            List<Player> nearby = player.getNearbyEntities(radius, radius, radius)
                    .stream()
                    .filter(entity -> entity instanceof Player)
                    .map(entity -> (Player) entity)
                    .toList();
            if (nearby.isEmpty()) {
                player.sendMessage(Component.text("Рядом с вами нет игроков",  NamedTextColor.RED));
                return true;
            }
            for (Player p : nearby) {
                var coordinate = MiniMessage.miniMessage().deserialize("x:" + p.getX() + " y:" + p.getY() + " z:" + p.getZ());
                player.sendMessage(Component.text("Найден игрок на координатах " + coordinate,  NamedTextColor.GREEN));
            }
            return true;
        }

        sender.sendMessage(Component.text("Usage: /near", NamedTextColor.RED));
        return true;
    }
}

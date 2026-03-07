package com.timedust.onyxEssentials.commands.supportCommands;

import com.timedust.onyxEssentials.utils.teleport.TeleportRequest;
import com.timedust.onyxEssentials.utils.teleport.TeleportUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TeleportCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            if (!(commandSender instanceof Player player)) {
                commandSender.sendMessage("Only for players!");
                return true;
            }
            if (!player.hasPermission("onyx-essentials.tp")) {
                player.sendMessage("You don't have permission!");
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);

            if (target == null) {
                player.sendMessage(Component.text("Игрок не найден", NamedTextColor.RED));
                return true;
            }

            TeleportRequest request = new TeleportRequest(player.getUniqueId(), target.getUniqueId(), TeleportRequest.RequestType.NORMAL);
            TeleportUtils.teleport(request);

        }

        if (args.length == 2) {
            if (!commandSender.hasPermission("tp.other")) {
                commandSender.sendMessage("You don't have permission!");
                return true;
            }

            Player sender = Bukkit.getPlayerExact(args[0]);
            if (sender == null) {
                commandSender.sendMessage("sender not found");
                return true;
            }
            Player target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                commandSender.sendMessage("target not found");
                return true;
            }

            TeleportRequest request = new TeleportRequest(sender.getUniqueId(), target.getUniqueId(), TeleportRequest.RequestType.NORMAL);
            TeleportUtils.teleport(request);
            commandSender.sendMessage("Teleport successful");
            return true;
        }

        if (args.length > 2) {
            commandSender.sendMessage("Usage: /tp <sender> [target]");
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            List<String> suggestions = new ArrayList<>();
            if (commandSender.hasPermission("onyx-essentials.tp")) {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (p.getName().toLowerCase().startsWith(input)) suggestions.add(p.getName());
                });
            }
            return suggestions;
        }
        if (args.length == 2) {
            String input = args[1].toLowerCase();
            List<String> suggestions = new ArrayList<>();
            if (commandSender.hasPermission("onyx-essentials.tp.other")) {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (p.getName().toLowerCase().startsWith(input)) suggestions.add(p.getName());
                });
            }
            return suggestions;
        }
        return List.of();
    }
}

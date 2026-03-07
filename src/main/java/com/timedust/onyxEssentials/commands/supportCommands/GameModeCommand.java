package com.timedust.onyxEssentials.commands.supportCommands;

import com.timedust.onyxEssentials.utils.PlayerUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GameModeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /gamemode <gamemode>",  NamedTextColor.RED));
            return true;
        }
        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("You must be a player to use this command.", NamedTextColor.RED));
                return true;
            }

            if (!sender.hasPermission("onyx-essentials.gamemode")) {
                sender.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
                return true;
            }

            GameMode mode = PlayerUtils.parseGameMode(args[0]);

            if (mode == null) {
                sender.sendMessage(Component.text("Invalid gamemode.", NamedTextColor.RED));
                return true;
            }

            player.setGameMode(mode);
            player.sendMessage(Component.text("Ваш режим игры установлен на " + mode.name(), NamedTextColor.GREEN));
            return true;
        }
        if (args.length == 2) {
            if (!sender.hasPermission("onyx-essentials.gamemode.other")) {
                sender.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
                return true;
            }

            GameMode mode = PlayerUtils.parseGameMode(args[0]);
            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                sender.sendMessage(Component.text("Player not found.", NamedTextColor.RED));
                return true;
            }

            target.setGameMode(mode);
            target.sendMessage(Component.text("Ваш режим игры установлен на " + mode.name(), NamedTextColor.GREEN));
            return true;
        }

        sender.sendMessage(Component.text("Usage: /gamemode <gamemode>", NamedTextColor.RED));
        return true;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            if (sender.hasPermission("onyx-essentials.gamemode")) {
                return List.of("0", "1", "2", "3",
                        "survival", "creative", "adventure", "spectator",
                        "s", "c", "a", "sp");
            }
        }
        if (args.length == 2) {
            String input = args[1].toLowerCase();
            List<String> suggestions = new ArrayList<>();
            if (sender.hasPermission("onyx-essentials.gamemode.other")) {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (p.getName().toLowerCase().startsWith(input)) suggestions.add(p.getName());
                });
            }
            return suggestions;
        }
        return List.of();
    }
}

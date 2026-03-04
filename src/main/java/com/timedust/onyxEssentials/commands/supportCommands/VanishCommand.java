package com.timedust.onyxEssentials.commands.supportCommands;

import com.timedust.onyxEssentials.managers.VanishManager;
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

public class VanishCommand implements CommandExecutor, TabCompleter {

    private final VanishManager vanishManager;

    public VanishCommand(VanishManager vanishManager) {
        this.vanishManager = vanishManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("Only players can execute!", NamedTextColor.RED));
                return true;
            }
            if (!player.hasPermission("onyx-essentials.vanish")) {
                player.sendMessage(Component.text("No permission!", NamedTextColor.RED));
                return true;
            }

            boolean isVanish = vanishManager.toggleVanish(player);
            player.sendMessage(Component.text(isVanish ? "Ваниш включен" : "Ваниш выключен",
                    isVanish ? NamedTextColor.GREEN : NamedTextColor.RED));
            return true;
        }

        if (args.length == 1) {
            if (!sender.hasPermission("onyx-essentials.vanish.other")) {
                sender.sendMessage(Component.text("No permission!", NamedTextColor.RED));
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(Component.text("Player not found!", NamedTextColor.RED));
                return true;
            }

            boolean isVanish = vanishManager.toggleVanish(target);
            sender.sendMessage(Component.text("Ваниш для " + target.getName() + (isVanish ? " включен" : " выключен"),
                    isVanish ? NamedTextColor.GREEN : NamedTextColor.RED));
            return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            List<String> suggestions = new ArrayList<>();
            if (sender.hasPermission("onyx-essentials.vanish.other")) {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (p.getName().toLowerCase().startsWith(input)) suggestions.add(p.getName());
                });
            }
            return suggestions;
        }

        return List.of();
    }
}

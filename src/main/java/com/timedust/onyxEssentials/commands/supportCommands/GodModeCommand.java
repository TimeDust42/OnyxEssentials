package com.timedust.onyxEssentials.commands.supportCommands;

import com.timedust.onyxEssentials.managers.GodModeManager;
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

public class GodModeCommand implements CommandExecutor, TabCompleter {

    private final GodModeManager manager;

    public GodModeCommand(GodModeManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("Only players can execute this command!",  NamedTextColor.RED));
                return true;
            }

            if (!player.hasPermission("onyx-essentials.godmode")) {
                player.sendMessage(Component.text("You do not have permission to execute this command!",   NamedTextColor.RED));
                return true;
            }

            if (manager.isGod(player.getUniqueId())) {
                manager.toggleGodMode(player.getUniqueId());
                player.sendMessage(Component.text("Режим бога выключен", NamedTextColor.RED));
                return true;
            }
            manager.toggleGodMode(player.getUniqueId());
            player.sendMessage(Component.text("Режим бога включён", NamedTextColor.GREEN));
        }

        if (args.length == 1) {
            if (!sender.hasPermission("onyx-essentials.godmode.other")) {
                sender.sendMessage(Component.text("You do not have permission to execute this command!",   NamedTextColor.RED));
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(Component.text("Player not found!",  NamedTextColor.RED));
                return true;
            }

            if (manager.isGod(target.getUniqueId())) {
                manager.toggleGodMode(target.getUniqueId());
                sender.sendMessage(Component.text("Режим бога игроку " + target.getName() + " выключен", NamedTextColor.RED));
                return true;
            }
            manager.toggleGodMode(target.getUniqueId());
            target.sendMessage(Component.text("Режим бога игроку " + target.getName() + " включён",  NamedTextColor.GREEN));
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            List<String> suggestions = new ArrayList<>();
            if (sender.hasPermission("onyx-essentials.godmode.other")) {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (p.getName().toLowerCase().startsWith(input)) suggestions.add(p.getName());
                });
            }
            return suggestions;
        }

        return List.of();
    }
}

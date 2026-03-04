package com.timedust.onyxEssentials.commands.supportCommands;

import com.timedust.onyxEssentials.managers.FlyManager;
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

public class FlyCommand implements CommandExecutor, TabCompleter {

    private final FlyManager flyManager;

    public FlyCommand(FlyManager flyManager) {
        this.flyManager = flyManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("Only players can execute this command!",  NamedTextColor.RED));
                return true;
            }

            if (!player.hasPermission("onyx-essentials.fly")) {
                player.sendMessage(Component.text("You do not have permission to execute this command!",   NamedTextColor.RED));
                return true;
            }

            boolean enabled = flyManager.toggleFly(player);

            Component message = enabled ?
                    Component.text("Полёт включён",  NamedTextColor.GREEN) :
                    Component.text("Полёт выключен", NamedTextColor.RED);

            player.sendMessage(message);
            return true;
        }

        if (args.length == 1) {
            if (!sender.hasPermission("onyx-essentials.fly.other")) {
                sender.sendMessage(Component.text("You do not have permission to execute this command!",   NamedTextColor.RED));
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                sender.sendMessage(Component.text("Player not found!",  NamedTextColor.RED));
                return true;
            }

            boolean enabled = flyManager.toggleFly(target);

            Component message = enabled ?
                    Component.text("Полёт игроку " + target.getName() + " выключен", NamedTextColor.GREEN) :
                    Component.text("Полёт игроку " + target.getName() + " включён",  NamedTextColor.RED);

            sender.sendMessage(message);
            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            List<String> suggestions = new ArrayList<>();
            if (sender.hasPermission("onyx-essentials.fly.other")) {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (p.getName().toLowerCase().startsWith(input)) suggestions.add(p.getName());
                });
            }
            return suggestions;
        }

        return List.of();
    }
}

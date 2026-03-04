package com.timedust.onyxEssentials.commands.supportCommands;

import com.timedust.onyxEssentials.configuration.ConfigManager;
import com.timedust.onyxEssentials.managers.CooldownsManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HealCommand implements CommandExecutor, TabCompleter {

    private final ConfigManager configManager;
    private final CooldownsManager cooldownsManager;

    public HealCommand(ConfigManager configManager, CooldownsManager cooldownsManager) {
        this.configManager = configManager;
        this.cooldownsManager = cooldownsManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {

        // Исцеление себя
        if (args.length == 0) {
            if (!(sender instanceof Player player))
                return sendError(sender, "Консоль должна указывать игрока!");

            if (!player.hasPermission("onyx-essentials.heal.me"))
                return sendError(player, "У вас нет прав!");

            if (!cooldownsManager.isExpired("heal_me", player.getUniqueId()))
                return sendCooldownMsg(player, "heal_me");

            player.setHealth(20);
            player.sendMessage(Component.text("Вы были исцелены!", NamedTextColor.GREEN));
            cooldownsManager.setCooldown("heal_me", player.getUniqueId(), configManager.getCooldownHealMe());
            return true;
        }

        // Исцеление всех
        if (args.length == 1 && args[0].equalsIgnoreCase("all")) {
            if (!sender.hasPermission("onyx-essentials.heal.all"))
                return sendError(sender, "Нет прав на исцеление всех!");

            if (sender instanceof Player player && !cooldownsManager.isExpired("heal_all", player.getUniqueId()))
                return sendCooldownMsg(player, "heal_all");

            Bukkit.getOnlinePlayers().forEach(p -> {
                p.setHealth(20);
                p.sendMessage(Component.text("Вы были исцелены игроком " + sender.getName(), NamedTextColor.GREEN));
            });

            sender.sendMessage(Component.text("Все игроки исцелены.", NamedTextColor.GREEN));
            if (sender instanceof Player player)
                cooldownsManager.setCooldown("heal_all", player.getUniqueId(), configManager.getCooldownHealAll());
            return true;
        }

        // Исцеление другого игрока
        if (args.length == 1) {
            if (!sender.hasPermission("onyx-essentials.heal.other"))
                return sendError(sender, "Нет прав на исцеление других!");

            if (sender instanceof Player player && !cooldownsManager.isExpired("heal_other", player.getUniqueId()))
                return sendCooldownMsg(player, "heal_other");

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null)
                return sendError(sender, "Игрок " + args[0] + " не найден.");

            target.setHealth(20);
            target.sendMessage(Component.text("Вы были исцелены игроком " + sender.getName(), NamedTextColor.GREEN));
            sender.sendMessage(Component.text("Игрок " + target.getName() + " исцелен.", NamedTextColor.GREEN));

            if (sender instanceof Player player)
                cooldownsManager.setCooldown("heal_other", player.getUniqueId(), configManager.getCooldownHealOther());
            return true;
        }

        return sendError(sender, "Использование: /heal [игрок|all]");
    }

    private boolean sendError(CommandSender s, String msg) {
        s.sendMessage(Component.text(msg, NamedTextColor.RED));
        return true;
    }

    private boolean sendCooldownMsg(Player player, String key) {
        long seconds = cooldownsManager.getRemainingTime(key, player.getUniqueId());
        player.sendMessage(Component.text("Подождите " + seconds + " сек. перед повторным использованием!", NamedTextColor.RED));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            List<String> suggestions = new ArrayList<>();
            if (sender.hasPermission("onyx-essentials.heal.all") && "all".startsWith(input)) suggestions.add("all");

            if (sender.hasPermission("onyx-essentials.heal.other")) {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (p.getName().toLowerCase().startsWith(input)) suggestions.add(p.getName());
                });
            }
            return suggestions;
        }
        return List.of();
    }
}
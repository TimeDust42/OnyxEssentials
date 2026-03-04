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

public class FeedCommand implements CommandExecutor, TabCompleter {

    private final ConfigManager configManager;
    private final CooldownsManager cooldownsManager;

    public FeedCommand(ConfigManager configManager, CooldownsManager cooldownsManager) {
        this.configManager = configManager;
        this.cooldownsManager = cooldownsManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // Накормить себя
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(Component.text("Консоль должна указать игрока!", NamedTextColor.RED));
                return true;
            }
            if (!player.hasPermission("onyx-essentials.feed.me")) {
                return sendError(player, "У вас нет прав!");
            }
            if (!cooldownsManager.isExpired("feed_me", player.getUniqueId())) {
                return sendCooldownMsg(player, "feed_me");
            }

            feed(player);
            player.sendMessage(Component.text("Вы были накормлены!", NamedTextColor.GREEN));
            cooldownsManager.setCooldown("feed_me", player.getUniqueId(), configManager.getCooldownFeedMe());
            return true;
        }

        // Накормить всех
        if (args.length == 1 && args[0].equalsIgnoreCase("all")) {
            if (!sender.hasPermission("onyx-essentials.feed.all")) {
                return sendError(sender, "Нет прав на кормление всех!");
            }
            if (sender instanceof Player player && !cooldownsManager.isExpired("feed_all", player.getUniqueId())) {
                return sendCooldownMsg(player, "feed_all");
            }

            Bukkit.getOnlinePlayers().forEach(p -> {
                feed(p);
                p.sendMessage(Component.text("Вы были накормлены игроком " + sender.getName(), NamedTextColor.GREEN));
            });

            sender.sendMessage(Component.text("Все игроки на сервере накормлены.", NamedTextColor.GREEN));
            if (sender instanceof Player player) {
                cooldownsManager.setCooldown("feed_all", player.getUniqueId(), configManager.getCooldownFeedAll());
            }
            return true;
        }

        // Накормить другого игрока
        if (args.length == 1) {
            if (!sender.hasPermission("onyx-essentials.feed.other")) {
                return sendError(sender, "Нет прав на кормление других!");
            }
            if (sender instanceof Player player && !cooldownsManager.isExpired("feed_other", player.getUniqueId())) {
                return sendCooldownMsg(player, "feed_other");
            }

            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                return sendError(sender, "Игрок " + args[0] + " не найден.");
            }

            feed(target);
            target.sendMessage(Component.text("Вы были накормлены игроком " + sender.getName(), NamedTextColor.GREEN));
            sender.sendMessage(Component.text("Игрок " + target.getName() + " накормлен.", NamedTextColor.GREEN));

            if (sender instanceof Player player) {
                cooldownsManager.setCooldown("feed_other", player.getUniqueId(), configManager.getCooldownFeedOther());
            }
            return true;
        }

        sender.sendMessage(Component.text("Использование: /feed [игрок|all]", NamedTextColor.RED));
        return true;
    }

    private void feed(Player p) {
        p.setFoodLevel(20);
        p.setSaturation(20f);
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
            if (sender.hasPermission("onyx-essentials.feed.all") && "all".startsWith(input)) suggestions.add("all");
            if (sender.hasPermission("onyx-essentials.feed.other")) {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    if (p.getName().toLowerCase().startsWith(input)) suggestions.add(p.getName());
                });
            }
            return suggestions;
        }
        return List.of();
    }
}

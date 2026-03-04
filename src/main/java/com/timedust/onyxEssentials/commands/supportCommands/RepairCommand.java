package com.timedust.onyxEssentials.commands.supportCommands;

import com.timedust.onyxEssentials.configuration.ConfigManager;
import com.timedust.onyxEssentials.managers.CooldownsManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class RepairCommand implements CommandExecutor, TabCompleter {

    private final ConfigManager configManager;
    private final CooldownsManager cooldownsManager;

    public RepairCommand(ConfigManager configManager, CooldownsManager cooldownsManager) {
        this.configManager = configManager;
        this.cooldownsManager = cooldownsManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player))
            return sendError(sender, "Данная команда только для игроков!");

        // Починка предмета в руке
        if (args.length == 0) {
            if (!player.hasPermission("onyx-essentials.repair"))
                return sendError(player, "У вас недостаточно прав!");

            if (!cooldownsManager.isExpired("repair", player.getUniqueId()))
                return sendCooldownMsg(player, "repair");

            ItemStack item = player.getInventory().getItemInMainHand();
            if (item.getType().isAir())
                return sendError(player, "У вас нет предмета в руке!");

            if (!(item.getItemMeta() instanceof Damageable damageable))
                return sendError(player, "Этот предмет не имеет прочности!");

            damageable.setDamage(0);
            item.setItemMeta(damageable);

            player.sendMessage(Component.text("Предмет починен!", NamedTextColor.GREEN));
            cooldownsManager.setCooldown("repair", player.getUniqueId(), configManager.getCooldownRepairItem());
            return true;
        }

        // Починка брони
        if (args.length == 1 && args[0].equalsIgnoreCase("armor")) {
            if (!player.hasPermission("onyx-essentials.repair.armor"))
                return sendError(player, "У вас недостаточно прав!");

            if (!cooldownsManager.isExpired("repair_armor", player.getUniqueId()))
                return sendCooldownMsg(player, "repair_armor");

            repairItems(player.getInventory().getArmorContents());

            player.sendMessage(Component.text("Вся броня починена!", NamedTextColor.GREEN));
            cooldownsManager.setCooldown("repair_armor", player.getUniqueId(), configManager.getCooldownRepairArmor());
            return true;
        }

        // Починка всего инвентаря
        if (args.length == 1 && args[0].equalsIgnoreCase("all")) {
            if (!player.hasPermission("onyx-essentials.repair.all"))
                return sendError(player, "У вас недостаточно прав!");

            if (!cooldownsManager.isExpired("repair_all", player.getUniqueId()))
                return sendCooldownMsg(player, "repair_all");

            repairItems(player.getInventory().getContents());

            player.sendMessage(Component.text("Все предметы в инвентаре починены!", NamedTextColor.GREEN));
            cooldownsManager.setCooldown("repair_all", player.getUniqueId(), configManager.getCooldownRepairFull());
            return true;
        }

        return sendError(sender, "Использование: /repair [armor|all]");
    }

    private void repairItems(ItemStack[] contents) {
        Arrays.stream(contents)
                .filter(item -> item != null && item.hasItemMeta() && item.getItemMeta() instanceof Damageable)
                .forEach(item -> {
                    Damageable meta = (Damageable) item.getItemMeta();
                    meta.setDamage(0);
                    item.setItemMeta(meta);
                });
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
            return Stream.of("armor", "all")
                    .filter(s -> s.startsWith(args[0].toLowerCase()))
                    .toList();
        }
        return List.of();
    }
}
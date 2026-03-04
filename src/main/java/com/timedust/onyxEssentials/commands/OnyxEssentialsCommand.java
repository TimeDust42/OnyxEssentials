package com.timedust.onyxEssentials.commands;

import com.timedust.onyxEssentials.OnyxEssentials;
import com.timedust.onyxEssentials.commands.onyxSubCommands.JoinSubCommand;
import com.timedust.onyxEssentials.commands.onyxSubCommands.QuitSubCommand;
import com.timedust.onyxEssentials.commands.onyxSubCommands.ReloadSubCommand;
import com.timedust.onyxEssentials.configuration.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnyxEssentialsCommand implements CommandExecutor, TabCompleter {

    private final OnyxEssentials plugin;
    private final ConfigManager config;
    private final Map<String, SubCommand> subCommands = new HashMap<>();
    
    public OnyxEssentialsCommand(OnyxEssentials plugin, ConfigManager config) {
        this.plugin = plugin;
        this.config = config;

        SubCommand reloadCmd = new ReloadSubCommand(plugin);
        SubCommand joinMessageCmd = new JoinSubCommand(plugin, config);
        SubCommand quitMessageCmd = new QuitSubCommand(plugin, config);

        subCommands.put("reload", reloadCmd);
        subCommands.put("join-message", joinMessageCmd);
        subCommands.put("quit-message", quitMessageCmd);
    }
    
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("onyxessentials.admin")) {
                player.sendMessage(Component.text("You don't have permission to use this command!", NamedTextColor.RED));
                return true;
            }
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /" + label + " <subcommand>");
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());

        if (subCommand != null) {
            String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            subCommand.execute(sender, subArgs);
        } else {
            sender.sendMessage("Unknown subcommand: " + args[0]);
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return subCommands.keySet().stream().toList();
        }

        return List.of();
    }
}

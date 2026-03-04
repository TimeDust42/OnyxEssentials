package com.timedust.onyxEssentials.commands.onyxSubCommands;

import com.timedust.onyxEssentials.OnyxEssentials;
import com.timedust.onyxEssentials.commands.SubCommand;
import com.timedust.onyxEssentials.configuration.ConfigManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class QuitSubCommand implements SubCommand {

    private final OnyxEssentials plugin;
    private final ConfigManager config;

    public QuitSubCommand(OnyxEssentials plugin, ConfigManager config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public String getName() {
        return "quit-message";
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NonNull @NotNull String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /onyxessentials quit-message <message>");
        }
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        String message = sb.toString().trim();
        config.setQuitMessage(message);
        sender.sendMessage("Success");
    }
}

package com.timedust.onyxEssentials.commands.onyxSubCommands;

import com.timedust.onyxEssentials.OnyxEssentials;
import com.timedust.onyxEssentials.commands.SubCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

public class ReloadSubCommand implements SubCommand {

    private final OnyxEssentials plugin;

    public ReloadSubCommand(OnyxEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NonNull @NotNull String[] args) {
        plugin.reloadPluginConfig();
        sender.sendMessage("Конфиг перезагружен");
    }
}

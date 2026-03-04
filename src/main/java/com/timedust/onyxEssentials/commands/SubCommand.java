package com.timedust.onyxEssentials.commands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface SubCommand {
    String getName();
    void execute(@NotNull CommandSender sender, @NotNull String[] args);
}

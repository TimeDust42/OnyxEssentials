package com.timedust.onyxEssentials;

import com.timedust.onyxEssentials.commands.*;
import com.timedust.onyxEssentials.commands.supportCommands.*;
import com.timedust.onyxEssentials.configuration.ConfigManager;
import com.timedust.onyxEssentials.listeners.GodModeListener;
import com.timedust.onyxEssentials.listeners.JoinQuitListener;
import com.timedust.onyxEssentials.managers.CooldownsManager;
import com.timedust.onyxEssentials.managers.FlyManager;
import com.timedust.onyxEssentials.managers.GodModeManager;
import com.timedust.onyxEssentials.managers.VanishManager;
import com.timedust.onyxEssentials.tasks.VanishActionBarTask;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class OnyxEssentials extends JavaPlugin {

    private ConfigManager configManager;

    private CooldownsManager cooldownsManager;
    private FlyManager flyManager;
    private GodModeManager godModeManager;
    private VanishManager vanishManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        /* Managers */
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        cooldownsManager = new CooldownsManager();
        flyManager = new FlyManager();
        godModeManager = new GodModeManager();
        vanishManager = new VanishManager(this);

        /* Events */
        getServer().getPluginManager().registerEvents(new JoinQuitListener(this, configManager, flyManager, godModeManager, vanishManager), this);
        getServer().getPluginManager().registerEvents(new GodModeListener(godModeManager), this);

        /* Bukkit Tasks */
        new VanishActionBarTask(vanishManager).runTaskTimer(this, 0L, 40L);

        /* Commands */
        registerCommand("onyxessentials", new OnyxEssentialsCommand(this, configManager));
        registerCommand("heal", new HealCommand(configManager, cooldownsManager));
        registerCommand("feed", new FeedCommand(configManager, cooldownsManager));
        registerCommand("repair", new RepairCommand(configManager, cooldownsManager));
        registerCommand("fly", new FlyCommand(flyManager));
        registerCommand("vanish", new VanishCommand(vanishManager));
        registerCommand("god", new GodModeCommand(godModeManager));
    }

    @Override
    public void onDisable() {

    }

    public void reloadPluginConfig() {
        this.reloadConfig();
        configManager.loadConfig();

        this.getLogger().info("Конфигурация успешно перезагружена!");
    }

    private void registerCommand(String label, Object command) {
        if (!(command instanceof CommandExecutor)) {
            getLogger().info("Ошибка регистрации команды " + label);
            return;
        }

        Objects.requireNonNull(this.getCommand(label)).setExecutor((CommandExecutor) command);
        if (!(command instanceof TabCompleter)) {
            getLogger().info("Ошибка регистрации TabCompleter для команды " + label);
            return;
        }
        Objects.requireNonNull(this.getCommand(label)).setTabCompleter((TabCompleter) command);
    }
}

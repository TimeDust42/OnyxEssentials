package com.timedust.onyxEssentials.configuration;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final JavaPlugin plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();

    private String joinMessage;
    private String quitMessage;
    private int cooldownRepairItem;
    private int cooldownRepairArmor;
    private int cooldownRepairFull;
    private int cooldownHealMe;
    private int cooldownHealOther;
    private int cooldownHealAll;
    private int cooldownFeedMe;
    private int cooldownFeedOther;
    private int cooldownFeedAll;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        var config = plugin.getConfig();

        joinMessage = config.getString("join-message", "<green><name> зашёл на сервер!");
        quitMessage = config.getString("quit-message", "<red><name> покинул сервер");
        cooldownRepairItem = config.getInt("cooldowns.repair.item", 300);
        cooldownRepairArmor = config.getInt("cooldowns.repair.armor", 3600);
        cooldownRepairFull = config.getInt("cooldowns.repair.full", 1800);
        cooldownHealMe = config.getInt("cooldowns.heal.me", 300);
        cooldownHealOther = config.getInt("cooldowns.heal.other", 600);
        cooldownHealAll = config.getInt("cooldowns.heal.all", 1800);
        cooldownFeedMe = config.getInt("cooldowns.feed.me", 300);
        cooldownFeedOther = config.getInt("cooldowns.feed.other", 600);
        cooldownFeedAll = config.getInt("cooldowns.feed.all", 1800);
    }

    public Component getJoinMessage(String playerName) {
        return mm.deserialize(joinMessage, Placeholder.parsed("name", playerName));
    }

    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }

    public Component getQuitMessage(String playerName) {
        return mm.deserialize(quitMessage, Placeholder.parsed("name", playerName));
    }

    public void setQuitMessage(String quitMessage) {
        this.quitMessage = quitMessage;
    }

    public int getCooldownRepairItem() {
        return cooldownRepairItem;
    }

    public int getCooldownRepairArmor() {
        return cooldownRepairArmor;
    }

    public int getCooldownRepairFull() {
        return cooldownRepairFull;
    }

    public int getCooldownHealMe() {
        return cooldownHealMe;
    }

    public int getCooldownHealOther() {
        return cooldownHealOther;
    }

    public int getCooldownHealAll() {
        return cooldownHealAll;
    }

    public int getCooldownFeedMe() {
        return cooldownFeedMe;
    }

    public int getCooldownFeedOther() {
        return cooldownFeedOther;
    }

    public int getCooldownFeedAll() {
        return cooldownFeedAll;
    }
}

package com.timedust.onyxEssentials.managers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.timedust.onyxEssentials.utils.teleport.TeleportRequest;
import com.timedust.onyxEssentials.utils.teleport.TeleportUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class TeleportManager {

    private final Cache<UUID, TeleportRequest> teleportRequestsCache = CacheBuilder.newBuilder()
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build();

    public void createRequest(Player sender, Player target, TeleportRequest.RequestType requestType) {
        TeleportRequest request = new TeleportRequest(sender.getUniqueId(), target.getUniqueId(), requestType);

        teleportRequestsCache.put(target.getUniqueId(), request);

        target.sendMessage(Component.text("Запрос от " + sender.getName() + ". Введите /tpaccept",  NamedTextColor.YELLOW));
    }

    public void acceptRequest(Player acceptor) {
        TeleportRequest request = teleportRequestsCache.getIfPresent(acceptor.getUniqueId());

        if (request == null) {
            acceptor.sendMessage(Component.text("Запрос истёк или не существует.", NamedTextColor.RED));
            return;
        }

        teleportRequestsCache.invalidate(acceptor.getUniqueId());

        if (TeleportUtils.teleport(request)) {
            acceptor.sendMessage(Component.text("Запрос принят!", NamedTextColor.GREEN));
        } else {
            acceptor.sendMessage(Component.text("Игрок больше не в сети.", NamedTextColor.RED));
        }
    }

    public void cancelRequest(Player sender, Player target, TeleportRequest.RequestType requestType) {
        teleportRequestsCache.invalidate(target.getUniqueId());
    }

}

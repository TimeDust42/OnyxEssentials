package com.timedust.onyxEssentials.utils.teleport;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TeleportUtils {

    public static boolean teleport(TeleportRequest request) {
        Player requester = Bukkit.getPlayer(request.requester());
        Player target = Bukkit.getPlayer(request.target());

        if (requester == null || target == null) return false;

        if (request.type() == TeleportRequest.RequestType.NORMAL) {
            requester.teleport(target);
            requester.sendMessage(Component.text("Вы телепортированы к " + target.getName()));
        } else {
            target.teleport(requester);
            target.sendMessage(Component.text("Вы телепортированы к " + requester.getName()));
        }
        return true;
    }

}

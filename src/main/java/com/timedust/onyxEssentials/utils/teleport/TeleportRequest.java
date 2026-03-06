package com.timedust.onyxEssentials.utils.teleport;

import java.util.UUID;

public record TeleportRequest(
        UUID requester,
        UUID target,
        RequestType type
) {
    /**
     * NORMAL - /tpa
     * HERE - /tphere
     */
    public enum RequestType {
        NORMAL,
        HERE
    }
}

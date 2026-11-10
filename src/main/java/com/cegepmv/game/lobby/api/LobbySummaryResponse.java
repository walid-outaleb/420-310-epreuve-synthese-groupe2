package com.cegepmv.game.lobby.api;

import com.cegepmv.game.lobby.application.LobbySummaryOutput;

public final class LobbySummaryResponse {

    public final String id;
    public final String hostId;
    public final int playerCount;
    public final int maxPlayers;

    private LobbySummaryResponse(LobbySummaryOutput output) {
        this.id = output.id();
        this.hostId = output.hostId();
        this.playerCount = output.playerCount();
        this.maxPlayers = output.maxPlayers();
    }

    public static LobbySummaryResponse from(LobbySummaryOutput output) {
        return new LobbySummaryResponse(output);
    }
}

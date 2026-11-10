package com.cegepmv.game.lobby.api;

import jakarta.json.bind.annotation.JsonbNillable;

@JsonbNillable
public final class MemberResponse {

    public final String playerId;
    public final String pseudonym;
    public final Object hero = null;

    public MemberResponse(String playerId, String pseudonym) {
        this.playerId = playerId;
        this.pseudonym = pseudonym;
    }
}

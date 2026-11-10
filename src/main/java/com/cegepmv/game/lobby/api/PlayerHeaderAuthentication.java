package com.cegepmv.game.lobby.api;

import jakarta.ws.rs.core.HttpHeaders;

public class PlayerHeaderAuthentication implements PlayerAuthentication {

    private static final String PLAYER_HEADER = "Player";

    @Override
    public String authenticate(HttpHeaders headers) {
        String playerId = headers.getHeaderString(PLAYER_HEADER);
        if (playerId == null || playerId.isBlank()) {
            throw new NotAuthenticatedException();
        }
        return playerId;
    }
}

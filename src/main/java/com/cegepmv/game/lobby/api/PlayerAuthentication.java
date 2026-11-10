package com.cegepmv.game.lobby.api;

import jakarta.ws.rs.core.HttpHeaders;

public interface PlayerAuthentication {

    String authenticate(HttpHeaders headers);
}

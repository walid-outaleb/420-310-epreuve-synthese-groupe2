package com.cegepmv.game.lobby.domain;

import java.util.Objects;
import java.util.UUID;

public record LobbyId(String value) {

    public LobbyId {
        Objects.requireNonNull(value);
    }

    public static LobbyId generate() {
        return new LobbyId(UUID.randomUUID().toString());
    }
}

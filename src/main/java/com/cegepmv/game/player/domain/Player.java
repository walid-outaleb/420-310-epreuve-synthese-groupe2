package com.cegepmv.game.player.domain;

import java.util.Objects;
import java.util.UUID;

public record Player(UUID id) {
    public Player {
        Objects.requireNonNull(id);
    }

    public static Player create() {
        return new Player(UUID.randomUUID());
    }
}

package com.cegepmv.game.lobby.domain;

import java.util.Objects;

public record Pseudonym(String value) {

    public Pseudonym {
        Objects.requireNonNull(value);
    }
}

package com.cegepmv.game.lobby.domain;

import com.cegepmv.game.lobby.domain.exception.InvalidPlayerException;

public record PlayerId(String value) {

    public PlayerId {
        if (value == null || value.isBlank()) {
            throw new InvalidPlayerException();
        }
    }
}

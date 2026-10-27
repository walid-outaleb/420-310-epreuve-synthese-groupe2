package com.cegepmv.game.lobby.domain;

import com.cegepmv.game.lobby.domain.exception.InvalidHostException;

public record HostId(String value) {

    public HostId {
        if (value == null || value.isBlank()) {
            throw new InvalidHostException();
        }
    }
}

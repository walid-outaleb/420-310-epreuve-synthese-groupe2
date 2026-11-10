package com.cegepmv.game.lobby.domain;

import java.util.Objects;

public record Member(PlayerId playerId, Pseudonym pseudonym) {

    public Member {
        Objects.requireNonNull(playerId);
        Objects.requireNonNull(pseudonym);
    }
}

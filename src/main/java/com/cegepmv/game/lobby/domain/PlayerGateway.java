package com.cegepmv.game.lobby.domain;

public interface PlayerGateway {

    Pseudonym fetchPseudonymOf(PlayerId playerId);
}

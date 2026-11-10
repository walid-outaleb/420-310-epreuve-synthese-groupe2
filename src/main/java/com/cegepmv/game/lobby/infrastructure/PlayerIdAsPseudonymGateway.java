package com.cegepmv.game.lobby.infrastructure;

import com.cegepmv.game.lobby.domain.PlayerGateway;
import com.cegepmv.game.lobby.domain.PlayerId;
import com.cegepmv.game.lobby.domain.Pseudonym;

public class PlayerIdAsPseudonymGateway implements PlayerGateway {

    @Override
    public Pseudonym fetchPseudonymOf(PlayerId playerId) {
        return new Pseudonym(playerId.value());
    }
}

package com.cegepmv.game.lobby.domain;

import java.util.List;

public class LobbyFactory {

    public Lobby create(PlayerId hostId, Pseudonym hostPseudonym) {
        return new Lobby(
                LobbyId.generate(), LobbyState.OPEN, List.of(new Member(hostId, hostPseudonym)));
    }
}

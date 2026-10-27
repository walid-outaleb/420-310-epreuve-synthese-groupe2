package com.cegepmv.game.lobby.application;

import com.cegepmv.game.lobby.domain.Lobby;

public class LobbyOutputMapper {

    public LobbyOutput toOutput(Lobby lobby) {
        return new LobbyOutput(
                lobby.id().value(), lobby.hostId().value(), lobby.maximumPlayers());
    }
}

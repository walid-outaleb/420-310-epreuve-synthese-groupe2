package com.cegepmv.game.lobby.infrastructure;

import com.cegepmv.game.lobby.domain.Lobby;

public class LobbyPersistenceMapper {

    public LobbyPersistenceModel toPersistence(Lobby lobby) {
        return new LobbyPersistenceModel(lobby.id().value(), lobby.hostId().value());
    }
}

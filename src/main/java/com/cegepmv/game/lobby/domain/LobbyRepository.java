package com.cegepmv.game.lobby.domain;

import java.util.List;

public interface LobbyRepository {

    void save(Lobby lobby);

    Lobby fetchById(LobbyId id);

    List<Lobby> findAllAcceptingPlayers();

    boolean isMemberOfOpenLobby(PlayerId playerId);
}

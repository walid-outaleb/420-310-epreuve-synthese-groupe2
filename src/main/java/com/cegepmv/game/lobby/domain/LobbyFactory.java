package com.cegepmv.game.lobby.domain;

public class LobbyFactory {

    public Lobby create(String hostId) {
        return new Lobby(LobbyId.generate(), new HostId(hostId));
    }
}

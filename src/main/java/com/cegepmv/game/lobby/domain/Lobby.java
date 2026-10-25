package com.cegepmv.game.lobby.domain;

import java.util.Objects;

public class Lobby {

    public static final int MAXIMUM_PLAYERS = 4;

    private final LobbyId id;
    private final HostId hostId;

    public Lobby(LobbyId id, HostId hostId) {
        this.id = Objects.requireNonNull(id);
        this.hostId = Objects.requireNonNull(hostId);
    }

    public LobbyId id() {
        return id;
    }

    public HostId hostId() {
        return hostId;
    }

    public int maximumPlayers() {
        return MAXIMUM_PLAYERS;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Lobby lobby && id.equals(lobby.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

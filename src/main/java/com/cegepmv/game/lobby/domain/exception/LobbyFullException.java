package com.cegepmv.game.lobby.domain.exception;

public class LobbyFullException extends LobbyException {

    public LobbyFullException() {
        super("LOBBY_FULL", "Le lobby est plein.");
    }
}

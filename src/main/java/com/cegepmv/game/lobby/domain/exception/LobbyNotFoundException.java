package com.cegepmv.game.lobby.domain.exception;

public class LobbyNotFoundException extends LobbyException {

    public LobbyNotFoundException() {
        super("LOBBY_NOT_FOUND", "Le lobby est introuvable.");
    }
}

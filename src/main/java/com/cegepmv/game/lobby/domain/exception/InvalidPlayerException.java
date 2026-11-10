package com.cegepmv.game.lobby.domain.exception;

public class InvalidPlayerException extends LobbyException {

    public InvalidPlayerException() {
        super("INVALID_PLAYER", "L'identifiant du joueur est obligatoire.");
    }
}

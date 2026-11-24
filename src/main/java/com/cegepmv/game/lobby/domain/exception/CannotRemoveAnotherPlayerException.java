package com.cegepmv.game.lobby.domain.exception;

public class CannotRemoveAnotherPlayerException extends LobbyException {

    public CannotRemoveAnotherPlayerException() {
        super("FORBIDDEN", "Un joueur ne peut retirer que lui-même d'un lobby.");
    }
}

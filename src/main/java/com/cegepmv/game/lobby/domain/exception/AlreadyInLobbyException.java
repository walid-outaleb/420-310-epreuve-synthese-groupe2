package com.cegepmv.game.lobby.domain.exception;

public class AlreadyInLobbyException extends LobbyException {

    public AlreadyInLobbyException() {
        super("ALREADY_IN_LOBBY", "Le joueur est déjà membre d'un lobby ouvert.");
    }
}

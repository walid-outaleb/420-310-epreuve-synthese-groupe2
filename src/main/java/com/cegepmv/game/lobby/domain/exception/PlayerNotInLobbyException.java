package com.cegepmv.game.lobby.domain.exception;

public class PlayerNotInLobbyException extends LobbyException {

    public PlayerNotInLobbyException() {
        super("PLAYER_NOT_IN_LOBBY", "Le joueur n'est pas membre de ce lobby.");
    }
}

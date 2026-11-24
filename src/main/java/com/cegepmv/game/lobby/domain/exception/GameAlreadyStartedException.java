package com.cegepmv.game.lobby.domain.exception;

public class GameAlreadyStartedException extends LobbyException {

    public GameAlreadyStartedException() {
        super("GAME_ALREADY_STARTED", "La partie est déjà lancée.");
    }
}

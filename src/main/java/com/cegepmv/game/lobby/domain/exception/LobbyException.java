package com.cegepmv.game.lobby.domain.exception;

public abstract class LobbyException extends RuntimeException {

    protected LobbyException(String message) {
        super(message);
    }
}

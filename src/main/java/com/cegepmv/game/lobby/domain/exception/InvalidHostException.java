package com.cegepmv.game.lobby.domain.exception;

public class InvalidHostException extends LobbyException {

    public InvalidHostException() {
        super("Host id cannot be blank.");
    }
}

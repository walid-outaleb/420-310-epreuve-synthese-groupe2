package com.cegepmv.game.lobby.domain.exception;

public abstract class LobbyException extends RuntimeException {

    private final String code;

    protected LobbyException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String code() {
        return code;
    }
}

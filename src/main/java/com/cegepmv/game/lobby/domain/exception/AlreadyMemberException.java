package com.cegepmv.game.lobby.domain.exception;

public class AlreadyMemberException extends LobbyException {

    public AlreadyMemberException() {
        super("ALREADY_MEMBER", "Le joueur est déjà membre de ce lobby.");
    }
}

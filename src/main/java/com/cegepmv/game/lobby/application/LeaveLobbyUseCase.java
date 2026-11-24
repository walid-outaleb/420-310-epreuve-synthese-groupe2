package com.cegepmv.game.lobby.application;

import com.cegepmv.game.lobby.domain.Lobby;
import com.cegepmv.game.lobby.domain.LobbyId;
import com.cegepmv.game.lobby.domain.LobbyRepository;
import com.cegepmv.game.lobby.domain.PlayerId;
import com.cegepmv.game.lobby.domain.exception.CannotRemoveAnotherPlayerException;

public class LeaveLobbyUseCase {

    private final LobbyRepository lobbyRepository;

    public LeaveLobbyUseCase(LobbyRepository lobbyRepository) {
        this.lobbyRepository = lobbyRepository;
    }

    public void execute(LeaveLobbyCommand command) {
        PlayerId leavingPlayerId = new PlayerId(command.playerId());
        if (!leavingPlayerId.equals(new PlayerId(command.requesterId()))) {
            throw new CannotRemoveAnotherPlayerException();
        }
        Lobby lobby = lobbyRepository.fetchById(new LobbyId(command.lobbyId()));
        lobby.leave(leavingPlayerId);
        lobbyRepository.save(lobby);
    }
}

package com.cegepmv.game.lobby.application;

import com.cegepmv.game.lobby.domain.Lobby;
import com.cegepmv.game.lobby.domain.LobbyFactory;
import com.cegepmv.game.lobby.domain.LobbyRepository;

public class CreateLobbyUseCase {

    private final LobbyFactory lobbyFactory;
    private final LobbyRepository lobbyRepository;
    private final LobbyOutputMapper outputMapper;

    public CreateLobbyUseCase(
            LobbyFactory lobbyFactory,
            LobbyRepository lobbyRepository,
            LobbyOutputMapper outputMapper) {
        this.lobbyFactory = lobbyFactory;
        this.lobbyRepository = lobbyRepository;
        this.outputMapper = outputMapper;
    }

    public LobbyOutput execute(CreateLobbyCommand command) {
        Lobby lobby = lobbyFactory.create(command.hostId());
        lobbyRepository.save(lobby);
        return outputMapper.toOutput(lobby);
    }
}

package com.cegepmv.game.lobby.application;

import com.cegepmv.game.lobby.domain.Lobby;
import com.cegepmv.game.lobby.domain.LobbyFactory;
import com.cegepmv.game.lobby.domain.LobbyRepository;
import com.cegepmv.game.lobby.domain.PlayerGateway;
import com.cegepmv.game.lobby.domain.PlayerId;
import com.cegepmv.game.lobby.domain.Pseudonym;
import com.cegepmv.game.lobby.domain.exception.AlreadyInLobbyException;

public class CreateLobbyUseCase {

    private final LobbyFactory lobbyFactory;
    private final LobbyRepository lobbyRepository;
    private final PlayerGateway playerGateway;
    private final LobbyOutputMapper outputMapper;

    public CreateLobbyUseCase(
            LobbyFactory lobbyFactory,
            LobbyRepository lobbyRepository,
            PlayerGateway playerGateway,
            LobbyOutputMapper outputMapper) {
        this.lobbyFactory = lobbyFactory;
        this.lobbyRepository = lobbyRepository;
        this.playerGateway = playerGateway;
        this.outputMapper = outputMapper;
    }

    public LobbyOutput execute(CreateLobbyCommand command) {
        PlayerId hostId = new PlayerId(command.playerId());
        if (lobbyRepository.isMemberOfOpenLobby(hostId)) {
            throw new AlreadyInLobbyException();
        }
        Pseudonym hostPseudonym = playerGateway.fetchPseudonymOf(hostId);
        Lobby lobby = lobbyFactory.create(hostId, hostPseudonym);
        lobbyRepository.save(lobby);
        return outputMapper.toOutput(lobby);
    }
}

package com.cegepmv.game.lobby.application;

import com.cegepmv.game.lobby.domain.Lobby;
import com.cegepmv.game.lobby.domain.LobbyId;
import com.cegepmv.game.lobby.domain.LobbyRepository;
import com.cegepmv.game.lobby.domain.Member;
import com.cegepmv.game.lobby.domain.PlayerGateway;
import com.cegepmv.game.lobby.domain.PlayerId;
import com.cegepmv.game.lobby.domain.exception.AlreadyInLobbyException;

public class JoinLobbyUseCase {

    private final LobbyRepository lobbyRepository;
    private final PlayerGateway playerGateway;
    private final LobbyOutputMapper outputMapper;

    public JoinLobbyUseCase(
            LobbyRepository lobbyRepository,
            PlayerGateway playerGateway,
            LobbyOutputMapper outputMapper) {
        this.lobbyRepository = lobbyRepository;
        this.playerGateway = playerGateway;
        this.outputMapper = outputMapper;
    }

    public LobbyOutput execute(JoinLobbyCommand command) {
        PlayerId playerId = new PlayerId(command.playerId());
        Lobby lobby = lobbyRepository.fetchById(new LobbyId(command.lobbyId()));
        if (!lobby.hasMember(playerId) && lobbyRepository.isMemberOfOpenLobby(playerId)) {
            throw new AlreadyInLobbyException();
        }
        lobby.join(new Member(playerId, playerGateway.fetchPseudonymOf(playerId)));
        lobbyRepository.save(lobby);
        return outputMapper.toOutput(lobby);
    }
}

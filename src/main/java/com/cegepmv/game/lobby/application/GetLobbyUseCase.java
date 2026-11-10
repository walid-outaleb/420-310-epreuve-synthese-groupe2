package com.cegepmv.game.lobby.application;

import com.cegepmv.game.lobby.domain.LobbyId;
import com.cegepmv.game.lobby.domain.LobbyRepository;

public class GetLobbyUseCase {

    private final LobbyRepository lobbyRepository;
    private final LobbyOutputMapper outputMapper;

    public GetLobbyUseCase(LobbyRepository lobbyRepository, LobbyOutputMapper outputMapper) {
        this.lobbyRepository = lobbyRepository;
        this.outputMapper = outputMapper;
    }

    public LobbyOutput execute(GetLobbyQuery query) {
        return outputMapper.toOutput(lobbyRepository.fetchById(new LobbyId(query.lobbyId())));
    }
}

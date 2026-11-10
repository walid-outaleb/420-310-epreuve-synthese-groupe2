package com.cegepmv.game.lobby.application;

import com.cegepmv.game.lobby.domain.LobbyRepository;
import java.util.List;

public class ListOpenLobbiesUseCase {

    private final LobbyRepository lobbyRepository;
    private final LobbyOutputMapper outputMapper;

    public ListOpenLobbiesUseCase(LobbyRepository lobbyRepository, LobbyOutputMapper outputMapper) {
        this.lobbyRepository = lobbyRepository;
        this.outputMapper = outputMapper;
    }

    public List<LobbySummaryOutput> execute() {
        return lobbyRepository.findAllAcceptingPlayers().stream().map(outputMapper::toSummary).toList();
    }
}

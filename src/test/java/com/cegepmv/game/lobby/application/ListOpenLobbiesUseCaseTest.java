package com.cegepmv.game.lobby.application;

import static com.cegepmv.game.lobby.domain.LobbyBuilder.aLobby;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cegepmv.game.lobby.domain.Lobby;
import com.cegepmv.game.lobby.domain.LobbyRepository;
import com.cegepmv.game.lobby.domain.LobbyState;
import com.cegepmv.game.lobby.infrastructure.InMemoryLobbyRepository;
import com.cegepmv.game.lobby.infrastructure.LobbyPersistenceMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListOpenLobbiesUseCaseTest {

    private LobbyRepository lobbyRepository;
    private ListOpenLobbiesUseCase listOpenLobbiesUseCase;

    @BeforeEach
    void createUseCase() {
        lobbyRepository = new InMemoryLobbyRepository(new LobbyPersistenceMapper());
        listOpenLobbiesUseCase = new ListOpenLobbiesUseCase(lobbyRepository, new LobbyOutputMapper());
    }

    @Test
    void givenOpenLobbyWithSeats_whenListing_thenShowsHostAndPlayerCountOutOfFour() {
        Lobby lobby = aLobby().withMemberCount(2).build();
        lobbyRepository.save(lobby);

        List<LobbySummaryOutput> lobbies = listOpenLobbiesUseCase.execute();

        assertEquals(List.of(new LobbySummaryOutput(lobby.id().value(), "p-1", 2, 4)), lobbies);
    }

    @Test
    void givenFullLobby_whenListing_thenItIsNotShown() {
        lobbyRepository.save(aLobby().withMemberCount(4).build());

        assertEquals(List.of(), listOpenLobbiesUseCase.execute());
    }

    @Test
    void givenStartedLobby_whenListing_thenItIsNotShown() {
        lobbyRepository.save(aLobby().withState(LobbyState.STARTED).withMemberCount(2).build());

        assertEquals(List.of(), listOpenLobbiesUseCase.execute());
    }
}

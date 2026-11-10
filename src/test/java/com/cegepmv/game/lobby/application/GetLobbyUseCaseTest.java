package com.cegepmv.game.lobby.application;

import static com.cegepmv.game.lobby.domain.LobbyBuilder.aLobby;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.cegepmv.game.lobby.domain.Lobby;
import com.cegepmv.game.lobby.domain.LobbyId;
import com.cegepmv.game.lobby.domain.LobbyRepository;
import com.cegepmv.game.lobby.domain.exception.LobbyNotFoundException;
import com.cegepmv.game.lobby.infrastructure.InMemoryLobbyRepository;
import com.cegepmv.game.lobby.infrastructure.LobbyPersistenceMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetLobbyUseCaseTest {

    private LobbyRepository lobbyRepository;
    private GetLobbyUseCase getLobbyUseCase;

    @BeforeEach
    void createUseCase() {
        lobbyRepository = new InMemoryLobbyRepository(new LobbyPersistenceMapper());
        getLobbyUseCase = new GetLobbyUseCase(lobbyRepository, new LobbyOutputMapper());
    }

    @Test
    void givenExistingLobby_whenGettingIt_thenReturnsHostStateAndMembers() {
        Lobby lobby = aLobby().withMemberCount(2).build();
        lobbyRepository.save(lobby);

        LobbyOutput output = getLobbyUseCase.execute(new GetLobbyQuery(lobby.id().value()));

        assertEquals("OPEN", output.state());
        assertEquals("p-1", output.hostId());
        assertEquals(
                List.of(new MemberOutput("p-1", "Player1"), new MemberOutput("p-2", "Player2")),
                output.players());
        assertEquals(2, output.availableSeats());
    }

    @Test
    void givenUnknownLobby_whenGettingIt_thenThrowsLobbyNotFound() {
        GetLobbyQuery query = new GetLobbyQuery(LobbyId.generate().value());

        assertThrows(LobbyNotFoundException.class, () -> getLobbyUseCase.execute(query));
    }
}

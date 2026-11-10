package com.cegepmv.game.lobby.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.cegepmv.game.lobby.domain.LobbyFactory;
import com.cegepmv.game.lobby.domain.LobbyRepository;
import com.cegepmv.game.lobby.domain.PlayerGateway;
import com.cegepmv.game.lobby.domain.Pseudonym;
import com.cegepmv.game.lobby.domain.exception.AlreadyInLobbyException;
import com.cegepmv.game.lobby.infrastructure.InMemoryLobbyRepository;
import com.cegepmv.game.lobby.infrastructure.LobbyPersistenceMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CreateLobbyUseCaseTest {

    private static final String HOST_ID = "p-7f3a";
    private static final String OTHER_PLAYER_ID = "p-91bc";
    private static final String HOST_PSEUDONYM = "Aria";

    private CreateLobbyUseCase createLobbyUseCase;

    @BeforeEach
    void createUseCase() {
        LobbyRepository lobbyRepository = new InMemoryLobbyRepository(new LobbyPersistenceMapper());
        PlayerGateway playerGateway = playerId -> new Pseudonym(HOST_PSEUDONYM);
        createLobbyUseCase =
                new CreateLobbyUseCase(
                        new LobbyFactory(), lobbyRepository, playerGateway, new LobbyOutputMapper());
    }

    @Test
    void givenPlayer_whenCreatingLobby_thenLobbyIsOpenWithPlayerAsOnlyMember() {
        LobbyOutput lobby = createLobbyUseCase.execute(new CreateLobbyCommand(HOST_ID));

        assertEquals("OPEN", lobby.state());
        assertEquals(HOST_ID, lobby.hostId());
        assertEquals(List.of(new MemberOutput(HOST_ID, HOST_PSEUDONYM)), lobby.players());
        assertEquals(3, lobby.availableSeats());
    }

    @Test
    void givenPlayerAlreadyInOpenLobby_whenCreatingLobby_thenThrowsAlreadyInLobby() {
        createLobbyUseCase.execute(new CreateLobbyCommand(HOST_ID));

        assertThrows(
                AlreadyInLobbyException.class,
                () -> createLobbyUseCase.execute(new CreateLobbyCommand(HOST_ID)));
    }

    @Test
    void givenTwoDifferentPlayers_whenEachCreatesLobby_thenBothSucceedWithDistinctIds() {
        LobbyOutput first = createLobbyUseCase.execute(new CreateLobbyCommand(HOST_ID));
        LobbyOutput second = createLobbyUseCase.execute(new CreateLobbyCommand(OTHER_PLAYER_ID));

        assertNotEquals(first.id(), second.id());
    }
}

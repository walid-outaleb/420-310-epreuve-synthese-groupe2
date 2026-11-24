package com.cegepmv.game.lobby.application;

import static com.cegepmv.game.lobby.domain.LobbyBuilder.aLobby;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.cegepmv.game.lobby.domain.Lobby;
import com.cegepmv.game.lobby.domain.LobbyRepository;
import com.cegepmv.game.lobby.domain.exception.CannotRemoveAnotherPlayerException;
import com.cegepmv.game.lobby.domain.exception.LobbyNotFoundException;
import com.cegepmv.game.lobby.infrastructure.InMemoryLobbyRepository;
import com.cegepmv.game.lobby.infrastructure.LobbyPersistenceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaveLobbyUseCaseTest {

    private LobbyRepository lobbyRepository;
    private LeaveLobbyUseCase leaveLobbyUseCase;

    @BeforeEach
    void createUseCase() {
        lobbyRepository = new InMemoryLobbyRepository(new LobbyPersistenceMapper());
        leaveLobbyUseCase = new LeaveLobbyUseCase(lobbyRepository);
    }

    @Test
    void givenMember_whenLeaving_thenHeIsNoLongerInTheSavedLobby() {
        Lobby lobby = aLobby().withMemberCount(2).build();
        lobbyRepository.save(lobby);

        leaveLobbyUseCase.execute(new LeaveLobbyCommand(lobby.id().value(), "p-2", "p-2"));

        assertEquals(1, lobbyRepository.fetchById(lobby.id()).members().size());
    }

    @Test
    void givenHostLeaves_whenSomeoneElseRemains_thenNextPlayerIsHost() {
        Lobby lobby = aLobby().withMemberCount(2).build();
        lobbyRepository.save(lobby);

        leaveLobbyUseCase.execute(new LeaveLobbyCommand(lobby.id().value(), "p-1", "p-1"));

        assertEquals("p-2", lobbyRepository.fetchById(lobby.id()).hostId().value());
    }

    @Test
    void givenLastMember_whenLeaving_thenLobbyIsClosedAndNoLongerFound() {
        Lobby lobby = aLobby().withMemberCount(1).build();
        lobbyRepository.save(lobby);

        leaveLobbyUseCase.execute(new LeaveLobbyCommand(lobby.id().value(), "p-1", "p-1"));

        assertThrows(LobbyNotFoundException.class, () -> lobbyRepository.fetchById(lobby.id()));
        assertEquals(false, lobbyRepository.isMemberOfOpenLobby(lobby.hostId()));
    }

    @Test
    void givenAnotherPlayerAsRequester_whenRemovingSomeone_thenThrowsForbidden() {
        Lobby lobby = aLobby().withMemberCount(2).build();
        lobbyRepository.save(lobby);
        LeaveLobbyCommand command = new LeaveLobbyCommand(lobby.id().value(), "p-2", "p-1");

        assertThrows(CannotRemoveAnotherPlayerException.class, () -> leaveLobbyUseCase.execute(command));
    }
}

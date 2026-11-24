package com.cegepmv.game.lobby.application;

import static com.cegepmv.game.lobby.domain.LobbyBuilder.aLobby;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.cegepmv.game.lobby.domain.Lobby;
import com.cegepmv.game.lobby.domain.LobbyId;
import com.cegepmv.game.lobby.domain.LobbyRepository;
import com.cegepmv.game.lobby.domain.PlayerGateway;
import com.cegepmv.game.lobby.domain.Pseudonym;
import com.cegepmv.game.lobby.domain.exception.AlreadyInLobbyException;
import com.cegepmv.game.lobby.domain.exception.AlreadyMemberException;
import com.cegepmv.game.lobby.domain.exception.LobbyFullException;
import com.cegepmv.game.lobby.domain.exception.LobbyNotFoundException;
import com.cegepmv.game.lobby.infrastructure.InMemoryLobbyRepository;
import com.cegepmv.game.lobby.infrastructure.LobbyPersistenceMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JoinLobbyUseCaseTest {

    private static final String NEWCOMER_ID = "p-9";
    private static final String NEWCOMER_PSEUDONYM = "Bram";

    private LobbyRepository lobbyRepository;
    private JoinLobbyUseCase joinLobbyUseCase;

    @BeforeEach
    void createUseCase() {
        lobbyRepository = new InMemoryLobbyRepository(new LobbyPersistenceMapper());
        PlayerGateway playerGateway = playerId -> new Pseudonym(NEWCOMER_PSEUDONYM);
        joinLobbyUseCase = new JoinLobbyUseCase(lobbyRepository, playerGateway, new LobbyOutputMapper());
    }

    @Test
    void givenOpenLobby_whenPlayerJoins_thenRepresentationShowsHimAndOneFewerSeat() {
        Lobby lobby = aLobby().withMemberCount(1).build();
        lobbyRepository.save(lobby);

        LobbyOutput output = joinLobbyUseCase.execute(new JoinLobbyCommand(lobby.id().value(), NEWCOMER_ID));

        assertEquals(
                List.of(new MemberOutput("p-1", "Player1"), new MemberOutput(NEWCOMER_ID, NEWCOMER_PSEUDONYM)),
                output.players());
        assertEquals(2, output.availableSeats());
    }

    @Test
    void givenJoinedPlayer_whenGettingLobbyAgain_thenHeIsStillAMember() {
        Lobby lobby = aLobby().withMemberCount(1).build();
        lobbyRepository.save(lobby);
        joinLobbyUseCase.execute(new JoinLobbyCommand(lobby.id().value(), NEWCOMER_ID));

        Lobby reloaded = lobbyRepository.fetchById(lobby.id());

        assertEquals(2, reloaded.members().size());
    }

    @Test
    void givenUnknownLobby_whenJoining_thenThrowsLobbyNotFound() {
        JoinLobbyCommand command = new JoinLobbyCommand(LobbyId.generate().value(), NEWCOMER_ID);

        assertThrows(LobbyNotFoundException.class, () -> joinLobbyUseCase.execute(command));
    }

    @Test
    void givenFullLobby_whenJoining_thenThrowsLobbyFull() {
        Lobby lobby = aLobby().withMemberCount(4).build();
        lobbyRepository.save(lobby);

        assertThrows(
                LobbyFullException.class,
                () -> joinLobbyUseCase.execute(new JoinLobbyCommand(lobby.id().value(), NEWCOMER_ID)));
    }

    @Test
    void givenAlreadyMember_whenJoiningSameLobby_thenThrowsAlreadyMember() {
        Lobby lobby = aLobby().withMemberCount(2).build();
        lobbyRepository.save(lobby);

        assertThrows(
                AlreadyMemberException.class,
                () -> joinLobbyUseCase.execute(new JoinLobbyCommand(lobby.id().value(), "p-2")));
    }

    @Test
    void givenMemberOfAnotherOpenLobby_whenJoining_thenThrowsAlreadyInLobby() {
        Lobby other = aLobby().withMemberCount(1).build();
        Lobby target = aLobby().withMemberCount(1).build();
        lobbyRepository.save(other);
        lobbyRepository.save(target);
        joinLobbyUseCase.execute(new JoinLobbyCommand(other.id().value(), NEWCOMER_ID));

        assertThrows(
                AlreadyInLobbyException.class,
                () -> joinLobbyUseCase.execute(new JoinLobbyCommand(target.id().value(), NEWCOMER_ID)));
    }
}

package com.cegepmv.game.lobby.domain;

import static com.cegepmv.game.lobby.domain.LobbyBuilder.aLobby;
import static com.cegepmv.game.lobby.domain.LobbyBuilder.memberNumber;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.cegepmv.game.lobby.domain.exception.AlreadyMemberException;
import com.cegepmv.game.lobby.domain.exception.GameAlreadyStartedException;
import com.cegepmv.game.lobby.domain.exception.LobbyFullException;
import com.cegepmv.game.lobby.domain.exception.PlayerNotInLobbyException;
import java.util.List;
import org.junit.jupiter.api.Test;

class LobbyTest {

    @Test
    void givenOpenLobbyWithSeat_whenPlayerJoins_thenHeIsAddedAfterTheHost() {
        Lobby lobby = aLobby().withMemberCount(1).build();

        lobby.join(memberNumber(2));

        assertEquals(List.of(memberNumber(1), memberNumber(2)), lobby.members());
        assertEquals(2, lobby.availableSeats());
    }

    @Test
    void givenFullLobby_whenFifthPlayerJoins_thenThrowsLobbyFull() {
        Lobby lobby = aLobby().withMemberCount(4).build();

        assertThrows(LobbyFullException.class, () -> lobby.join(memberNumber(5)));
    }

    @Test
    void givenMemberOfLobby_whenJoiningAgain_thenThrowsAlreadyMember() {
        Lobby lobby = aLobby().withMemberCount(2).build();

        assertThrows(AlreadyMemberException.class, () -> lobby.join(memberNumber(2)));
    }

    @Test
    void givenStartedLobby_whenPlayerJoins_thenThrowsGameAlreadyStarted() {
        Lobby lobby = aLobby().withState(LobbyState.STARTED).withMemberCount(2).build();

        assertThrows(GameAlreadyStartedException.class, () -> lobby.join(memberNumber(3)));
    }

    @Test
    void givenMember_whenLeaving_thenSeatIsFreedAndHeIsRemoved() {
        Lobby lobby = aLobby().withMemberCount(3).build();

        lobby.leave(memberNumber(3).playerId());

        assertEquals(List.of(memberNumber(1), memberNumber(2)), lobby.members());
        assertEquals(2, lobby.availableSeats());
    }

    @Test
    void givenHostLeaves_whenOtherMembersRemain_thenEarliestJoinedBecomesHost() {
        Lobby lobby = aLobby().withMemberCount(3).build();

        lobby.leave(memberNumber(1).playerId());

        assertEquals(memberNumber(2).playerId(), lobby.hostId());
    }

    @Test
    void givenLastMember_whenLeaving_thenLobbyIsClosed() {
        Lobby lobby = aLobby().withMemberCount(1).build();

        lobby.leave(memberNumber(1).playerId());

        assertEquals(LobbyState.CLOSED, lobby.state());
    }

    @Test
    void givenNonMember_whenLeaving_thenThrowsPlayerNotInLobby() {
        Lobby lobby = aLobby().withMemberCount(2).build();

        assertThrows(PlayerNotInLobbyException.class, () -> lobby.leave(memberNumber(9).playerId()));
    }

    @Test
    void givenStartedLobby_whenMemberLeaves_thenThrowsGameAlreadyStarted() {
        Lobby lobby = aLobby().withState(LobbyState.STARTED).withMemberCount(2).build();

        assertThrows(GameAlreadyStartedException.class, () -> lobby.leave(memberNumber(2).playerId()));
    }
}

package com.cegepmv.game.lobby.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.cegepmv.game.lobby.domain.exception.InvalidPlayerException;
import java.util.List;
import org.junit.jupiter.api.Test;

class LobbyFactoryTest {

    private static final PlayerId HOST_ID = new PlayerId("p-7f3a");
    private static final Pseudonym HOST_PSEUDONYM = new Pseudonym("Aria");

    private final LobbyFactory lobbyFactory = new LobbyFactory();

    @Test
    void givenHost_whenCreatingLobby_thenLobbyIsOpen() {
        Lobby lobby = lobbyFactory.create(HOST_ID, HOST_PSEUDONYM);

        assertEquals(LobbyState.OPEN, lobby.state());
    }

    @Test
    void givenHost_whenCreatingLobby_thenHostIsTheFirstMember() {
        Lobby lobby = lobbyFactory.create(HOST_ID, HOST_PSEUDONYM);

        assertEquals(HOST_ID, lobby.hostId());
        assertEquals(List.of(new Member(HOST_ID, HOST_PSEUDONYM)), lobby.members());
    }

    @Test
    void givenHost_whenCreatingLobby_thenThreeSeatsRemain() {
        Lobby lobby = lobbyFactory.create(HOST_ID, HOST_PSEUDONYM);

        assertEquals(3, lobby.availableSeats());
    }

    @Test
    void givenSameHost_whenCreatingTwoLobbies_thenIdsDiffer() {
        Lobby first = lobbyFactory.create(HOST_ID, HOST_PSEUDONYM);
        Lobby second = lobbyFactory.create(HOST_ID, HOST_PSEUDONYM);

        assertNotEquals(first.id(), second.id());
    }

    @Test
    void givenBlankPlayerId_whenBuildingPlayerId_thenThrowsInvalidPlayer() {
        assertThrows(InvalidPlayerException.class, () -> new PlayerId(" "));
    }
}

package com.cegepmv.game.lobby.domain;

import com.cegepmv.game.lobby.domain.exception.AlreadyMemberException;
import com.cegepmv.game.lobby.domain.exception.GameAlreadyStartedException;
import com.cegepmv.game.lobby.domain.exception.LobbyFullException;
import com.cegepmv.game.lobby.domain.exception.PlayerNotInLobbyException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lobby {

    public static final int MAXIMUM_PLAYERS = 4;

    private final LobbyId id;
    private LobbyState state;
    private final List<Member> members;

    public Lobby(LobbyId id, LobbyState state, List<Member> members) {
        this.id = Objects.requireNonNull(id);
        this.state = Objects.requireNonNull(state);
        this.members = new ArrayList<>(members);
    }

    public void join(Member newMember) {
        if (state == LobbyState.STARTED) {
            throw new GameAlreadyStartedException();
        }
        if (hasMember(newMember.playerId())) {
            throw new AlreadyMemberException();
        }
        if (availableSeats() == 0) {
            throw new LobbyFullException();
        }
        members.add(newMember);
    }

    public void leave(PlayerId playerId) {
        if (state == LobbyState.STARTED) {
            throw new GameAlreadyStartedException();
        }
        if (!hasMember(playerId)) {
            throw new PlayerNotInLobbyException();
        }
        members.removeIf(member -> member.playerId().equals(playerId));
        if (members.isEmpty()) {
            state = LobbyState.CLOSED;
        }
    }

    public boolean hasMember(PlayerId playerId) {
        return members.stream().anyMatch(member -> member.playerId().equals(playerId));
    }

    public LobbyId id() {
        return id;
    }

    public LobbyState state() {
        return state;
    }

    public PlayerId hostId() {
        return members.getFirst().playerId();
    }

    public List<Member> members() {
        return List.copyOf(members);
    }

    public int availableSeats() {
        return MAXIMUM_PLAYERS - members.size();
    }

    public boolean acceptsPlayers() {
        return state == LobbyState.OPEN && availableSeats() > 0;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Lobby lobby && id.equals(lobby.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}

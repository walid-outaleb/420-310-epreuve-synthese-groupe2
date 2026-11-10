package com.cegepmv.game.lobby.application;

import com.cegepmv.game.lobby.domain.Lobby;
import com.cegepmv.game.lobby.domain.Member;

public class LobbyOutputMapper {

    public LobbyOutput toOutput(Lobby lobby) {
        return new LobbyOutput(
                lobby.id().value(),
                lobby.state().name(),
                lobby.hostId().value(),
                lobby.members().stream().map(this::toOutput).toList(),
                lobby.availableSeats());
    }

    public LobbySummaryOutput toSummary(Lobby lobby) {
        return new LobbySummaryOutput(
                lobby.id().value(),
                lobby.hostId().value(),
                lobby.members().size(),
                Lobby.MAXIMUM_PLAYERS);
    }

    private MemberOutput toOutput(Member member) {
        return new MemberOutput(member.playerId().value(), member.pseudonym().value());
    }
}

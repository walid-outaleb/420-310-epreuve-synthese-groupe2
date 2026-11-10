package com.cegepmv.game.lobby.api;

import com.cegepmv.game.lobby.application.LobbyOutput;
import java.util.List;

public final class LobbyResponse {

    public final String id;
    public final String state;
    public final String hostId;
    public final List<MemberResponse> players;
    public final int availableSeats;

    private LobbyResponse(LobbyOutput output) {
        this.id = output.id();
        this.state = output.state();
        this.hostId = output.hostId();
        this.players =
                output.players().stream()
                        .map(member -> new MemberResponse(member.playerId(), member.pseudonym()))
                        .toList();
        this.availableSeats = output.availableSeats();
    }

    public static LobbyResponse from(LobbyOutput output) {
        return new LobbyResponse(output);
    }
}

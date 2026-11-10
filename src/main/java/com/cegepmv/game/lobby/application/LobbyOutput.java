package com.cegepmv.game.lobby.application;

import java.util.List;

public record LobbyOutput(
        String id, String state, String hostId, List<MemberOutput> players, int availableSeats) {}

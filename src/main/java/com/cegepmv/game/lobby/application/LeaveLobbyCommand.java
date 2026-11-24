package com.cegepmv.game.lobby.application;

public record LeaveLobbyCommand(String lobbyId, String playerId, String requesterId) {}

package com.cegepmv.game.lobby.infrastructure;

import java.util.List;

public record LobbyPersistenceModel(String id, String state, List<MemberPersistenceModel> members) {}

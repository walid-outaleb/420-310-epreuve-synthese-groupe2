package com.cegepmv.game.lobby.infrastructure;

import com.cegepmv.game.lobby.domain.Lobby;
import com.cegepmv.game.lobby.domain.LobbyRepository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryLobbyRepository implements LobbyRepository {

    private final Map<String, LobbyPersistenceModel> lobbies = new ConcurrentHashMap<>();
    private final LobbyPersistenceMapper persistenceMapper;

    public InMemoryLobbyRepository(LobbyPersistenceMapper persistenceMapper) {
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void save(Lobby lobby) {
        LobbyPersistenceModel model = persistenceMapper.toPersistence(lobby);
        lobbies.put(model.id(), model);
    }
}

package com.cegepmv.game.lobby.infrastructure;

import com.cegepmv.game.lobby.domain.Lobby;
import com.cegepmv.game.lobby.domain.LobbyId;
import com.cegepmv.game.lobby.domain.LobbyRepository;
import com.cegepmv.game.lobby.domain.LobbyState;
import com.cegepmv.game.lobby.domain.PlayerId;
import com.cegepmv.game.lobby.domain.exception.LobbyNotFoundException;
import java.util.List;
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

    @Override
    public Lobby fetchById(LobbyId id) {
        LobbyPersistenceModel model = lobbies.get(id.value());
        if (model == null || model.state().equals(LobbyState.CLOSED.name())) {
            throw new LobbyNotFoundException();
        }
        return persistenceMapper.toDomain(model);
    }

    @Override
    public List<Lobby> findAllAcceptingPlayers() {
        return lobbies.values().stream()
                .map(persistenceMapper::toDomain)
                .filter(Lobby::acceptsPlayers)
                .toList();
    }

    @Override
    public boolean isMemberOfOpenLobby(PlayerId playerId) {
        return lobbies.values().stream()
                .filter(lobby -> lobby.state().equals(LobbyState.OPEN.name()))
                .flatMap(lobby -> lobby.members().stream())
                .anyMatch(member -> member.playerId().equals(playerId.value()));
    }
}

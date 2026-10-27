package com.cegepmv.game.player.infrastructure;

import com.cegepmv.game.player.domain.Player;
import com.cegepmv.game.player.domain.PlayerRepository;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryPlayerRepository implements PlayerRepository {
    private final ConcurrentMap<UUID, Player> players = new ConcurrentHashMap<>();

    @Override
    public void save(Player player) {
        if (players.putIfAbsent(player.id(), player) != null) {
            throw new IllegalStateException("Duplicate player identifier");
        }
    }

    @Override
    public Optional<Player> findById(UUID id) {
        return Optional.ofNullable(players.get(id));
    }
}

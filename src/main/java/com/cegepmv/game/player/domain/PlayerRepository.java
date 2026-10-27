package com.cegepmv.game.player.domain;

import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository {
    void save(Player player);
    Optional<Player> findById(UUID id);
}

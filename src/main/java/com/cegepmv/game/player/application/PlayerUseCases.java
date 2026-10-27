package com.cegepmv.game.player.application;

import com.cegepmv.game.player.domain.Player;
import com.cegepmv.game.player.domain.PlayerRepository;
import java.util.Optional;
import java.util.UUID;

public class PlayerUseCases {
    private final PlayerRepository players;

    public PlayerUseCases(PlayerRepository players) {
        this.players = players;
    }

    public UUID create() {
        Player player = Player.create();
        players.save(player);
        return player.id();
    }

    public Optional<UUID> find(UUID id) {
        return players.findById(id).map(Player::id);
    }
}

package com.cegepmv.game.player;

import com.cegepmv.game.player.api.PlayerResource;
import com.cegepmv.game.player.application.PlayerUseCases;
import com.cegepmv.game.player.infrastructure.InMemoryPlayerRepository;
import org.glassfish.jersey.server.ResourceConfig;

public class PlayerInstaller {
    public void install(ResourceConfig resourceConfig) {
        resourceConfig.register(new PlayerResource(new PlayerUseCases(new InMemoryPlayerRepository())));
    }
}

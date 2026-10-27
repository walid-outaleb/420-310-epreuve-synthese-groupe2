package com.cegepmv.game.lobby;

import com.cegepmv.game.lobby.api.LobbyExceptionMapper;
import com.cegepmv.game.lobby.api.LobbyResource;
import com.cegepmv.game.lobby.application.CreateLobbyUseCase;
import com.cegepmv.game.lobby.application.LobbyOutputMapper;
import com.cegepmv.game.lobby.domain.LobbyFactory;
import com.cegepmv.game.lobby.domain.LobbyRepository;
import com.cegepmv.game.lobby.infrastructure.InMemoryLobbyRepository;
import com.cegepmv.game.lobby.infrastructure.LobbyPersistenceMapper;
import org.glassfish.jersey.server.ResourceConfig;

public class LobbyInstaller {

    private final LobbyRepository lobbyRepository;

    public LobbyInstaller() {
        this.lobbyRepository = new InMemoryLobbyRepository(new LobbyPersistenceMapper());
    }

    public void install(ResourceConfig resourceConfig) {
        CreateLobbyUseCase createLobbyUseCase =
                new CreateLobbyUseCase(new LobbyFactory(), lobbyRepository, new LobbyOutputMapper());

        resourceConfig.register(new LobbyResource(createLobbyUseCase));
        resourceConfig.register(new LobbyExceptionMapper());
    }
}

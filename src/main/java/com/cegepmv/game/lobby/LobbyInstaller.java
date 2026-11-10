package com.cegepmv.game.lobby;

import com.cegepmv.game.lobby.api.LobbyExceptionMapper;
import com.cegepmv.game.lobby.api.LobbyResource;
import com.cegepmv.game.lobby.api.NotAuthenticatedExceptionMapper;
import com.cegepmv.game.lobby.api.PlayerAuthentication;
import com.cegepmv.game.lobby.api.PlayerHeaderAuthentication;
import com.cegepmv.game.lobby.application.CreateLobbyUseCase;
import com.cegepmv.game.lobby.application.GetLobbyUseCase;
import com.cegepmv.game.lobby.application.JoinLobbyUseCase;
import com.cegepmv.game.lobby.application.LeaveLobbyUseCase;
import com.cegepmv.game.lobby.application.ListOpenLobbiesUseCase;
import com.cegepmv.game.lobby.application.LobbyOutputMapper;
import com.cegepmv.game.lobby.domain.LobbyFactory;
import com.cegepmv.game.lobby.domain.LobbyRepository;
import com.cegepmv.game.lobby.domain.PlayerGateway;
import com.cegepmv.game.lobby.infrastructure.InMemoryLobbyRepository;
import com.cegepmv.game.lobby.infrastructure.LobbyPersistenceMapper;
import com.cegepmv.game.lobby.infrastructure.PlayerIdAsPseudonymGateway;
import org.glassfish.jersey.server.ResourceConfig;

public class LobbyInstaller {

    private final LobbyRepository lobbyRepository;
    private final PlayerGateway playerGateway;
    private final PlayerAuthentication playerAuthentication;

    public LobbyInstaller() {
        this.lobbyRepository = new InMemoryLobbyRepository(new LobbyPersistenceMapper());
        this.playerGateway = new PlayerIdAsPseudonymGateway();
        this.playerAuthentication = new PlayerHeaderAuthentication();
    }

    public void install(ResourceConfig resourceConfig) {
        LobbyOutputMapper outputMapper = new LobbyOutputMapper();
        CreateLobbyUseCase createLobbyUseCase =
                new CreateLobbyUseCase(new LobbyFactory(), lobbyRepository, playerGateway, outputMapper);
        ListOpenLobbiesUseCase listOpenLobbiesUseCase =
                new ListOpenLobbiesUseCase(lobbyRepository, outputMapper);
        GetLobbyUseCase getLobbyUseCase = new GetLobbyUseCase(lobbyRepository, outputMapper);
        JoinLobbyUseCase joinLobbyUseCase =
                new JoinLobbyUseCase(lobbyRepository, playerGateway, outputMapper);
        LeaveLobbyUseCase leaveLobbyUseCase = new LeaveLobbyUseCase(lobbyRepository);

        resourceConfig.register(
                new LobbyResource(
                        createLobbyUseCase,
                        listOpenLobbiesUseCase,
                        getLobbyUseCase,
                        joinLobbyUseCase,
                        leaveLobbyUseCase,
                        playerAuthentication));
        resourceConfig.register(new LobbyExceptionMapper());
        resourceConfig.register(new NotAuthenticatedExceptionMapper());
    }
}

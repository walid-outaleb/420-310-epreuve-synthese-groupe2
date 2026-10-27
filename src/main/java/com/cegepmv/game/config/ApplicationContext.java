package com.cegepmv.game.config;

import com.cegepmv.game.lobby.LobbyInstaller;
import com.cegepmv.game.player.PlayerInstaller;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

public final class ApplicationContext {

    private ApplicationContext() {}

    public static ResourceConfig createResourceConfig() {
        ResourceConfig resourceConfig =
                new ResourceConfig().property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        new LobbyInstaller().install(resourceConfig);
        new PlayerInstaller().install(resourceConfig);

        return resourceConfig;
    }
}

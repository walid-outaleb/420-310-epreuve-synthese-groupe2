package com.cegepmv.game.lobby.api;

import com.cegepmv.game.lobby.application.CreateLobbyCommand;
import com.cegepmv.game.lobby.application.CreateLobbyUseCase;
import com.cegepmv.game.lobby.application.LobbyOutput;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/lobbies")
@Produces(MediaType.APPLICATION_JSON)
public class LobbyResource {

    private final CreateLobbyUseCase createLobbyUseCase;

    public LobbyResource(CreateLobbyUseCase createLobbyUseCase) {
        this.createLobbyUseCase = createLobbyUseCase;
    }

    @POST
    public Response createLobby(
            @HeaderParam("Player") @NotNull String playerId, @Context UriInfo uriInfo) {
        CreateLobbyCommand command = new CreateLobbyCommand(playerId);

        LobbyOutput lobby = createLobbyUseCase.execute(command);

        URI location = uriInfo.getAbsolutePathBuilder().path(lobby.id()).build();
        return Response.created(location).build();
    }
}

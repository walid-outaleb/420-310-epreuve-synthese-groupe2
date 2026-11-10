package com.cegepmv.game.lobby.api;

import com.cegepmv.game.lobby.application.CreateLobbyCommand;
import com.cegepmv.game.lobby.application.CreateLobbyUseCase;
import com.cegepmv.game.lobby.application.GetLobbyQuery;
import com.cegepmv.game.lobby.application.GetLobbyUseCase;
import com.cegepmv.game.lobby.application.JoinLobbyCommand;
import com.cegepmv.game.lobby.application.JoinLobbyUseCase;
import com.cegepmv.game.lobby.application.LeaveLobbyCommand;
import com.cegepmv.game.lobby.application.LeaveLobbyUseCase;
import com.cegepmv.game.lobby.application.ListOpenLobbiesUseCase;
import com.cegepmv.game.lobby.application.LobbyOutput;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/lobbies")
@Produces(MediaType.APPLICATION_JSON)
public class LobbyResource {

    private final CreateLobbyUseCase createLobbyUseCase;
    private final ListOpenLobbiesUseCase listOpenLobbiesUseCase;
    private final GetLobbyUseCase getLobbyUseCase;
    private final JoinLobbyUseCase joinLobbyUseCase;
    private final LeaveLobbyUseCase leaveLobbyUseCase;
    private final PlayerAuthentication playerAuthentication;

    public LobbyResource(
            CreateLobbyUseCase createLobbyUseCase,
            ListOpenLobbiesUseCase listOpenLobbiesUseCase,
            GetLobbyUseCase getLobbyUseCase,
            JoinLobbyUseCase joinLobbyUseCase,
            LeaveLobbyUseCase leaveLobbyUseCase,
            PlayerAuthentication playerAuthentication) {
        this.createLobbyUseCase = createLobbyUseCase;
        this.listOpenLobbiesUseCase = listOpenLobbiesUseCase;
        this.getLobbyUseCase = getLobbyUseCase;
        this.joinLobbyUseCase = joinLobbyUseCase;
        this.leaveLobbyUseCase = leaveLobbyUseCase;
        this.playerAuthentication = playerAuthentication;
    }

    @POST
    public Response createLobby(@Context HttpHeaders headers, @Context UriInfo uriInfo) {
        String playerId = playerAuthentication.authenticate(headers);

        LobbyOutput lobby = createLobbyUseCase.execute(new CreateLobbyCommand(playerId));

        URI location = uriInfo.getAbsolutePathBuilder().path(lobby.id()).build();
        return Response.created(location).entity(LobbyResponse.from(lobby)).build();
    }

    @GET
    public Response listLobbies(@Context HttpHeaders headers) {
        playerAuthentication.authenticate(headers);

        List<LobbySummaryResponse> lobbies =
                listOpenLobbiesUseCase.execute().stream().map(LobbySummaryResponse::from).toList();

        return Response.ok(lobbies).build();
    }

    @GET
    @Path("/{lobbyId}")
    public Response getLobby(@PathParam("lobbyId") String lobbyId, @Context HttpHeaders headers) {
        playerAuthentication.authenticate(headers);

        LobbyOutput lobby = getLobbyUseCase.execute(new GetLobbyQuery(lobbyId));

        return Response.ok(LobbyResponse.from(lobby)).build();
    }

    @POST
    @Path("/{lobbyId}/players")
    public Response joinLobby(
            @PathParam("lobbyId") String lobbyId,
            @Context HttpHeaders headers,
            @Context UriInfo uriInfo) {
        String playerId = playerAuthentication.authenticate(headers);

        LobbyOutput lobby = joinLobbyUseCase.execute(new JoinLobbyCommand(lobbyId, playerId));

        URI location = uriInfo.getAbsolutePathBuilder().path(playerId).build();
        return Response.created(location).entity(LobbyResponse.from(lobby)).build();
    }

    @DELETE
    @Path("/{lobbyId}/players/{playerId}")
    public Response leaveLobby(
            @PathParam("lobbyId") String lobbyId,
            @PathParam("playerId") String playerId,
            @Context HttpHeaders headers) {
        String requesterId = playerAuthentication.authenticate(headers);

        leaveLobbyUseCase.execute(new LeaveLobbyCommand(lobbyId, playerId, requesterId));

        return Response.noContent().build();
    }
}

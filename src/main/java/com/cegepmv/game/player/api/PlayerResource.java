package com.cegepmv.game.player.api;

import com.cegepmv.game.player.application.PlayerUseCases;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.util.Map;
import java.util.UUID;

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {
    private final PlayerUseCases players;

    public PlayerResource(PlayerUseCases players) {
        this.players = players;
    }

    @POST
    public Response create(String body, @Context UriInfo uriInfo) {
        if (body != null && !body.isBlank()) {
            return error(400, "La création d'un joueur ne prend pas de corps de requête.");
        }
        UUID id = players.create();
        return Response.created(uriInfo.getAbsolutePathBuilder().path(id.toString()).build()).build();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String rawId) {
        UUID id;
        try {
            id = UUID.fromString(rawId);
            if (!id.toString().equalsIgnoreCase(rawId)) {
                return error(400, "Identifiant de joueur invalide.");
            }
        } catch (IllegalArgumentException exception) {
            return error(400, "Identifiant de joueur invalide.");
        }
        return players.find(id)
                .map(playerId -> Response.ok(Map.of("id", playerId.toString())).build())
                .orElseGet(() -> error(404, "Joueur introuvable."));
    }

    private static Response error(int status, String message) {
        return Response.status(status).type(MediaType.APPLICATION_JSON)
                .entity(Map.of("message", message)).build();
    }
}

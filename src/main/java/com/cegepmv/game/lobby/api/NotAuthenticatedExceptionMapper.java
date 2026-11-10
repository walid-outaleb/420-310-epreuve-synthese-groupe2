package com.cegepmv.game.lobby.api;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotAuthenticatedExceptionMapper implements ExceptionMapper<NotAuthenticatedException> {

    private static final int UNAUTHORIZED = 401;

    @Override
    public Response toResponse(NotAuthenticatedException exception) {
        return Response.status(UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse("NOT_AUTHENTICATED", "Le joueur n'est pas authentifié."))
                .build();
    }
}

package com.cegepmv.game.lobby.api;

import com.cegepmv.game.lobby.domain.exception.LobbyException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LobbyExceptionMapper implements ExceptionMapper<LobbyException> {

    private static final int UNPROCESSABLE_CONTENT = 422;

    @Override
    public Response toResponse(LobbyException exception) {
        return Response.status(UNPROCESSABLE_CONTENT)
                .type(MediaType.TEXT_PLAIN)
                .entity(exception.getMessage())
                .build();
    }
}

package com.cegepmv.game.lobby.api;

import com.cegepmv.game.lobby.domain.exception.AlreadyInLobbyException;
import com.cegepmv.game.lobby.domain.exception.AlreadyMemberException;
import com.cegepmv.game.lobby.domain.exception.CannotRemoveAnotherPlayerException;
import com.cegepmv.game.lobby.domain.exception.GameAlreadyStartedException;
import com.cegepmv.game.lobby.domain.exception.LobbyException;
import com.cegepmv.game.lobby.domain.exception.LobbyFullException;
import com.cegepmv.game.lobby.domain.exception.LobbyNotFoundException;
import com.cegepmv.game.lobby.domain.exception.PlayerNotInLobbyException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class LobbyExceptionMapper implements ExceptionMapper<LobbyException> {

    private static final int UNPROCESSABLE_CONTENT = 422;
    private static final Map<Class<? extends LobbyException>, Integer> STATUS_BY_EXCEPTION =
            Map.of(
                    AlreadyInLobbyException.class, 409,
                    AlreadyMemberException.class, 409,
                    LobbyFullException.class, 409,
                    GameAlreadyStartedException.class, 409,
                    LobbyNotFoundException.class, 404,
                    PlayerNotInLobbyException.class, 404,
                    CannotRemoveAnotherPlayerException.class, 403);

    @Override
    public Response toResponse(LobbyException exception) {
        return Response.status(STATUS_BY_EXCEPTION.getOrDefault(exception.getClass(), UNPROCESSABLE_CONTENT))
                .type(MediaType.APPLICATION_JSON)
                .entity(new ErrorResponse(exception.code(), exception.getMessage()))
                .build();
    }
}

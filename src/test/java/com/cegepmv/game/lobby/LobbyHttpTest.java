package com.cegepmv.game.lobby;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.cegepmv.game.config.ApplicationContext;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LobbyHttpTest {
    private static HttpServer server;
    private static URI lobbiesUri;
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    @BeforeAll
    static void startServer() {
        server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create("http://127.0.0.1:0/"), ApplicationContext.createResourceConfig());
        int port = server.getListener("grizzly").getPort();
        lobbiesUri = URI.create("http://127.0.0.1:" + port + "/lobbies");
    }

    @AfterAll
    static void stopServer() {
        if (server != null) {
            server.shutdownNow();
        }
    }

    @Test
    void creatingLobbiesReturnsCreatedWithDistinctLocations() throws Exception {
        HttpResponse<String> first = createLobby("akli2006");
        HttpResponse<String> second = createLobby("akli2006");

        assertEquals(201, first.statusCode());
        assertEquals(201, second.statusCode());
        URI firstLocation = URI.create(first.headers().firstValue("Location").orElseThrow());
        URI secondLocation = URI.create(second.headers().firstValue("Location").orElseThrow());
        assertTrue(firstLocation.getPath().startsWith("/lobbies/"));
        UUID.fromString(firstLocation.getPath().substring("/lobbies/".length()));
        assertNotEquals(firstLocation, secondLocation);
    }

    @Test
    void missingPlayerHeaderIsRejected() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(lobbiesUri)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }

    @Test
    void emptyPlayerHeaderIsRejected() throws Exception {
        HttpResponse<String> response = createLobby("");

        assertEquals(422, response.statusCode());
        assertEquals("Host id cannot be blank.", response.body());
    }

    private static HttpResponse<String> createLobby(String playerId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(lobbiesUri)
                .header("Player", playerId)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }
}

package com.cegepmv.game.player;

import static org.junit.jupiter.api.Assertions.*;

import com.cegepmv.game.config.ApplicationContext;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.time.Duration;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PlayerHttpTest {
    private static HttpServer server;
    private static URI playersUri;
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(5)).build();

    @BeforeAll
    static void startServer() {
        server = GrizzlyHttpServerFactory.createHttpServer(
                URI.create("http://127.0.0.1:0/"), ApplicationContext.createResourceConfig());
        playersUri = URI.create("http://127.0.0.1:" + server.getListener("grizzly").getPort() + "/players");
    }

    @AfterAll
    static void stopServer() {
        if (server != null) server.shutdownNow();
    }

    @Test
    void createdPlayersHaveUniqueLocationsAndCanBeRead() throws Exception {
        HttpResponse<String> first = createPlayer();
        HttpResponse<String> second = createPlayer();
        assertEquals(201, first.statusCode());
        assertEquals(201, second.statusCode());
        URI firstLocation = URI.create(first.headers().firstValue("Location").orElseThrow());
        URI secondLocation = URI.create(second.headers().firstValue("Location").orElseThrow());
        assertNotEquals(firstLocation, secondLocation);
        assertTrue(firstLocation.getPath().startsWith("/players/"));
        String id = firstLocation.getPath().substring("/players/".length());
        UUID.fromString(id);
        HttpResponse<String> loaded = get(firstLocation);
        assertEquals(200, loaded.statusCode());
        assertTrue(loaded.body().contains(id));
        assertEquals(200, get(secondLocation).statusCode());
        assertEquals(200, get(firstLocation).statusCode());
    }

    @Test
    void unknownPlayerReturnsNotFound() throws Exception {
        HttpResponse<String> response = get(playersUri.resolve("/players/" + UUID.randomUUID()));
        assertEquals(404, response.statusCode());
        assertFalse(response.body().isBlank());
        assertFalse(response.body().contains("Exception"));
    }

    @Test
    void malformedIdentifierReturnsBadRequest() throws Exception {
        HttpResponse<String> response = get(playersUri.resolve("/players/not-a-uuid"));
        assertEquals(400, response.statusCode());
        assertFalse(response.body().isBlank());
        assertFalse(response.body().contains("Exception"));
    }

    @Test
    void unexpectedCreationBodyIsRejected() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(playersUri).timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{invalid"))
                .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
        assertFalse(response.body().isBlank());
    }

    private static HttpResponse<String> createPlayer() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(playersUri).timeout(Duration.ofSeconds(5))
                .POST(HttpRequest.BodyPublishers.noBody()).build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> get(URI uri) throws Exception {
        return CLIENT.send(HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(5)).GET().build(), HttpResponse.BodyHandlers.ofString());
    }
}

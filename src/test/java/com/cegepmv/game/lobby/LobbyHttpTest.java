package com.cegepmv.game.lobby;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void givenPlayer_whenCreatingLobby_thenReturnsCreatedWithLocationAndLobbyRepresentation() throws Exception {
        String playerId = newPlayerId();

        HttpResponse<String> response = createLobby(playerId);

        assertEquals(201, response.statusCode());
        URI location = URI.create(response.headers().firstValue("Location").orElseThrow());
        assertTrue(location.getPath().startsWith("/lobbies/"));
        String lobbyId = location.getPath().substring("/lobbies/".length());
        assertTrue(response.body().contains("\"id\":\"" + lobbyId + "\""));
        assertTrue(response.body().contains("\"state\":\"OPEN\""));
        assertTrue(response.body().contains("\"hostId\":\"" + playerId + "\""));
        assertTrue(response.body().contains("\"availableSeats\":3"));
        assertTrue(response.body().contains("\"hero\":null"));
    }

    @Test
    void givenPlayerAlreadyInOpenLobby_whenCreatingLobby_thenReturnsConflict() throws Exception {
        String playerId = newPlayerId();
        createLobby(playerId);

        HttpResponse<String> response = createLobby(playerId);

        assertEquals(409, response.statusCode());
        assertTrue(response.body().contains("\"error\":\"ALREADY_IN_LOBBY\""));
        assertTrue(response.body().contains("\"description\""));
    }

    @Test
    void givenNoPlayerHeader_whenCreatingLobby_thenReturnsNotAuthenticated() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(lobbiesUri)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(401, response.statusCode());
        assertTrue(response.body().contains("\"error\":\"NOT_AUTHENTICATED\""));
    }

    @Test
    void givenCreatedLobby_whenListingLobbies_thenItAppearsWithPlayerCount() throws Exception {
        String playerId = newPlayerId();
        String lobbyId = lobbyIdOf(createLobby(playerId));

        HttpResponse<String> response = get(lobbiesUri, playerId);

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"id\":\"" + lobbyId + "\""));
        assertTrue(response.body().contains("\"playerCount\":1"));
        assertTrue(response.body().contains("\"maxPlayers\":4"));
    }

    @Test
    void givenCreatedLobby_whenGettingIt_thenReturnsItsRepresentation() throws Exception {
        String playerId = newPlayerId();
        String lobbyId = lobbyIdOf(createLobby(playerId));

        HttpResponse<String> response = get(URI.create(lobbiesUri + "/" + lobbyId), newPlayerId());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"id\":\"" + lobbyId + "\""));
        assertTrue(response.body().contains("\"hostId\":\"" + playerId + "\""));
        assertTrue(!response.body().contains("email"));
    }

    @Test
    void givenUnknownLobby_whenGettingIt_thenReturnsNotFound() throws Exception {
        HttpResponse<String> response =
                get(URI.create(lobbiesUri + "/" + UUID.randomUUID()), newPlayerId());

        assertEquals(404, response.statusCode());
        assertTrue(response.body().contains("\"error\":\"LOBBY_NOT_FOUND\""));
    }

    @Test
    void givenNoPlayerHeader_whenListingLobbies_thenReturnsNotAuthenticated() throws Exception {
        HttpRequest request = HttpRequest.newBuilder(lobbiesUri).GET().build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(401, response.statusCode());
    }

    @Test
    void givenOpenLobby_whenAnotherPlayerJoins_thenReturnsCreatedWithLocationAndTwoPlayers() throws Exception {
        String lobbyId = lobbyIdOf(createLobby(newPlayerId()));
        String newcomerId = newPlayerId();

        HttpResponse<String> response = join(lobbyId, newcomerId);

        assertEquals(201, response.statusCode());
        assertEquals(
                "/lobbies/" + lobbyId + "/players/" + newcomerId,
                URI.create(response.headers().firstValue("Location").orElseThrow()).getPath());
        assertTrue(response.body().contains("\"availableSeats\":2"));
        assertTrue(response.body().contains("\"playerId\":\"" + newcomerId + "\""));
    }

    @Test
    void givenFullLobby_whenFifthPlayerJoins_thenReturnsLobbyFull() throws Exception {
        String lobbyId = lobbyIdOf(createLobby(newPlayerId()));
        join(lobbyId, newPlayerId());
        join(lobbyId, newPlayerId());
        join(lobbyId, newPlayerId());

        HttpResponse<String> response = join(lobbyId, newPlayerId());

        assertEquals(409, response.statusCode());
        assertTrue(response.body().contains("\"error\":\"LOBBY_FULL\""));
    }

    @Test
    void givenMember_whenJoiningSameLobbyAgain_thenReturnsAlreadyMember() throws Exception {
        String lobbyId = lobbyIdOf(createLobby(newPlayerId()));
        String playerId = newPlayerId();
        join(lobbyId, playerId);

        HttpResponse<String> response = join(lobbyId, playerId);

        assertEquals(409, response.statusCode());
        assertTrue(response.body().contains("\"error\":\"ALREADY_MEMBER\""));
    }

    @Test
    void givenMemberOfAnotherLobby_whenJoining_thenReturnsAlreadyInLobby() throws Exception {
        String playerId = newPlayerId();
        createLobby(playerId);
        String otherLobbyId = lobbyIdOf(createLobby(newPlayerId()));

        HttpResponse<String> response = join(otherLobbyId, playerId);

        assertEquals(409, response.statusCode());
        assertTrue(response.body().contains("\"error\":\"ALREADY_IN_LOBBY\""));
    }

    @Test
    void givenUnknownLobby_whenJoining_thenReturnsNotFound() throws Exception {
        HttpResponse<String> response = join(UUID.randomUUID().toString(), newPlayerId());

        assertEquals(404, response.statusCode());
        assertTrue(response.body().contains("\"error\":\"LOBBY_NOT_FOUND\""));
    }

    @Test
    void givenMember_whenLeaving_thenReturnsNoContentAndSeatIsFreed() throws Exception {
        String lobbyId = lobbyIdOf(createLobby(newPlayerId()));
        String playerId = newPlayerId();
        join(lobbyId, playerId);

        HttpResponse<String> response = leave(lobbyId, playerId, playerId);

        assertEquals(204, response.statusCode());
        assertTrue(get(URI.create(lobbiesUri + "/" + lobbyId), playerId).body().contains("\"availableSeats\":3"));
    }

    @Test
    void givenAnotherPlayer_whenRemovingSomeone_thenReturnsForbidden() throws Exception {
        String hostId = newPlayerId();
        String lobbyId = lobbyIdOf(createLobby(hostId));
        String playerId = newPlayerId();
        join(lobbyId, playerId);

        HttpResponse<String> response = leave(lobbyId, playerId, hostId);

        assertEquals(403, response.statusCode());
        assertTrue(response.body().contains("\"error\":\"FORBIDDEN\""));
    }

    @Test
    void givenNonMember_whenLeaving_thenReturnsPlayerNotInLobby() throws Exception {
        String lobbyId = lobbyIdOf(createLobby(newPlayerId()));
        String strangerId = newPlayerId();

        HttpResponse<String> response = leave(lobbyId, strangerId, strangerId);

        assertEquals(404, response.statusCode());
        assertTrue(response.body().contains("\"error\":\"PLAYER_NOT_IN_LOBBY\""));
    }

    @Test
    void givenLastMember_whenLeaving_thenLobbyDisappears() throws Exception {
        String hostId = newPlayerId();
        String lobbyId = lobbyIdOf(createLobby(hostId));

        leave(lobbyId, hostId, hostId);

        assertEquals(404, get(URI.create(lobbiesUri + "/" + lobbyId), hostId).statusCode());
        assertTrue(!get(lobbiesUri, hostId).body().contains(lobbyId));
    }

    @Test
    void givenHostLeaves_whenAnotherPlayerRemains_thenHeBecomesHost() throws Exception {
        String hostId = newPlayerId();
        String lobbyId = lobbyIdOf(createLobby(hostId));
        String otherId = newPlayerId();
        join(lobbyId, otherId);

        leave(lobbyId, hostId, hostId);

        assertTrue(get(URI.create(lobbiesUri + "/" + lobbyId), otherId).body().contains("\"hostId\":\"" + otherId + "\""));
    }

    private static HttpResponse<String> leave(String lobbyId, String playerId, String requesterId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(lobbiesUri + "/" + lobbyId + "/players/" + playerId))
                .header("Player", requesterId)
                .DELETE()
                .build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> join(String lobbyId, String playerId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(lobbiesUri + "/" + lobbyId + "/players"))
                .header("Player", playerId)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static String lobbyIdOf(HttpResponse<String> createResponse) {
        String path = URI.create(createResponse.headers().firstValue("Location").orElseThrow()).getPath();
        return path.substring("/lobbies/".length());
    }

    private static HttpResponse<String> get(URI uri, String playerId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(uri).header("Player", playerId).GET().build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static String newPlayerId() {
        return UUID.randomUUID().toString();
    }

    private static HttpResponse<String> createLobby(String playerId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(lobbiesUri)
                .header("Player", playerId)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }
}

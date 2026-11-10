package com.cegepmv.game.lobby.infrastructure;

import com.cegepmv.game.lobby.domain.Lobby;
import com.cegepmv.game.lobby.domain.LobbyId;
import com.cegepmv.game.lobby.domain.LobbyState;
import com.cegepmv.game.lobby.domain.Member;
import com.cegepmv.game.lobby.domain.PlayerId;
import com.cegepmv.game.lobby.domain.Pseudonym;

public class LobbyPersistenceMapper {

    public LobbyPersistenceModel toPersistence(Lobby lobby) {
        return new LobbyPersistenceModel(
                lobby.id().value(),
                lobby.state().name(),
                lobby.members().stream().map(this::toPersistence).toList());
    }

    public Lobby toDomain(LobbyPersistenceModel model) {
        return new Lobby(
                new LobbyId(model.id()),
                LobbyState.valueOf(model.state()),
                model.members().stream().map(this::toDomain).toList());
    }

    private MemberPersistenceModel toPersistence(Member member) {
        return new MemberPersistenceModel(member.playerId().value(), member.pseudonym().value());
    }

    private Member toDomain(MemberPersistenceModel model) {
        return new Member(new PlayerId(model.playerId()), new Pseudonym(model.pseudonym()));
    }
}

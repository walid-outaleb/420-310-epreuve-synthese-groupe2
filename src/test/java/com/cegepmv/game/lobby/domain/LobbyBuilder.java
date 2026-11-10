package com.cegepmv.game.lobby.domain;

import java.util.ArrayList;
import java.util.List;

public class LobbyBuilder {

    private LobbyId id = LobbyId.generate();
    private LobbyState state = LobbyState.OPEN;
    private List<Member> members = memberList(1);

    public static LobbyBuilder aLobby() {
        return new LobbyBuilder();
    }

    public LobbyBuilder withId(LobbyId id) {
        this.id = id;
        return this;
    }

    public LobbyBuilder withState(LobbyState state) {
        this.state = state;
        return this;
    }

    public LobbyBuilder withMemberCount(int count) {
        this.members = memberList(count);
        return this;
    }

    public Lobby build() {
        return new Lobby(id, state, members);
    }

    public static Member memberNumber(int number) {
        return new Member(new PlayerId("p-" + number), new Pseudonym("Player" + number));
    }

    private static List<Member> memberList(int count) {
        List<Member> members = new ArrayList<>();
        for (int number = 1; number <= count; number++) {
            members.add(memberNumber(number));
        }
        return members;
    }
}

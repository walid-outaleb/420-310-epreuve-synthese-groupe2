package com.cegepmv.game.hero.domain;

import java.util.Objects;

public enum Species {
    HUMAN(1, 1, 1, 1, 1, 1),
    ELF(0, 2, 0, 1, 0, 0),
    DWARF(1, 0, 2, 0, 0, 0),
    ORC(2, 0, 1, -1, 0, 0);

    private final int strength;
    private final int dexterity;
    private final int constitution;
    private final int intelligence;
    private final int wisdom;
    private final int charisma;

    Species(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
        this.strength = strength;
        this.dexterity = dexterity;
        this.constitution = constitution;
        this.intelligence = intelligence;
        this.wisdom = wisdom;
        this.charisma = charisma;
    }

    public AbilityScores applyTo(InitialAbilityScores initial) {
        Objects.requireNonNull(initial, "Les caractéristiques initiales sont requises.");
        return new AbilityScores(
                initial.strength() + strength,
                initial.dexterity() + dexterity,
                initial.constitution() + constitution,
                initial.intelligence() + intelligence,
                initial.wisdom() + wisdom,
                initial.charisma() + charisma);
    }
}

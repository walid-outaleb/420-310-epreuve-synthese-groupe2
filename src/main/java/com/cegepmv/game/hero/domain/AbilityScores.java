package com.cegepmv.game.hero.domain;

/** Caractéristiques après application des ajustements d'espèce. */
public record AbilityScores(
        int strength, int dexterity, int constitution,
        int intelligence, int wisdom, int charisma) {
    private static final int MAXIMUM = 20;

    public AbilityScores {
        if (strength > MAXIMUM || dexterity > MAXIMUM || constitution > MAXIMUM
                || intelligence > MAXIMUM || wisdom > MAXIMUM || charisma > MAXIMUM) {
            throw new IllegalArgumentException("Une caractéristique ne peut pas dépasser 20.");
        }
    }

    public static int modifier(int value) {
        return Math.floorDiv(value - 10, 2);
    }
}

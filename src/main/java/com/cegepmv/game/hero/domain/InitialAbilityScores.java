package com.cegepmv.game.hero.domain;

/** Les valeurs choisies par le joueur, avant les ajustements d'espèce. */
public record InitialAbilityScores(
        int strength, int dexterity, int constitution,
        int intelligence, int wisdom, int charisma) {
    private static final int MINIMUM = 3;
    private static final int MAXIMUM = 18;

    public InitialAbilityScores {
        validate("Force", strength);
        validate("Dextérité", dexterity);
        validate("Constitution", constitution);
        validate("Intelligence", intelligence);
        validate("Sagesse", wisdom);
        validate("Charisme", charisma);
    }

    private static void validate(String ability, int value) {
        if (value < MINIMUM || value > MAXIMUM) {
            throw new IllegalArgumentException(ability + " doit être comprise entre 3 et 18.");
        }
    }
}

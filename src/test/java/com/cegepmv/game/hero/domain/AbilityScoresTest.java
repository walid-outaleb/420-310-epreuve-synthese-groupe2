package com.cegepmv.game.hero.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class AbilityScoresTest {
    @Test
    void appliesEachSpeciesAdjustments() {
        InitialAbilityScores initial = new InitialAbilityScores(10, 10, 10, 10, 10, 10);
        assertEquals(new AbilityScores(11, 11, 11, 11, 11, 11), Species.HUMAN.applyTo(initial));
        assertEquals(new AbilityScores(10, 12, 10, 11, 10, 10), Species.ELF.applyTo(initial));
        assertEquals(new AbilityScores(11, 10, 12, 10, 10, 10), Species.DWARF.applyTo(initial));
        assertEquals(new AbilityScores(12, 10, 11, 9, 10, 10), Species.ORC.applyTo(initial));
    }

    @Test
    void acceptsInitialBoundsAndRejectsEachOutOfRangeAbility() {
        new InitialAbilityScores(3, 18, 3, 18, 3, 18);
        assertThrows(IllegalArgumentException.class, () -> new InitialAbilityScores(2, 10, 10, 10, 10, 10));
        assertThrows(IllegalArgumentException.class, () -> new InitialAbilityScores(10, 19, 10, 10, 10, 10));
        assertThrows(IllegalArgumentException.class, () -> new InitialAbilityScores(10, 10, 2, 10, 10, 10));
        assertThrows(IllegalArgumentException.class, () -> new InitialAbilityScores(10, 10, 10, 19, 10, 10));
        assertThrows(IllegalArgumentException.class, () -> new InitialAbilityScores(10, 10, 10, 10, 2, 10));
        assertThrows(IllegalArgumentException.class, () -> new InitialAbilityScores(10, 10, 10, 10, 10, 19));
    }

    @Test
    void speciesBonusCanReachButNotExceedTwenty() {
        InitialAbilityScores maximum = new InitialAbilityScores(18, 18, 18, 18, 18, 18);
        assertEquals(20, Species.ELF.applyTo(maximum).dexterity());
        assertEquals(20, Species.DWARF.applyTo(maximum).constitution());
        assertEquals(20, Species.ORC.applyTo(maximum).strength());
        assertThrows(IllegalArgumentException.class, () -> new AbilityScores(21, 10, 10, 10, 10, 10));
    }

    @Test
    void modifiersRoundDownForEveryValueInTheRuleTable() {
        int[] expected = {-4, -3, -3, -2, -2, -1, -1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5};
        for (int value = 3; value <= 20; value++) {
            assertEquals(expected[value - 3], AbilityScores.modifier(value), "valeur " + value);
        }
    }
}

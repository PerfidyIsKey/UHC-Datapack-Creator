package commands;

import uhc.game.DifficultyId;

/**
 * Fluent builder for the Minecraft /difficulty command.
 * Allows setting a specific difficulty or querying the current difficulty (by calling build() immediately).
 */
public class Difficulty {
    private DifficultyId difficulty;

    /**
     * Private constructor to enforce use of static factory method.
     */
    private Difficulty() {}

    /**
     * Starts the construction of the /difficulty command.
     */
    public static Difficulty create() {
        return new Difficulty();
    }

    /**
     * Sets the difficulty level to be used in the command.
     * @param difficulty The desired DifficultyId (PEACEFUL, EASY, NORMAL, HARD).
     * @return The current builder instance.
     * @throws IllegalArgumentException if the provided difficulty is null.
     */
    public Difficulty difficulty(DifficultyId difficulty) {
        if (difficulty == null) {
            throw new IllegalArgumentException("DifficultyId cannot be null. Use .create().build() to query the current difficulty.");
        }
        this.difficulty = difficulty;
        return this;
    }

    /**
     * Builds the final /difficulty command string.
     * If no difficulty is set, the command defaults to querying the current difficulty (Java Edition behavior).
     * @return The complete command string, e.g., "difficulty hard" or "difficulty".
     */
    public String build() {
        StringBuilder sb = new StringBuilder("difficulty");

        // If 'difficulty' is null, the resulting command is just "difficulty", which queries the current difficulty.
        if (difficulty != null) {
            sb.append(" ").append(difficulty);
        }

        return sb.toString();
    }
}
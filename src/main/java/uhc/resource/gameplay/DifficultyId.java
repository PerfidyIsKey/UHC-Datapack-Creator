package uhc.resource.gameplay;

/**
 * ⚖️ **Difficulty Identifier Registry**
 * <p>
 * Defines the four available difficulty levels for the Minecraft engine.
 * This enum maps Java constants to the lowercase identifiers required by
 * the {@code /difficulty} command and server world settings.
 * </p>
 */
public enum DifficultyId {

    // --- 🏳️ Difficulty Definitions ---

    /** Mobs do not spawn; hunger does not deplete. */
    PEACEFUL,

    /** Reduced damage from mobs; starvation does not kill the player. */
    EASY,

    /** Standard survival experience; starvation reduces health to half a heart. */
    NORMAL,

    /** Increased mob damage; starvation can be fatal to the player. */
    HARD;

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the command-ready identifier for this difficulty.
     * <p><b>Example:</b> {@code DifficultyId.HARD.getCommandValue()} returns {@code "hard"}.</p>
     * * @return The lowercase string representation of the difficulty.
     */
    public String getCommandValue() {
        return this.name().toLowerCase();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely parses a difficulty identifier from a string.
     * <p><b>Error Catching:</b> This method handles null, empty, or invalid strings
     * by returning {@link #NORMAL} as a fail-safe default, ensuring the game
     * state remains valid even with corrupted configuration data.</p>
     * * @param input The raw string to parse (e.g., "hard" or "HARD").
     * @return The matching {@link DifficultyId}, or {@link #NORMAL} if no match is found.
     */
    public static DifficultyId fromString(String input) {
        if (input == null || input.isBlank()) {
            return NORMAL;
        }

        String target = input.toUpperCase().trim();
        try {
            return DifficultyId.valueOf(target);
        } catch (IllegalArgumentException | NullPointerException e) {
            // Logically catch any mistyped difficulty string and fallback
            return NORMAL;
        }
    }

    /**
     * Parses a difficulty based on its ordinal index.
     * <p><b>Error Catching:</b> Validates that the index is within the valid
     * range of the enum values to prevent {@link ArrayIndexOutOfBoundsException}.</p>
     * * @param index The ordinal position (0-3).
     * @return The matching {@link DifficultyId}.
     * @throws IllegalArgumentException if the index is outside the bounds of defined difficulties.
     */
    public static DifficultyId fromOrdinal(int index) {
        if (index < 0 || index >= values().length) {
            throw new IllegalArgumentException("Difficulty index " + index + " is out of bounds (0-3).");
        }
        return values()[index];
    }

    // --- 📝 Overrides ---

    /**
     * Returns the difficulty name in lowercase for direct command insertion.
     * <p><b>Logic:</b> Delegates to {@link #getCommandValue()} to maintain consistency.</p>
     * * @return The lowercase identifier (e.g., "peaceful").
     */
    @Override
    public String toString() {
        return getCommandValue();
    }
}
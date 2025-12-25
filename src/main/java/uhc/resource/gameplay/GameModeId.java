package uhc.resource.gameplay;

/**
 * 🎮 **Game Mode Identifier Registry**
 * <p>
 * Defines the four primary game modes available in Minecraft. This enum facilitates
 * synchronization between player NBT data (integer indices) and the Minecraft
 * command engine (lowercase identifiers).
 * </p>
 */
public enum GameModeId {

    // --- 🕹️ Game Mode Definitions ---

    /** Standard survival mode with health, hunger, and resource gathering. Index: 0. */
    SURVIVAL(0),

    /** Creative mode with flight, infinite resources, and invulnerability. Index: 1. */
    CREATIVE(1),

    /** Restricted survival mode where blocks cannot be broken or placed without tools. Index: 2. */
    ADVENTURE(2),

    /** Ghost mode where players are invisible, can fly through blocks, and cannot interact. Index: 3. */
    SPECTATOR(3);

    // --- ⚙️ State & Fields ---

    /** * The internal integer value used by Minecraft in NBT data (e.g., the {@code playerGameType} tag). */
    private final int index;

    // --- 🏗️ Constructor ---

    /**
     * Internal constructor for linking enum constants to NBT indices.
     * @param index The integer used in Minecraft's internal registry and data files.
     */
    GameModeId(int index) {
        this.index = index;
    }

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the NBT-compatible integer index for this game mode.
     * <p><b>Use case:</b> Writing to or reading from player {@code .dat} files or
     * packet data requiring a numeric {@code playerGameType}.</p>
     * @return The raw integer index (0-3).
     */
    public int index() {
        return index;
    }

    /**
     * Returns the required command argument string for the {@code /gamemode} command.
     * <p><b>Example:</b> {@code GameModeId.CREATIVE.getCommandName()} returns {@code "creative"}.</p>
     * @return The lowercase identifier.
     */
    public String getCommandName() {
        return this.name().toLowerCase();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a GameModeId from an NBT index.
     * <p><b>Error Catching:</b> Iterates through defined constants. If the index is
     * out of bounds or unrecognized (due to corrupted data), it defaults to
     * {@link #SURVIVAL} to ensure the player remains in a playable state.</p>
     * @param index The index to look up (typically 0 through 3).
     * @return The corresponding {@link GameModeId}, or {@link #SURVIVAL} as a fail-safe.
     */
    public static GameModeId fromIndex(int index) {
        for (GameModeId mode : values()) {
            if (mode.index == index) {
                return mode;
            }
        }
        // Fail-safe logic for unexpected data values
        return SURVIVAL;
    }

    /**
     * Parses a game mode from a string identifier.
     * <p><b>Error Catching:</b> Handles null or blank strings by defaulting to
     * {@link #SURVIVAL}. Uses a case-insensitive check to maintain resilience.</p>
     * @param input The raw string (e.g., "Spectator" or "creative").
     * @return The matching {@link GameModeId}, or {@link #SURVIVAL} if invalid.
     */
    public static GameModeId fromString(String input) {
        if (input == null || input.isBlank()) {
            return SURVIVAL;
        }

        String target = input.toUpperCase().trim();
        try {
            return GameModeId.valueOf(target);
        } catch (IllegalArgumentException e) {
            // Logically catch mistyped modes from configs or commands
            return SURVIVAL;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the lowercase identifier for direct command insertion.
     * @return The result of {@link #getCommandName()}.
     */
    @Override
    public String toString() {
        return getCommandName();
    }
}
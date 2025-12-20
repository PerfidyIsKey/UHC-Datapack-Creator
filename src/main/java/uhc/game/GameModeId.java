package uhc.game;

/**
 * Represents the four main game modes in Minecraft.
 * Stores the internal NBT index (0-3) and provides the
 * lowercase command argument via toString().
 */
public enum GameModeId {

    SURVIVAL(0),
    CREATIVE(1),
    ADVENTURE(2),
    SPECTATOR(3);

    private final int index;

    /**
     * @param index The internal integer used in NBT data (playerGameType).
     */
    GameModeId(int index) {
        this.index = index;
    }

    /**
     * Returns the NBT-compatible integer index for this game mode.
     * Use this for tags like "playerGameType".
     */
    public int index() {
        return index;
    }

    /**
     * Safely retrieves a GameModeId from an NBT index.
     * @param index The index to look up (0-3).
     * @return The corresponding GameModeId, or SURVIVAL as a fallback.
     */
    public static GameModeId fromIndex(int index) {
        for (GameModeId mode : values()) {
            if (mode.index == index) {
                return mode;
            }
        }
        return SURVIVAL; // Default fallback to prevent crashes
    }

    /**
     * Returns the required command argument string (e.g., "survival").
     */
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
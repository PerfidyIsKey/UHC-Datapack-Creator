package shared;

/**
 * Provides type-safe identifiers for standard NBT paths used in Minecraft data commands.
 * By mapping a Java constant (e.g., COLLAR_COLOR) to the exact NBT string (e.g., "CollarColor"),
 * this prevents command-breaking typos.
 */
public enum DataPathId {

    // Entity NBT Paths
    /** The path for a mob's CollarColor tag (used by tamed mobs like wolves). */
    COLLAR_COLOR("CollarColor"),

    /** The path for the mob's movement position (a list of three doubles). */
    POS("Pos"),

    /** The path for the owner UUID (a list of four integers) of a tamed mob. */
    OWNER("Owner"),

    /** The path for the entity's unique identifier (a list of four integers). */
    UUID("UUID"),

    /** The path for an entity's age in ticks (e.g., 'Age' for cows, sheep, etc.). */
    AGE("Age");

    private final String path;

    /**
     * Private constructor for the DataPathId enum.
     * @param path The exact string representation of the NBT tag path required by Minecraft.
     */
    DataPathId(String path) {
        this.path = path;
    }

    /**
     * Retrieves the exact NBT path string.
     * @return The path string (e.g., "CollarColor").
     */
    @Override
    public String toString() {
        return path;
    }
}
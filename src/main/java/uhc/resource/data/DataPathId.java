package uhc.resource.data;

import java.util.Objects;

/**
 * 📂 **NBT Data Path Registry**
 * <p>
 * Provides type-safe identifiers for standard NBT paths used in Minecraft
 * data commands and NBT compound manipulation.
 * </p>
 * <p>
 * By mapping Java constants to the exact, case-sensitive NBT strings used
 * by the game engine, this registry prevents command-breaking typos
 * during runtime data modification.
 * </p>
 */
public enum DataPathId {

    // --- 🐾 Entity NBT Paths ---

    /** * The path for a mob's collar color tag.
     * <p><b>Context:</b> Used by tamed wolves and cats to store dye color indices (0-15).</p>
     */
    COLLAR_COLOR("CollarColor"),

    /** * The path for the entity's world position.
     * <p><b>Context:</b> A list of three doubles {@code [x, y, z]}.</p>
     */
    POS("Pos"),

    /** * The path for a tamed mob's owner.
     * <p><b>Context:</b> In modern versions, this is stored as an {@code IntArray}
     * representing the owner's UUID.</p>
     */
    OWNER("Owner"),

    /** * The path for the entity's unique identifier.
     * <p><b>Context:</b> Stored as a 4-integer array (UUID-Least and UUID-Most combined).</p>
     */
    UUID("UUID"),

    /** * The path for an entity's biological age in ticks.
     * <p><b>Context:</b> Used by breedable mobs; negative values represent babies,
     * while 0 or higher represents adults.</p>
     */
    AGE("Age");

    // --- ⚙️ Internal State ---

    /** * The exact, case-sensitive string representation of the NBT tag path. */
    private final String path;

    // --- 🏗️ Constructor ---

    /**
     * Internal constructor for mapping constants to NBT path strings.
     * @param path The specific NBT key used by Minecraft's data engine.
     */
    DataPathId(String path) {
        this.path = Objects.requireNonNull(path, "NBT path string cannot be null.");
    }

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the exact NBT path string for use in {@code /data} commands.
     * <p><b>Example:</b> {@code DataPathId.COLLAR_COLOR.getPath()} returns {@code "CollarColor"}.</p>
     * @return The case-sensitive NBT key.
     */
    public String getPath() {
        return path;
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a DataPathId from a raw string identifier.
     * <p><b>Error Catching:</b> Performs a case-insensitive check against the
     * path strings. If the input is null or unrecognized, it returns {@code null}
     * rather than throwing an exception, allowing the caller to handle
     * custom NBT paths gracefully.</p>
     * @param rawPath The raw string (e.g., "pos", "Age").
     * @return The matching {@link DataPathId}, or {@code null} if not in registry.
     */
    public static DataPathId fromString(String rawPath) {
        if (rawPath == null || rawPath.isBlank()) {
            return null;
        }

        String target = rawPath.trim();
        for (DataPathId id : values()) {
            if (id.path.equalsIgnoreCase(target)) {
                return id;
            }
        }
        return null;
    }

    // --- 📝 Overrides ---

    /**
     * Returns the exact NBT path string for direct command insertion.
     * <p><b>Implementation:</b> Delegates to {@link #getPath()}.</p>
     * @return The result of {@link #getPath()}.
     */
    @Override
    public String toString() {
        return getPath();
    }
}
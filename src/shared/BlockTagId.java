package shared;

/**
 * An enumeration representing a fixed list of common Minecraft Block Tags.
 * <p>
 * Block Tags are used in commands like {@code /fill replace} (block predicates)
 * to specify criteria that matches any block belonging to the tag group.
 * The output string is prefixed with a hash symbol ({@code #}).
 * <p>
 * This class implements the {@link BlockIdentifier} interface.
 */
public enum BlockTagId implements BlockIdentifier {

    // --- Built-in Minecraft tags (using default 'minecraft' namespace) ---
    /** Matches all types of leaves. */
    LEAVES("leaves"),
    /** Matches all types of planks. */
    PLANKS("planks"),
    /** Matches all types of log blocks. */
    LOGS("logs"),

    // --- Custom tags (using specified namespace) ---
    /** A custom tag example in the 'uhc' namespace. */
    BLOCK_BEACON_LIGHT("uhc", "block_beacon_light");

    // The namespace component of the resource location (e.g., "minecraft").
    private final String namespace;
    // The name component of the resource location (e.g., "planks").
    private final String name;

    /**
     * Private constructor for tags with a specific, custom namespace.
     *
     * @param namespace The namespace component.
     * @param name The name component.
     */
    BlockTagId(String namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }

    /**
     * Private constructor for tags within the default {@code minecraft} namespace.
     *
     * @param name The name component.
     */
    BlockTagId(String name) {
        this.namespace = "minecraft";
        this.name = name;
    }

    /**
     * Constructs the fully qualified resource location string, prefixed with a hash symbol.
     *
     * @return The immutable block tag string (e.g., {@code #minecraft:planks}).
     */
    @Override
    public String getResourceLocation() {
        return "#" + namespace + ":" + name;
    }

    /**
     * Returns the fully qualified block tag string, suitable for direct insertion into commands.
     *
     * @return The immutable block tag string.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}
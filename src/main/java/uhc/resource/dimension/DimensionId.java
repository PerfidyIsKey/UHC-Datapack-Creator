package uhc.resource.dimension;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🌌 **Dimension Identifier**
 * <p>
 * Represents the resource location of a Minecraft dimension.
 * Used for teleportation, location checks, and world management.
 * </p>
 */
public enum DimensionId {
    /** 🌍 The main world where players usually start. */
    OVERWORLD("overworld"),

    /** 🔥 The hellish dimension filled with lava and fortresses. */
    NETHER("the_nether"),

    /** ✨ The final dimension and home of the Ender Dragon. */
    END("the_end");

    private final String path;
    private final String namespace;

    /**
     * Internal constructor using the default Minecraft namespace.
     * @param path The dimension name (e.g., "overworld").
     */
    DimensionId(String path) {
        this(path, DatapackConfig.MINECRAFT_NAMESPACE);
    }

    /**
     * Internal constructor for custom namespaces.
     * @param path The dimension name.
     * @param namespace The resource namespace.
     * @throws NullPointerException if path or namespace is null.
     */
    DimensionId(String path, String namespace) {
        this.path = Objects.requireNonNull(path, "Dimension path cannot be null.");
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null.");
    }

    /**
     * Safely retrieves a DimensionId from a path string.
     * @param path The path to look for (e.g., "the_nether").
     * @return The matching {@link DimensionId} or {@link #OVERWORLD} as a fallback.
     */
    public static DimensionId fromPath(String path) {
        if (path == null) return OVERWORLD;
        for (DimensionId id : values()) {
            if (id.path.equalsIgnoreCase(path)) {
                return id;
            }
        }
        return OVERWORLD;
    }

    /**
     * Combines the namespace and path into a standard Minecraft resource location.
     * @return A string formatted as {@code namespace:path} (e.g., "minecraft:overworld").
     */
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    /**
     * Returns the full resource location for use directly in commands.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}
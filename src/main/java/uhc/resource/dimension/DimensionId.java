package uhc.resource.dimension;

import uhc.core.DatapackConfig;
import uhc.resource.ResourceLocation;
import java.util.Objects;

/**
 * 🌌 **Dimension Identifier**
 * <p>
 * Represents the unique resource location for a Minecraft dimension.
 * This registry is used for teleportation logic, dimension-specific
 * predicates, and world management within the UHC engine.
 * </p>
 */
public enum DimensionId implements ResourceLocation {

    // --- 🪐 Dimension Constants ---

    /** * 🌍 **The Overworld**
     * <p>The primary dimension where players spawn and build.</p>
     */
    OVERWORLD("overworld"),

    /** * 🔥 **The Nether**
     * <p>A hellish dimension filled with lava, bastions, and fortresses.</p>
     */
    NETHER("the_nether"),

    /** * ✨ **The End**
     * <p>The final frontier and home of the Ender Dragon and End Cities.</p>
     */
    END("the_end");

    // --- ⚙️ Internal State ---

    /** * The specific identifier for the dimension (e.g., "the_nether"). */
    private final String path;

    /** * The registry namespace (e.g., "minecraft"). */
    private final String namespace;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Standard Minecraft Constructor**
     * <p>Uses the default Minecraft namespace defined in {@link DatapackConfig}.</p>
     * @param path The dimension path (e.g., "overworld").
     */
    DimensionId(String path) {
        this(path, DatapackConfig.MINECRAFT_NAMESPACE);
    }

    /**
     * 🟠 **Full Custom Constructor**
     * <p>Allows for custom namespaces, useful for multi-world plugins or data packs.</p>
     * <p><b>Error Catching:</b> Trims and lowercases inputs to resolve formatting
     * inconsistencies and triggers {@link #validate()} to ensure character compliance.</p>
     * @param path      The dimension path component.
     * @param namespace The resource namespace component.
     * @throws NullPointerException if path or namespace is null.
     */
    DimensionId(String path, String namespace) {
        this.path = Objects.requireNonNull(path, "Dimension path cannot be null.").toLowerCase().trim();
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null.").toLowerCase().trim();

        // Ensure the identifier adheres to Minecraft's naming rules.
        this.validate();
    }

    // --- 🛰️ ResourceLocation Implementation ---

    /**
     * Retrieves the namespace associated with this dimension.
     * @return The namespace string (e.g., "minecraft").
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * Retrieves the path associated with this dimension.
     * @return The path string (e.g., "the_nether").
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Combines the namespace and path into a valid namespaced key.
     * @return The full identifier (e.g., "minecraft:overworld").
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    /**
     * Performs a syntax check on the identifier components.
     * <p><b>Error Catching:</b> Uses the regex from {@link ResourceLocation}
     * to ensure no illegal characters exist in the registry key.</p>
     * @throws IllegalStateException if the identifier is malformed.
     */
    @Override
    public void validate() throws IllegalStateException {
        if (!VALID_PATTERN.matcher(getResourceLocation()).matches()) {
            throw new IllegalStateException("Malformed DimensionId: " + getResourceLocation());
        }
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a DimensionId from a path string.
     * <p><b>Error Catching:</b> Performs a case-insensitive search. If the path
     * is null or unrecognized, it defaults to {@link #OVERWORLD} to prevent
     * teleportation errors or crashes.</p>
     * @param path The path to look for (e.g., "the_nether").
     * @return The matching {@link DimensionId}, or {@link #OVERWORLD} as a fallback.
     */
    public static DimensionId fromPath(String path) {
        if (path == null || path.isBlank()) {
            return OVERWORLD;
        }

        String target = path.trim();
        for (DimensionId id : values()) {
            if (id.path.equalsIgnoreCase(target)) {
                return id;
            }
        }
        return OVERWORLD;
    }

    // --- 📝 Overrides ---

    /**
     * Returns the full resource location for direct use in commands.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}
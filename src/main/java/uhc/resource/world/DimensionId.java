package uhc.resource.world;

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

    /** * The specific path identifier for the dimension (e.g., "the_nether").
     * Validated against Minecraft's resource location requirements.
     */
    private final String path;

    /** * The registry namespace (e.g., "minecraft").
     * Defaults to the value provided in {@link DatapackConfig}.
     */
    private final String namespace;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Standard Minecraft Constructor**
     * <p>Initializes the dimension using the default {@code minecraft} namespace.</p>
     * * @param path The dimension path (e.g., "overworld").
     */
    DimensionId(String path) {
        this(path, DatapackConfig.MINECRAFT_NAMESPACE);
    }

    /**
     * 🟠 **Full Custom Constructor**
     * <p>Allows for custom namespaces, useful for multi-world configurations.</p>
     * * @param path      The dimension path component.
     * @param namespace The resource namespace component.
     * @throws NullPointerException if path or namespace is null.
     */
    DimensionId(String path, String namespace) {
        // Initialization with trimming and case-correction for stability
        this.path = Objects.requireNonNull(path, "Dimension path cannot be null.")
                .toLowerCase().trim();
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null.")
                .toLowerCase().trim();

        // Immediate validation ensures the object is never in an illegal state.
        this.validate();
    }

    // --- 📝 String & Display Formatting ---

    /**
     * 🏷️ **Title Case Dimension Name**
     * <p>Returns the enum name converted to Title Case (e.g., "Overworld", "Nether").
     * This is useful for chat messages, UI labels, or scoreboard displays.</p>
     *
     * @return The Title Case string derived from {@link #name()}.
     */
    public String title() {
        try {
            // Converts "OVERWORLD" -> "Overworld"
            String constantName = this.name().toLowerCase();
            return constantName.substring(0, 1).toUpperCase() + constantName.substring(1);
        } catch (Exception e) {
            // Error Catching: Fallback to avoid logic breaks during string manipulation
            return "Unknown";
        }
    }

    /**
     * 🏷️ **Get Dimension (Alias)**
     * <p>Provides an alias for {@link #title()} to match naming conventions in other registries.</p>
     * * @return The Title Case string.
     */
    public String getDimension() {
        return title();
    }

    // --- 🛰️ ResourceLocation Implementation ---

    /**
     * Retrieves the namespace associated with this dimension.
     * * @return The namespace string.
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * Retrieves the path associated with this dimension.
     * * @return The path string.
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Combines the namespace and path into a valid namespaced key.
     * * @return The full identifier (e.g., "minecraft:overworld").
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    /**
     * Performs a syntax check on the identifier components.
     * * @throws IllegalStateException if the identifier is malformed according to {@link ResourceLocation}.
     */
    @Override
    public void validate() throws IllegalStateException {
        try {
            if (!VALID_PATTERN.matcher(getResourceLocation()).matches()) {
                throw new IllegalStateException("Malformed DimensionId: " + getResourceLocation());
            }
        } catch (Exception e) {
            throw new IllegalStateException("Critical failure during DimensionId validation.");
        }
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a DimensionId from a path string.
     * * @param path The path to look for (e.g., "the_nether").
     * @return The matching {@link DimensionId}, or {@link #OVERWORLD} as a fallback.
     */
    public static DimensionId fromPath(String path) {
        if (path == null || path.isBlank()) {
            return OVERWORLD;
        }

        try {
            String target = path.trim();
            for (DimensionId id : values()) {
                if (id.path.equalsIgnoreCase(target)) {
                    return id;
                }
            }
        } catch (Exception e) {
            // Silent catch to return default value on iteration error
        }

        return OVERWORLD;
    }

    // --- 📝 Overrides ---

    /**
     * Returns the full resource location for direct use in commands.
     * * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}
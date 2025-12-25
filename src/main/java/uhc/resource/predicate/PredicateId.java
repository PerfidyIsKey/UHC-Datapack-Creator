package uhc.resource.predicate;

import uhc.core.DatapackConfig;
import uhc.resource.ResourceLocation;
import java.util.Objects;

/**
 * 🔍 **Predicate Identifier**
 * <p>
 * Represents a resource location for a condition (predicate) stored within a datapack.
 * Predicates are JSON-based logic files used for complex condition checking in
 * {@code /execute if predicate <id>} commands or loot table conditions.
 * </p>
 */
public enum PredicateId implements ResourceLocation {

    // --- 🛠️ Custom UHC Predicates ---

    /** * Checks if the UHC match state is currently set to active.
     * <p>Location: {@code uhc_core_pack:is_match_active}</p>
     */
    IS_MATCH_ACTIVE("is_match_active", DatapackConfig.CUSTOM_NAMESPACE),

    /** * Checks if a player's coordinates are currently within the active world border.
     * <p>Location: {@code uhc_core_pack:inside_border}</p>
     */
    INSIDE_BORDER("inside_border", DatapackConfig.CUSTOM_NAMESPACE),

    // --- 🌍 Vanilla Minecraft Predicates ---

    /** * A standard check to determine if the world weather is currently a thunderstorm.
     * <p>Location: {@code minecraft:is_thundering}</p>
     */
    IS_THUNDERING("is_thundering", DatapackConfig.MINECRAFT_NAMESPACE);

    // --- ⚙️ Internal State ---

    /** * The registry namespace (e.g., "minecraft" or "uhc_core_pack"). */
    private final String namespace;

    /** * The specific path to the predicate JSON file (e.g., "is_match_active"). */
    private final String path;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Standard Minecraft Constructor**
     * <p>Uses the default Minecraft namespace for the predicate path.</p>
     * @param path The predicate path (filename without .json).
     */
    PredicateId(String path) {
        this(path, DatapackConfig.MINECRAFT_NAMESPACE);
    }

    /**
     * 🟠 **Full Custom Constructor**
     * <p>Allows for specific namespaces and paths. Performs sanitization and validation.</p>
     * <p><b>Error Catching:</b> Trims and lowercases all inputs. Replaces potential
     * spaces in the path with underscores and triggers {@link #validate()}.</p>
     * @param path      The predicate path component.
     * @param namespace The resource namespace component.
     * @throws NullPointerException if path or namespace is null.
     */
    PredicateId(String path, String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null.").toLowerCase().trim();

        // Resolve path: Ensure lowercase and replace spaces with underscores for safety
        String rawPath = Objects.requireNonNull(path, "Predicate path cannot be null.").toLowerCase().trim();
        this.path = rawPath.replace(" ", "_");

        // Validate the final identifier against the ResourceLocation regex pattern.
        this.validate();
    }

    // --- 🛰️ ResourceLocation Implementation ---

    /**
     * Retrieves the namespace associated with this predicate.
     * @return The namespace string.
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * Retrieves the path associated with this predicate.
     * @return The path string.
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Combines the namespace and path into a valid namespaced key.
     * @return The full identifier (e.g., "uhc_core_pack:is_match_active").
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
    }

    /**
     * Performs a syntax check on the identifier components.
     * <p><b>Error Catching:</b> Uses the regex pattern from the {@link ResourceLocation}
     * interface to ensure the identifier is compatible with Minecraft's naming rules.</p>
     * @throws IllegalStateException if the identifier contains illegal characters.
     */
    @Override
    public void validate() throws IllegalStateException {
        if (!VALID_PATTERN.matcher(getResourceLocation()).matches()) {
            throw new IllegalStateException("Invalid PredicateId format: " + getResourceLocation());
        }
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a PredicateId from its path string.
     * <p><b>Error Catching:</b> Performs a case-insensitive search. If the path is null
     * or unrecognized, it returns {@code null} to allow for dynamic predicate handling.</p>
     * @param path The path to search for.
     * @return The matching {@link PredicateId}, or {@code null} if not found.
     */
    public static PredicateId fromPath(String path) {
        if (path == null || path.isBlank()) {
            return null;
        }

        String target = path.trim();
        for (PredicateId id : values()) {
            if (id.path.equalsIgnoreCase(target)) {
                return id;
            }
        }
        return null;
    }

    // --- 📝 Overrides ---

    /**
     * Returns the full resource location for use in command generation.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}
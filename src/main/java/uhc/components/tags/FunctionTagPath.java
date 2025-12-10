package uhc.components.tags;

/**
 * 🏷️ **Type-Safe Minecraft Function Tag Paths**
 * <p>
 * Defines all available function tag files, their path names, and their default
 * namespace location.
 * </p>
 */
public enum FunctionTagPath {

    // --- Critical Vanilla Tags (Explicitly DEFAULTING to 'minecraft' namespace) ---

    /**
     * Represents the {@code load.json} tag.
     * Runs once when the world is loaded/reloaded. Default namespace: {@code minecraft}.
     */
    LOAD,

    /**
     * Represents the {@code tick.json} tag.
     * Runs every game tick. Default namespace: {@code minecraft}.
     */
    TICK,

    // --- Custom Tags (Uses the parameterless constructor, defaulting to 'minecraft') ---

    /**
     * Represents a custom sequence tag (e.g., {@code custom_loop.json}).
     * If no namespace is specified, it defaults to {@code minecraft}.
     */
    CUSTOM_LOOP, // NO ARGUMENT: Will use the default "minecraft" namespace.

    /**
     * Represents another custom tag explicitly using the custom namespace.
     * Requires the custom namespace, defined in {@code DatapackConfig}.
     */
    UHC_INIT(uhc.core.DatapackConfig.CUSTOM_NAMESPACE),

    ; // Semicolon required

    // The suggested namespace where this tag file component should be placed (e.g., "minecraft" or "uhc_core_pack").
    private final String targetNamespace;

    /**
     * Private constructor for tags whose target namespace is explicitly defined.
     *
     * @param targetNamespace The namespace where the tag file should reside.
     * @throws IllegalArgumentException if the provided namespace is null or empty.
     */
    FunctionTagPath(String targetNamespace) {
        if (targetNamespace == null || targetNamespace.isBlank()) {
            throw new IllegalArgumentException("Tag target namespace cannot be null or empty.");
        }
        this.targetNamespace = targetNamespace;
    }

    /**
     * Private parameterless constructor for tags that do not specify a namespace.
     * This constructor defaults the tag location to the standard {@code "minecraft"} namespace.
     */
    FunctionTagPath() {
        this("minecraft");
    }

    /**
     * Generates the final, complete Minecraft function tag name.
     * <p>
     * Ensures the enum constant name is converted to **lowercase** for compliance
     * with Minecraft's resource location rules.
     * </p>
     * Example: For {@code LOAD}, returns {@code "load"}.
     *
     * @return The complete tag name string (excluding the {@code .json} extension).
     */
    public String getPath() {
        // Minecraft resource locations must be all lowercase.
        return this.name().toLowerCase();
    }

    /**
     * Retrieves the suggested namespace where this tag file should be physically located.
     * This helps modules place the tag component correctly.
     *
     * @return The target namespace string (e.g., "minecraft").
     */
    public String getTargetNamespace() {
        return targetNamespace;
    }

    /**
     * Returns the string representation of the tag name, delegating to {@code getPath()}.
     *
     * @return The lowercase tag name.
     */
    @Override
    public String toString() {
        return getPath();
    }
}
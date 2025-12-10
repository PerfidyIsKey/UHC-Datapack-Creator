package uhc.components.tags;

import uhc.core.DatapackConfig;

/**
 * 🏷️ **Type-Safe Minecraft Function Tag Paths**
 * <p>
 * This enum defines all available function tag files, their path names, and their **recommended
 * namespace location**. By using this enum, we ensure tags are referenced correctly
 * and placed in the appropriate {@code tags/function/} folder.
 * </p>
 */
public enum FunctionTagPath {

    // --- Critical Vanilla Tags (Explicitly DEFAULTING to 'minecraft' namespace) ---

    /**
     * Represents the mandatory {@code load.json} tag.
     * Runs once when the world is loaded/reloaded (e.g., via {@code /reload}).
     * Recommended namespace location: {@code minecraft}.
     */
    LOAD,

    /**
     * Represents the mandatory {@code tick.json} tag.
     * Runs every single game tick (20 times per second).
     * Recommended namespace location: {@code minecraft}.
     */
    TICK,

    // --- Custom Tags ---

    /**
     * Represents a custom sequence tag (e.g., {@code custom_loop.json}).
     * If no namespace is specified, it uses the default {@code minecraft} namespace.
     * Useful for user-triggered tags that don't need a specific location.
     */
    CUSTOM_LOOP, // No arguments provided. Delegates to the parameterless constructor.

    /**
     * Represents another custom tag explicitly designed for the custom namespace (e.g., {@code uhc_init.json}).
     * Requires the custom namespace, defined in {@code DatapackConfig}.
     */
    UHC_INIT(DatapackConfig.CUSTOM_NAMESPACE), // Uses the custom namespace constant

    ; // Semicolon required to separate enum constants from fields/methods

    // The suggested namespace where this tag file component should be placed.
    private final String targetNamespace;

    /**
     * Private constructor for tags whose target namespace is explicitly defined.
     *
     * @param targetNamespace The namespace where the tag file should reside (e.g., "minecraft" or "uhc_core_pack").
     * @throws IllegalArgumentException if the provided namespace is null or empty (caught during initialization).
     */
    FunctionTagPath(String targetNamespace) {
        if (targetNamespace == null || targetNamespace.isBlank()) {
            // This is primarily a compile-time check for constant usage.
            throw new IllegalArgumentException("Tag target namespace cannot be null or empty.");
        }
        this.targetNamespace = targetNamespace;
    }

    /**
     * Private parameterless constructor for tags that do not specify a namespace.
     * This constructor defaults the tag location to the standard {@code "minecraft"} namespace,
     * pulling the value from the centralized {@code DatapackConfig}.
     */
    FunctionTagPath() {
        // Delegates to the primary constructor, using the static constant for the default value.
        this(DatapackConfig.MINECRAFT_NAMESPACE);
    }

    /**
     * Generates the final, complete Minecraft function tag name.
     * <p>
     * The method ensures the enum constant name is converted to **lowercase** for strict
     * compliance with Minecraft's resource location rules.
     * </p>
     * Example: For {@code LOAD}, returns {@code "load"}.
     *
     * @return The complete tag name string (excluding the {@code .json} extension).
     */
    public String getPath() {
        // Ensures the output is lowercase, as required by Minecraft.
        return this.name().toLowerCase();
    }

    /**
     * Retrieves the suggested namespace where this tag file component should be physically located.
     * This value is essential for modules to correctly place the tag file on the disk.
     *
     * @return The target namespace string (e.g., "minecraft" or "uhc_core_pack").
     */
    public String getTargetNamespace() {
        return targetNamespace;
    }

    /**
     * Returns the string representation of the tag name.
     * Delegates to {@code getPath()} to ensure consistent, lowercase formatting everywhere the object is used as a string.
     *
     * @return The lowercase tag name.
     */
    @Override
    public String toString() {
        return getPath();
    }
}
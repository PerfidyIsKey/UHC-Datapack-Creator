package uhc.resource;

import java.util.regex.Pattern;

/**
 * 🛠️ **Base Resource Location**
 * <p>
 * The root interface for all namespaced identifiers in Minecraft.
 * A Resource Location represents a unique key in a registry, typically
 * formatted as {@code namespace:path} (e.g., {@code minecraft:iron_ingot}).
 * </p>
 */
public interface ResourceLocation {

    // --- 🔍 Constants ---

    /**
     * Compiled regex pattern to validate Minecraft resource locations.
     * Matches lowercase alphanumeric characters, underscores, dots, and hyphens.
     */
    Pattern VALID_PATTERN = Pattern.compile("([a-z0-9._-]+:)?[a-z0-9._/-]+");

    // --- 🛰️ Core Methods ---

    /**
     * Retrieves the full, formatted namespaced identifier.
     * <p>
     * Implementation Requirement:
     * The returned string must strictly follow the {@code namespace:path} format.
     * If no namespace is explicitly provided by the source, it should default
     * to the value defined in {@code DatapackConfig.MINECRAFT_NAMESPACE}.
     * </p>
     * * @return The full resource location string (e.g., "minecraft:stone").
     */
    String getResourceLocation();

    // --- 🛠️ Default Validation Logic ---

    /**
     * Validates that the resource location string adheres to Minecraft's strict
     * naming conventions (lowercase, no spaces, specific special characters).
     * * @return {@code true} if the location is syntactically valid; {@code false} otherwise.
     */
    default boolean isValid() {
        String location = getResourceLocation();
        if (location == null || location.isBlank()) {
            return false;
        }
        return VALID_PATTERN.matcher(location).matches();
    }

    /**
     * Ensures the resource location is valid, throwing an exception if it is not.
     * <p><b>Error Catching:</b> This provides an immediate fail-fast mechanism
     * to prevent malformed strings from reaching NBT or command execution.</p>
     * * @throws IllegalStateException if the resource location is malformed.
     */
    default void validate() throws IllegalStateException {
        if (!isValid()) {
            throw new IllegalStateException("Malformed ResourceLocation: '" + getResourceLocation() +
                    "'. Must be lowercase and match pattern: [a-z0-9._-]");
        }
    }
}
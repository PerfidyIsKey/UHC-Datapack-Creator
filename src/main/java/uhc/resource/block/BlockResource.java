package uhc.resource.block;

import uhc.resource.ResourceLocation;
import java.util.Objects;

/**
 * 🏷️ **Block Resource Marker**
 * <p>
 * Specifically identifies a {@link ResourceLocation} that is valid for world placement,
 * block predicates, and block-related commands (e.g., {@code /setblock}, {@code /fill}).
 * </p>
 */
public interface BlockResource extends ResourceLocation {

    // --- 🛠️ Static Factory Methods ---

    /**
     * Returns a standard block identifier.
     * <p><b>Use case:</b> Direct block IDs like {@code minecraft:stone}.</p>
     * @param rawLocation The namespaced ID.
     * @return A validated BlockResource instance.
     */
    static BlockResource of(String rawLocation) {
        Objects.requireNonNull(rawLocation, "Block resource location cannot be null.");
        BlockResource resource = () -> rawLocation.toLowerCase().trim();
        resource.validate();
        return resource;
    }

    /**
     * Returns a block tag identifier.
     * <p><b>Use case:</b> Referencing groups of blocks in predicates, e.g., {@code #minecraft:logs}.</p>
     * <p><b>Error Catching:</b> Automatically ensures the tag is prefixed with {@code #}.</p>
     * @param rawTag The namespaced tag (with or without the # prefix).
     * @return A validated BlockResource instance representing a tag.
     */
    static BlockResource tag(String rawTag) {
        Objects.requireNonNull(rawTag, "Block tag cannot be null.");
        String formatted = rawTag.trim().startsWith("#") ? rawTag.trim() : "#" + rawTag.trim();
        BlockResource resource = () -> formatted.toLowerCase();
        resource.validate();
        return resource;
    }

    /**
     * Creates a dynamic block identifier, useful for specialized or modded blocks.
     * @param namespace The namespace (e.g., "minecraft").
     * @param path The block path (e.g., "deepslate_diamond_ore").
     * @return A validated BlockResource.
     */
    static BlockResource dynamic(String namespace, String path) {
        Objects.requireNonNull(namespace, "Namespace cannot be null");
        Objects.requireNonNull(path, "Path cannot be null");
        return of(namespace.toLowerCase() + ":" + path.toLowerCase());
    }

    // --- 🛰️ Core Contract ---

    /**
     * @return The formatted identifier (e.g., "minecraft:stone" or "#minecraft:wool").
     */
    @Override
    String getResourceLocation();

    /**
     * Helper to check if this resource represents a tag.
     * @return {@code true} if the identifier starts with '#'.
     */
    default boolean isTag() {
        return getResourceLocation().startsWith("#");
    }

    // --- 🛡️ Validation ---

    /**
     * Performs block-specific validation.
     * <p><b>Error Catching:</b> Validates standard naming rules while allowing
     * the '#' prefix specifically for tags.</p>
     */
    @Override
    default void validate() throws IllegalStateException {
        String location = getResourceLocation();

        // Temporarily strip '#' for regex validation if it's a tag
        String toValidate = isTag() ? location.substring(1) : location;

        if (toValidate.isEmpty() || !VALID_PATTERN.matcher(toValidate).matches()) {
            throw new IllegalStateException("Malformed BlockResource: '" + location + "'.");
        }

        if (location.contains("[") || location.contains("{")) {
            throw new IllegalStateException("BlockResource cannot contain state/NBT: " + location);
        }
    }
}
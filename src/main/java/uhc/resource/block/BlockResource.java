package uhc.resource.block;

import uhc.resource.ResourceLocation;
import java.util.Objects;

/**
 * 🏷️ **Block Resource Marker**
 * <p>
 * Specifically identifies a {@link ResourceLocation} that is valid for world placement,
 * block predicates, and block-related commands.
 * </p>
 */
public interface BlockResource extends ResourceLocation {

    // --- 🛠️ Static Factory Methods ---

    /**
     * Returns a standard block identifier.
     * @param rawLocation The namespaced ID (e.g., "minecraft:stone").
     * @return A validated BlockResource instance.
     */
    static BlockResource of(String rawLocation) {
        Objects.requireNonNull(rawLocation, "Block resource location cannot be null.");
        String cleaned = rawLocation.toLowerCase().trim();

        if (!cleaned.contains(":")) {
            throw new IllegalStateException("Block Resource must contain a namespace separator ':'.");
        }

        String[] parts = cleaned.split(":", 2);
        String ns = parts[0];
        String p = parts[1];

        BlockResource resource = new BlockResource() {
            @Override public String getNamespace() { return ns; }
            @Override public String getPath() { return p; }
            @Override public String getResourceLocation() { return cleaned; }
        };

        resource.validate();
        return resource;
    }

    /**
     * Returns a block tag identifier prefixed with '#'.
     * @param rawTag The namespaced tag (e.g., "minecraft:logs").
     * @return A validated BlockResource instance representing a tag.
     */
    static BlockResource tag(String rawTag) {
        Objects.requireNonNull(rawTag, "Block tag cannot be null.");
        String trimmed = rawTag.trim().toLowerCase();
        String pathOnly = trimmed.startsWith("#") ? trimmed.substring(1) : trimmed;

        if (!pathOnly.contains(":")) {
            throw new IllegalStateException("Block Tag must contain a namespace separator ':'.");
        }

        String[] parts = pathOnly.split(":", 2);
        String ns = parts[0];
        String p = parts[1];

        BlockResource resource = new BlockResource() {
            @Override public String getNamespace() { return ns; }
            @Override public String getPath() { return p; }
            @Override public String getResourceLocation() { return "#" + ns + ":" + p; }
        };

        resource.validate();
        return resource;
    }

    // --- 🛰️ Core Contract Overrides ---

    @Override String getNamespace();
    @Override String getPath();
    @Override String getResourceLocation();

    /**
     * Helper to check if this resource represents a tag.
     * @return {@code true} if the identifier starts with '#'.
     */
    default boolean isTag() {
        return getResourceLocation().startsWith("#");
    }

    // --- 🛡️ Validation ---

    @Override
    default void validate() throws IllegalStateException {
        // Run standard ResourceLocation regex validation on the parts
        String raw = getNamespace() + ":" + getPath();
        if (!VALID_PATTERN.matcher(raw).matches()) {
            throw new IllegalStateException("Malformed BlockResource: '" + getResourceLocation() + "'.");
        }

        if (getResourceLocation().contains("[") || getResourceLocation().contains("{")) {
            throw new IllegalStateException("BlockResource cannot contain state/NBT: " + getResourceLocation());
        }
    }
}
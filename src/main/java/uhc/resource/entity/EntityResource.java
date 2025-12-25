package uhc.resource.entity;

import uhc.resource.ResourceLocation;
import java.util.Objects;

/**
 * 🏷️ **Entity Resource Marker**
 * <p>
 * Specifically identifies a {@link ResourceLocation} that represents an entity type
 * valid for summoning, killing, or targeting.
 * </p>
 */
public interface EntityResource extends ResourceLocation {

    // --- 🛠️ Static Factory / Utility ---

    /**
     * Creates a type-safe {@code EntityResource} from a raw namespaced string.
     * <p><b>Error Catching:</b> Decomposes the string into namespace and path to
     * satisfy the base contract and executes validation.</p>
     * @param rawLocation The namespaced ID (e.g., "minecraft:zombie").
     * @return A validated EntityResource instance.
     * @throws IllegalStateException if the location is malformed or missing a colon.
     */
    static EntityResource of(String rawLocation) {
        Objects.requireNonNull(rawLocation, "Entity resource location cannot be null.");
        String cleaned = rawLocation.toLowerCase().trim();

        if (!cleaned.contains(":")) {
            throw new IllegalStateException("Entity Resource must contain a namespace separator ':'. Received: " + cleaned);
        }

        String[] parts = cleaned.split(":", 2);
        String namespace = parts[0];
        String path = parts[1];

        EntityResource resource = new EntityResource() {
            @Override
            public String getNamespace() { return namespace; }

            @Override
            public String getPath() { return path; }

            @Override
            public String getResourceLocation() { return cleaned; }
        };

        resource.validate();
        return resource;
    }

    // --- 🛰️ Core Contract Overrides ---

    /** @return The namespace component. */
    @Override
    String getNamespace();

    /** @return The path component. */
    @Override
    String getPath();

    /** @return The full identifier. */
    @Override
    String getResourceLocation();

    // --- 🛡️ Validation ---

    /**
     * Performs entity-specific validation on the identifier.
     * <p><b>Error Catching:</b> Ensures no NBT data or selector syntax is present.</p>
     * @throws IllegalStateException if the entity identifier is syntactically invalid.
     */
    @Override
    default void validate() throws IllegalStateException {
        // Run standard ResourceLocation regex validation
        ResourceLocation.super.validate();

        String location = getResourceLocation();

        // Ensure purely registry type identifier
        if (location.contains("{") || location.contains("}")) {
            throw new IllegalStateException("EntityResource must represent the Entity Type only, not NBT. Found: " + location);
        }

        if (location.contains("@") || location.contains("[") || location.contains("]")) {
            throw new IllegalStateException("EntityResource cannot contain selector syntax (@, [, ]): " + location);
        }
    }
}
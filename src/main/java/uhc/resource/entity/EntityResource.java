package uhc.resource.entity;

import uhc.resource.ResourceLocation;
import java.util.Objects;

/**
 * 🏷️ **Entity Resource Marker**
 * <p>
 * Specifically identifies a {@link ResourceLocation} that represents an entity type
 * valid for summoning, killing, or targeting (e.g., {@code /summon}, {@code /kill},
 * or {@code @e[type=...]}).
 * </p>
 * <p>
 * This interface prevents logic errors by ensuring that methods designed for
 * mob/entity manipulation cannot accidentally receive items or blocks.
 * </p>
 */
public interface EntityResource extends ResourceLocation {

    // --- 🛠️ Static Factory / Utility ---

    /**
     * Creates a type-safe {@code EntityResource} from a raw namespaced string.
     * <p><b>Error Catching:</b> Validates that the input is not null and immediately
     * executes {@link #validate()} to verify the string adheres to Minecraft's
     * internal entity registry standards.</p>
     * * @param rawLocation The namespaced ID (e.g., "minecraft:zombie", "uhc:custom_boss").
     * @return A validated EntityResource instance.
     * @throws NullPointerException if {@code rawLocation} is null.
     * @throws IllegalStateException if the location is malformed.
     */
    static EntityResource of(String rawLocation) {
        Objects.requireNonNull(rawLocation, "Entity resource location cannot be null.");

        EntityResource resource = () -> rawLocation.toLowerCase().trim();

        // Fail-fast validation before the object is used in command builders
        resource.validate();

        return resource;
    }

    // --- 🛰️ Core Contract ---

    /**
     * Inherited from {@link ResourceLocation}.
     * <p>
     * Must return the unique identifier for the entity type (e.g., "minecraft:creeper").
     * This is the string used in the {@code type} argument of selectors.
     * </p>
     * * @return The namespaced identifier for the entity.
     */
    @Override
    String getResourceLocation();

    // --- 🛡️ Validation ---

    /**
     * Performs entity-specific validation on the identifier.
     * <p><b>Error Catching:</b> In addition to standard regex validation, this
     * check ensures no entity NBT data (curly braces) is included in the base
     * resource identifier, which would break certain selector syntaxes.</p>
     * * @throws IllegalStateException if the entity identifier is syntactically invalid.
     */
    @Override
    default void validate() throws IllegalStateException {
        // Run standard ResourceLocation regex validation
        ResourceLocation.super.validate();

        String location = getResourceLocation();

        // Specific check: ResourceLocation represents the TYPE, not the specific data.
        if (location.contains("{") || location.contains("}")) {
            throw new IllegalStateException("EntityResource must represent the Entity Type only, " +
                    "not its NBT data. Found: " + location);
        }

        // Check for selector-specific characters that shouldn't be in a Registry ID
        if (location.contains("@") || location.contains("[") || location.contains("]")) {
            throw new IllegalStateException("EntityResource cannot contain selector syntax (@, [, ]): " + location);
        }
    }
}
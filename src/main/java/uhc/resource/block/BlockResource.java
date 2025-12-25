package uhc.resource.block;

import uhc.resource.ResourceLocation;
import java.util.Objects;

/**
 * 🏷️ **Block Resource Marker**
 * <p>
 * Specifically identifies a {@link ResourceLocation} that is valid for world placement
 * and block-related logic (e.g., {@code /setblock}, {@code /fill}, or checking block states).
 * </p>
 * <p>
 * <b>Note:</b> Many implementations of this interface will also implement
 * {@code ItemResource}, as most blocks in Minecraft have a corresponding item form
 * that can be held in an inventory.
 * </p>
 */
public interface BlockResource extends ResourceLocation {

    // --- 🛠️ Static Factory / Utility ---

    /**
     * Creates a type-safe {@code BlockResource} from a raw string location.
     * <p><b>Error Catching:</b> Validates that the string is not null or empty and
     * immediately runs {@link #validate()} to ensure it meets Minecraft registry standards.</p>
     * * @param rawLocation The namespaced ID (e.g., "minecraft:stone" or "uhc:lucky_block").
     * @return A validated BlockResource instance.
     * @throws NullPointerException if {@code rawLocation} is null.
     * @throws IllegalStateException if the location fails naming conventions.
     */
    static BlockResource of(String rawLocation) {
        Objects.requireNonNull(rawLocation, "Block resource location cannot be null.");

        BlockResource resource = () -> rawLocation.toLowerCase().trim();

        // Immediate validation to catch malformed strings before they enter the system
        resource.validate();

        return resource;
    }

    // --- 🛰️ Core Contract ---

    /**
     * Inherited from {@link ResourceLocation}.
     * <p>
     * Must return the unique identifier for the block (e.g., "minecraft:diamond_ore").
     * This string is used for block placement and NBT storage.
     * </p>
     * * @return The namespaced identifier for the block.
     */
    @Override
    String getResourceLocation();

    // --- 🛡️ Validation ---

    /**
     * Performs block-specific validation on the identifier.
     * <p><b>Error Catching:</b> Leverages the base regex validation while adding
     * specific constraints to prevent common command syntax errors.</p>
     * * @throws IllegalStateException if the block identifier is malformed or empty.
     */
    @Override
    default void validate() throws IllegalStateException {
        // Run standard ResourceLocation regex check ([a-z0-9._-])
        ResourceLocation.super.validate();

        String location = getResourceLocation();

        // Custom error check for common user errors in command strings
        if (location.contains("[") || location.contains("]")) {
            throw new IllegalStateException("BlockResource should not contain block state brackets: " + location +
                    ". Use a separate BlockState handler for properties.");
        }

        if (location.contains("{") || location.contains("}")) {
            throw new IllegalStateException("BlockResource should not contain NBT curly braces: " + location);
        }
    }
}
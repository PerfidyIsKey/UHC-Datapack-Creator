package uhc.resource.item;

import uhc.resource.ResourceLocation;
import java.util.Objects;

/**
 * 🏷️ **Item Resource Marker**
 * <p>
 * Specifically identifies a {@link ResourceLocation} that is valid for use in
 * ItemStacks, inventory slots, or {@code /give} commands.
 * </p>
 * <p>
 * By implementing this interface, a class (such as {@link ItemId}) signals
 * to the compiler that it can be safely used in methods requiring an item type.
 * </p>
 */
public interface ItemResource extends ResourceLocation {

    // --- 🛠️ Static Factory / Utility ---

    /**
     * Wraps a raw string into a type-safe {@code ItemResource} anonymous implementation.
     * <p><b>Error Catching:</b> This method validates that the provided string is not
     * null or blank and checks it against Minecraft's naming conventions via {@link #validate()}.</p>
     * * @param rawLocation The raw resource location (e.g., "minecraft:iron_sword").
     * @return A validated ItemResource instance.
     * @throws NullPointerException if the input is null.
     * @throws IllegalStateException if the input fails naming validation.
     */
    static ItemResource of(String rawLocation) {
        Objects.requireNonNull(rawLocation, "Raw item location cannot be null.");

        ItemResource resource = () -> rawLocation.toLowerCase().trim();

        // Ensure the manual input follows [a-z0-9._-]
        resource.validate();

        return resource;
    }

    // --- 🛰️ Core Contract ---

    /**
     * Inherited from {@link ResourceLocation}.
     * <p>
     * Implementation should return the full identifier (e.g., "minecraft:apple").
     * For items, this is typically used in give commands and NBT data.
     * </p>
     * * @return The namespaced identifier for the item.
     */
    @Override
    String getResourceLocation();

    // --- 🛡️ Validation ---

    /**
     * Performs an item-specific validation check.
     * <p><b>Error Catching:</b> Beyond basic regex, this ensures the item
     * identifier does not contain characters invalid for command syntax.</p>
     * * @throws IllegalStateException if the item identifier is malformed.
     */
    @Override
    default void validate() throws IllegalStateException {
        // Call the base ResourceLocation validation (regex check)
        ResourceLocation.super.validate();

        String path = getResourceLocation();
        if (path.contains(" ")) {
            throw new IllegalStateException("Item Resource cannot contain spaces: " + path);
        }
    }
}
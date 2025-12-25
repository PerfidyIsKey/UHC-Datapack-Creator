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
     * Wraps a raw string into a type-safe {@code ItemResource} instance.
     * <p><b>Error Catching:</b> This method validates that the provided string is not
     * null or blank and checks it against Minecraft's naming conventions.</p>
     * * @param rawLocation The raw resource location (e.g., "minecraft:iron_sword").
     * @return A validated ItemResource instance.
     * @throws NullPointerException if the input is null.
     * @throws IllegalStateException if the input fails naming validation or is missing a colon.
     */
    static ItemResource of(String rawLocation) {
        Objects.requireNonNull(rawLocation, "Raw item location cannot be null.");
        String cleaned = rawLocation.toLowerCase().trim();

        if (!cleaned.contains(":")) {
            throw new IllegalStateException("Item Resource must contain a namespace separator ':'. Received: " + cleaned);
        }

        String[] parts = cleaned.split(":", 2);
        String namespace = parts[0];
        String path = parts[1];

        ItemResource resource = new ItemResource() {
            @Override
            public String getNamespace() { return namespace; }

            @Override
            public String getPath() { return path; }

            @Override
            public String getResourceLocation() { return cleaned; }
        };

        // Ensure the manual input follows [a-z0-9._-]
        resource.validate();

        return resource;
    }

    // --- 🛰️ Core Contract Overrides ---

    /**
     * Inherited from {@link ResourceLocation}.
     * @return The namespace component (e.g., "minecraft").
     */
    @Override
    String getNamespace();

    /**
     * Inherited from {@link ResourceLocation}.
     * @return The path component (e.g., "apple").
     */
    @Override
    String getPath();

    /**
     * Inherited from {@link ResourceLocation}.
     * @return The namespaced identifier (e.g., "minecraft:apple").
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

        String full = getResourceLocation();
        if (full.contains(" ")) {
            throw new IllegalStateException("Item Resource cannot contain spaces: " + full);
        }

        if (getNamespace().isEmpty() || getPath().isEmpty()) {
            throw new IllegalStateException("Item Resource namespace or path cannot be empty.");
        }
    }
}
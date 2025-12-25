package uhc.resource.block;

import uhc.resource.item.ItemResource;
import uhc.text.DyeColor;
import java.util.Objects;

/**
 * 🧱 **Dynamic Block Identifier**
 * <p>
 * This class provides immutable, factory-generated block identifiers constructed at runtime.
 * It acts as the central engine for creating specific {@link BlockIdentifier} instances
 * using wood or color templates.
 * </p>
 */
public final class DynamicBlock implements BlockIdentifier, ItemResource {

    /** * The full namespaced identifier (e.g., "minecraft:cherry_planks").
     * This field is final and immutable to ensure thread safety across the UHC engine.
     */
    private final String location;

    // --- 🏗️ Constructor ---

    /**
     * Internal constructor used by static factory methods.
     * <p><b>Error Catching:</b> Immediately triggers {@link #validate()} to ensure
     * the constructed string is a syntactically valid Minecraft resource location.</p>
     * * @param location The pre-formatted full resource location string.
     * @throws IllegalStateException if the location violates Minecraft naming rules or is a tag.
     */
    private DynamicBlock(String location) {
        this.location = location;
        this.validate();
    }

    // --- 🎨 Colorable Factories ---

    /**
     * Creates a colored block identifier using the registry templates.
     * <p><b>Logic:</b> Delegates the construction to the {@link ColorableBlock} enum template.</p>
     * * @param color The {@link DyeColor} to apply (e.g., RED).
     * @param block The {@link ColorableBlock} template to use (e.g., WOOL).
     * @return A validated {@link BlockIdentifier} instance.
     * @throws NullPointerException if color or block template is null.
     */
    public static BlockIdentifier color(DyeColor color, ColorableBlock block) {
        Objects.requireNonNull(color, "DyeColor cannot be null.");
        Objects.requireNonNull(block, "ColorableBlock template cannot be null.");

        return block.withColor(color);
    }

    // --- 🌳 Wood-Type Factories ---

    /**
     * Creates a wood-based block identifier using the registry templates.
     * <p><b>Logic:</b> Delegates the construction to the {@link WoodBlock} enum template.</p>
     * * @param wood  The {@link WoodType} to apply (e.g., CHERRY).
     * @param block The {@link WoodBlock} template to use (e.g., PLANKS).
     * @return A validated {@link BlockIdentifier} instance.
     * @throws NullPointerException if wood or block template is null.
     */
    public static BlockIdentifier wood(WoodType wood, WoodBlock block) {
        Objects.requireNonNull(wood, "WoodType cannot be null.");
        Objects.requireNonNull(block, "WoodBlock template cannot be null.");

        return block.withWoodType(wood);
    }

    // --- 🧩 General Custom Factory ---

    /**
     * Creates a fully custom block resource from raw components.
     * <p><b>Error Catching:</b> Enforces lowercase and trims whitespace for both
     * namespace and path to maintain strict registry compatibility.</p>
     * * @param namespace The custom namespace (e.g., "uhc").
     * @param path      The block path component (e.g., "special_ore").
     * @return A validated {@link BlockIdentifier} instance.
     * @throws NullPointerException if namespace or path is null.
     */
    public static BlockIdentifier custom(String namespace, String path) {
        Objects.requireNonNull(namespace, "Namespace cannot be null.");
        Objects.requireNonNull(path, "Path cannot be null.");

        return new DynamicBlock(
                namespace.toLowerCase().trim() + ":" + path.toLowerCase().trim()
        );
    }

    // --- 🛰️ Contract Implementation ---

    /**
     * Retrieves the stored namespaced identifier.
     * @return The immutable resource location string.
     */
    @Override
    public String getResourceLocation() {
        return location;
    }

    /**
     * Validates the internal location string.
     * <p>Inherits logic from {@link BlockIdentifier#validate()} to ensure
     * no hashtags (#) are present and standard character rules are followed.</p>
     */
    @Override
    public void validate() throws IllegalStateException {
        BlockIdentifier.super.validate();
    }

    /**
     * Returns the identifier as a string for direct command insertion.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}
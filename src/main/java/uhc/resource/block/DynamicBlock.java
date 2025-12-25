package uhc.resource.block;

import uhc.core.DatapackConfig;
import uhc.text.DyeColor;
import java.util.Objects;

/**
 * 🧱 **Dynamic Block Identifier**
 * <p>
 * Provides a flexible way to construct block resource locations at runtime,
 * specifically for categorized blocks like colored or wood-based variants.
 * </p>
 */
public final class DynamicBlock implements BlockResource {

    /** The full namespaced identifier (e.g., "minecraft:cherry_planks"). */
    private final String location;

    /**
     * Private constructor to enforce use of static factory methods.
     * @param location The pre-formatted location string.
     */
    private DynamicBlock(String location) {
        this.location = location;
        this.validate();
    }

    // --- 🎨 Colorable Factories ---

    /**
     * Creates a colored block identifier (e.g., "minecraft:red_stained_glass").
     * @param color The {@link DyeColor} to apply.
     * @param baseName The base name of the block (e.g., "stained_glass").
     * @return A validated DynamicBlock resource.
     * @throws NullPointerException if color or baseName is null.
     */
    public static BlockResource color(DyeColor color, String baseName) {
        Objects.requireNonNull(color, "DyeColor cannot be null.");
        Objects.requireNonNull(baseName, "Base block name cannot be null.");

        String path = color.toString() + "_" + baseName.toLowerCase().trim();
        return new DynamicBlock(DatapackConfig.MINECRAFT_NAMESPACE + ":" + path);
    }

    // --- 🌳 Wood-Type Factories ---

    /**
     * Creates a wood-based block identifier (e.g., "minecraft:cherry_planks").
     * @param wood The {@link WoodType} to apply.
     * @param baseName The base name of the block (e.g., "planks").
     * @return A validated DynamicBlock resource.
     * @throws NullPointerException if wood or baseName is null.
     */
    public static BlockResource wood(WoodType wood, String baseName) {
        Objects.requireNonNull(wood, "WoodType cannot be null.");
        Objects.requireNonNull(baseName, "Base block name cannot be null.");

        String path = wood.toString() + "_" + baseName.toLowerCase().trim();
        return new DynamicBlock(DatapackConfig.MINECRAFT_NAMESPACE + ":" + path);
    }

    // --- 🧩 General Custom Factory ---

    /**
     * Creates a fully custom block resource from raw components.
     * @param namespace The custom namespace.
     * @param path The path component.
     * @return A validated DynamicBlock resource.
     */
    public static BlockResource custom(String namespace, String path) {
        Objects.requireNonNull(namespace, "Namespace cannot be null.");
        Objects.requireNonNull(path, "Path cannot be null.");

        return new DynamicBlock(namespace.toLowerCase().trim() + ":" + path.toLowerCase().trim());
    }

    // --- 🛰️ Contract Implementation ---

    @Override
    public String getResourceLocation() {
        return location;
    }

    @Override
    public String toString() {
        return getResourceLocation();
    }
}
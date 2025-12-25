package uhc.resource.block;

import uhc.resource.item.ItemResource;
import uhc.resource.color.DyeColor;
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

    // --- ⚙️ State & Fields ---

    /** * The namespace part of the resource location (e.g., "minecraft"). */
    private final String namespace;

    /** * The path part of the resource location (e.g., "cherry_planks"). */
    private final String path;

    // --- 🏗️ Constructor ---

    /**
     * Internal constructor used by static factory methods.
     * <p><b>Error Catching:</b> Immediately triggers {@link #validate()} to ensure
     * the constructed components form a syntactically valid Minecraft resource location.</p>
     * * @param namespace The resource namespace.
     * @param path      The resource path.
     * @throws IllegalStateException if the location violates Minecraft naming rules.
     */
    private DynamicBlock(String namespace, String path) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase().trim();
        this.path = Objects.requireNonNull(path, "Path cannot be null").toLowerCase().trim();
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
        return new DynamicBlock(namespace, path);
    }

    // --- 🛰️ ResourceLocation Implementation ---

    /**
     * Retrieves the namespace component of the block.
     * @return The namespace string (e.g., "minecraft").
     */
    @Override
    public String getNamespace() {
        return namespace;
    }

    /**
     * Retrieves the path component of the block.
     * @return The path string (e.g., "oak_log").
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * Retrieves the stored namespaced identifier.
     * @return The immutable resource location string in {@code namespace:path} format.
     */
    @Override
    public String getResourceLocation() {
        return namespace + ":" + path;
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

    // --- 📝 Overrides ---

    /**
     * Returns the identifier as a string for direct command insertion.
     * @return The result of {@link #getResourceLocation()}.
     */
    @Override
    public String toString() {
        return getResourceLocation();
    }
}
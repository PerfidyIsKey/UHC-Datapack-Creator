package uhc.resource.item;

import uhc.core.DatapackConfig;
import uhc.resource.color.DyeColor;
import java.util.Objects;

/**
 * 🎨 **Colorable Item Template Registry**
 * <p>
 * This enumeration defines templates for Minecraft items that require a
 * {@link DyeColor} prefix to resolve into a specific {@link ItemResource}.
 * </p>
 * <p>
 * Unlike blocks, these resources represent items that primarily exist within
 * inventories or as world entities (like dropped items), such as bundles.
 * </p>
 */
public enum ColorableItem {

    // --- 🏷️ Item Templates ---

    /** * A multi-slot storage item that can hold various stacks.
     * <p>Example: {@code BUNDLE.withColor(DyeColor.YELLOW)} -> "minecraft:yellow_bundle"</p>
     */
    BUNDLE,

    /** * A handheld or placeable light source.
     * <p>Example: {@code CANDLE.withColor(DyeColor.RED)} -> "minecraft:red_candle"</p>
     */
    CANDLE;

    // --- ⚙️ State & Fields ---

    /** * The namespace utilized for the generated item identifier.
     * Defaults to the standard Minecraft namespace.
     */
    private final String namespace;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Default Constructor**
     * <p>Initializes the template with the default Minecraft namespace
     * provided by the global {@link DatapackConfig}.</p>
     */
    ColorableItem() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
    }

    /**
     * 🟡 **Custom Namespace Constructor**
     * <p>Initializes the template with a specific namespace for modded or
     * custom datapack items.</p>
     * @param namespace The target namespace string.
     * @throws NullPointerException if the provided namespace is null.
     */
    ColorableItem(String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null for ColorableItem.");
    }

    // --- 🛠️ Logic Methods ---

    /**
     * Constructs a specific, immutable {@link ItemResource} by applying a color to this template.
     * <p><b>Error Catching:</b> Strictly validates that the {@link DyeColor} object is not null.
     * It delegates the final instantiation to {@link DynamicItem#custom}, which performs
     * syntax validation on the final string.</p>
     * * @param color The {@link DyeColor} object to apply (e.g., LIME, CYAN).
     * @return A fully qualified and validated {@link ItemResource}.
     * @throws NullPointerException if the provided {@code color} is null.
     * @throws IllegalStateException if the resulting resource location fails naming standards.
     */
    public ItemResource withColor(DyeColor color) {
        // Defensive check to prevent malformed "null_bundle" paths
        Objects.requireNonNull(color, "DyeColor cannot be null for a ColorableItem transformation.");

        // We use the enum's name safely to build the path component
        String itemBaseName = this.name().toLowerCase();

        // Use the DyeColor's own name/string representation to build the prefix
        String fullPath = color.toString() + "_" + itemBaseName;

        // Return via DynamicItem to ensure the result is treated as a dynamic resource
        return DynamicItem.custom(namespace, fullPath);
    }

    // --- 🛰️ Metadata Accessors ---

    /**
     * Retrieves the namespace defined for this colorable item category.
     * @return The namespace string (e.g., "minecraft").
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Returns a display-friendly name for this item category.
     * <p>Example: {@code BUNDLE} -> "Bundle"</p>
     * @return The capitalized category name.
     */
    public String getCategoryName() {
        String name = this.name().toLowerCase();
        if (name.isEmpty()) return "";
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
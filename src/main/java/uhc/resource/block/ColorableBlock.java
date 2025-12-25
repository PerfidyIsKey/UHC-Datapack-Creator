package uhc.resource.block;

import uhc.core.DatapackConfig;
import uhc.text.DyeColor;
import java.util.Objects;

/**
 * 🎨 **Colorable Block Registry**
 * <p>
 * Defines block types that require a {@link DyeColor} prefix to form a complete
 * Minecraft resource location. This enum acts as a template for generating
 * dynamic block identifiers.
 * </p>
 * <p>
 * <b>Example:</b> {@code STAINED_GLASS.withColor(DyeColor.RED)} produces {@code "minecraft:red_stained_glass"}.
 * </p>
 */
public enum ColorableBlock {

    /** Represents transparent decorative blocks like 'white_stained_glass'. */
    STAINED_GLASS,

    /** Represents gravity-affected blocks like 'red_concrete_powder'. */
    CONCRETE_POWDER,

    /** Represents solid blocks like 'blue_wool'. */
    WOOL,

    /** Represents solid blocks like 'green_concrete'. */
    CONCRETE,

    /** Represents blocks like 'orange_terracotta'. */
    TERRACOTTA,

    /** Represents transparent panes like 'cyan_stained_glass_pane'. */
    STAINED_GLASS_PANE;

    // --- ⚙️ State & Fields ---

    /** The namespace for the block identifier (e.g., "minecraft"). */
    private final String namespace;

    // --- 🏗️ Constructors ---

    /**
     * Default constructor using the standard Minecraft namespace defined in {@link DatapackConfig}.
     */
    ColorableBlock() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
    }

    /**
     * Constructor for blocks requiring a custom namespace (e.g., modded colored blocks).
     * @param namespace The custom namespace component.
     */
    ColorableBlock(String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase();
    }

    // --- 🛠️ Logic Methods ---

    /**
     * Constructs a complete, immutable {@link BlockResource} by applying a color to this template.
     * <p><b>Error Catching:</b> Validates that the color is not null and ensures the
     * resulting path is lowercased and trimmed to prevent command syntax errors.</p>
     * * @param color The {@link DyeColor} to apply (e.g., RED, BLUE).
     * @return A fully qualified and validated {@link BlockResource}.
     * @throws NullPointerException if the provided {@code color} is null.
     */
    public BlockResource withColor(DyeColor color) {
        Objects.requireNonNull(color, "DyeColor cannot be null for a ColorableBlockId.");

        // Convert enum name (e.g., STAINED_GLASS) to path component (stained_glass)
        String blockNameComponent = this.name().toLowerCase();

        // Build the combined path: e.g., "red_stained_glass"
        String fullPath = color.toString() + "_" + blockNameComponent;

        // Return a DynamicBlock (or a lambda BlockResource) to keep this Enum stateless
        return DynamicBlock.custom(namespace, fullPath);
    }

    // --- 🛰️ Metadata Accessors ---

    /**
     * Retrieves the namespace associated with this colorable template.
     * @return The namespace string.
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Provides a display-friendly name of the block category.
     * @return The formatted name (e.g., "Stained Glass").
     */
    public String getCategoryName() {
        String name = this.name().replace("_", " ").toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
package uhc.resource.block;

import uhc.core.DatapackConfig;
import uhc.resource.color.DyeColor;
import java.util.Objects;

/**
 * 🎨 **Colorable Block Template Registry**
 * <p>
 * This enumeration defines templates for Minecraft blocks that require a
 * {@link DyeColor} prefix to resolve into a specific {@link BlockIdentifier}.
 * </p>
 * <p>
 * <b>Example:</b> {@code WOOL.withColor(DyeColor.LIME)} results in a
 * {@code BlockIdentifier} for {@code "minecraft:lime_wool"}.
 * </p>
 */
public enum ColorableBlock {

    // --- 🏳️ Block Templates ---

    /** Transparent decorative blocks (e.g., 'white_stained_glass'). */
    STAINED_GLASS,

    /** Gravity-affected powder blocks (e.g., 'red_concrete_powder'). */
    CONCRETE_POWDER,

    /** Traditional soft building blocks (e.g., 'blue_wool'). */
    WOOL,

    /** Smooth, solid construction blocks (e.g., 'green_concrete'). */
    CONCRETE,

    /** Fired clay blocks (e.g., 'orange_terracotta'). */
    TERRACOTTA,

    /** Thin transparent panes (e.g., 'cyan_stained_glass_pane'). */
    STAINED_GLASS_PANE,

    /** Soft floor coverings (e.g., 'light_gray_carpet'). */
    CARPET,

    /** Glowing light sources (e.g., 'yellow_shulker_box'). */
    SHULKER_BOX;

    // --- ⚙️ State & Fields ---

    /** The namespace designated for this block category (e.g., "minecraft"). */
    private final String namespace;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Default Constructor**
     * <p>Initializes the template using the global default Minecraft namespace.</p>
     */
    ColorableBlock() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
    }

    /**
     * 🟡 **Custom Namespace Constructor**
     * <p>Initializes the template with a specific namespace, such as for modded content.</p>
     * @param namespace The target namespace component.
     * @throws NullPointerException if the namespace argument is null.
     */
    ColorableBlock(String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase();
    }

    // --- 🛠️ Logic Methods ---

    /**
     * Constructs a complete, immutable {@link BlockIdentifier} by applying a color to this template.
     * <p><b>Error Catching:</b> Strictly validates that the {@code color} is not null.
     * It internally utilizes {@link DynamicBlock#custom} which performs secondary syntax
     * and type-filter validation to ensure a tag is not created.</p>
     * * @param color The {@link DyeColor} to apply (e.g., RED).
     * @return A fully qualified, specific {@link BlockIdentifier}.
     * @throws NullPointerException if the provided {@code color} is null.
     */
    public BlockIdentifier withColor(DyeColor color) {
        Objects.requireNonNull(color, "DyeColor cannot be null for a ColorableBlock.");

        // Convert enum constant name to lowercase path component
        String blockNameComponent = this.name().toLowerCase();

        // Build the final path: {color}_{base_name}
        String fullPath = color.toString() + "_" + blockNameComponent;

        // Return via DynamicBlock to ensure it implements BlockIdentifier
        return DynamicBlock.custom(namespace, fullPath);
    }

    // --- 🛰️ Metadata Accessors ---

    /**
     * Retrieves the namespace defined for this template.
     * @return The namespace string.
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Returns a human-readable name for the block category.
     * <p>Example: {@code STAINED_GLASS_PANE} -> "Stained Glass Pane"</p>
     * @return The formatted display name.
     */
    public String getCategoryName() {
        String name = this.name().replace("_", " ").toLowerCase();
        if (name.isEmpty()) return "";

        // Capitalize words for clean UI display
        String[] words = name.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (result.length() > 0) result.append(" ");
            result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return result.toString();
    }
}
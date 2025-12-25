package uhc.resource.block;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🌳 **Wood-Based Block Template Registry**
 * <p>
 * Defines block types that require a {@link WoodType} prefix to construct a valid
 * Minecraft resource location. This enum acts as a factory template.
 * </p>
 * <p>
 * <b>Example:</b> {@code PLANKS.withWoodType(WoodType.CHERRY)} produces {@code "minecraft:cherry_planks"}.
 * </p>
 */
public enum WoodBlock {

    // --- 🪵 Standard Wood Templates ---

    /** Basic building blocks (e.g., 'oak_planks'). */
    PLANKS,

    /** Vertical support blocks (e.g., 'spruce_log'). */
    LOG,

    /** Bark-covered decorative blocks (e.g., 'birch_wood'). */
    WOOD,

    /** Debarked log variants (e.g., 'stripped_jungle_log'). */
    STRIPPED_LOG,

    /** Debarked wood variants (e.g., 'stripped_acacia_wood'). */
    STRIPPED_WOOD,

    /** Floor-standing signage (e.g., 'dark_oak_sign'). */
    SIGN,

    /** Wall-mounted signage (e.g., 'mangrove_wall_sign'). */
    WALL_SIGN,

    /** Horizontal movement blocks (e.g., 'cherry_stairs'). */
    STAIRS,

    /** Half-height building blocks (e.g., 'bamboo_slab'). */
    SLAB,

    /** Boundary and decorative rails (e.g., 'crimson_fence'). */
    FENCE,

    /** Functional gates for fences (e.g., 'warped_fence_gate'). */
    FENCE_GATE,

    /** Horizontal entryway blocks (e.g., 'oak_door'). */
    DOOR,

    /** Compact vertical entryway blocks (e.g., 'spruce_trapdoor'). */
    TRAPDOOR,

    /** Floor-based activation blocks (e.g., 'birch_pressure_plate'). */
    PRESSURE_PLATE,

    /** Wall-mounted activation blocks (e.g., 'jungle_button'). */
    BUTTON;

    // --- ⚙️ State & Fields ---

    /** The namespace for the block identifier (defaulting to "minecraft"). */
    private final String namespace;

    // --- 🏗️ Constructors ---

    /**
     * Default constructor utilizing the standard Minecraft namespace from {@link DatapackConfig}.
     */
    WoodBlock() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
    }

    /**
     * Constructor for wood blocks requiring a custom namespace (e.g., modded wood variants).
     * @param namespace The custom namespace component.
     * @throws NullPointerException if the namespace is null.
     */
    WoodBlock(String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase();
    }

    // --- 🛠️ Logic Methods ---

    /**
     * Constructs a complete, immutable {@link BlockResource} by applying a wood type to this template.
     * <p><b>Error Catching:</b> Strictly validates that the wood type is not null. It automatically
     * formats the resulting path to lowercase to ensure registry compatibility.</p>
     * * @param type The {@link WoodType} to apply (e.g., OAK, CHERRY).
     * @return A fully qualified and validated {@link BlockResource}.
     * @throws NullPointerException if the provided {@code type} is null.
     */
    public BlockResource withWoodType(WoodType type) {
        Objects.requireNonNull(type, "WoodType cannot be null for a WoodBlockId.");

        // Convert enum constant (e.g., STRIPPED_LOG) to lowercase path part (stripped_log)
        String blockNameComponent = this.name().toLowerCase();

        // Build the combined path: e.g., "cherry_planks"
        String fullPath = type.toString() + "_" + blockNameComponent;

        // Return a validated DynamicBlock instance
        return DynamicBlock.custom(namespace, fullPath);
    }

    // --- 🛰️ Metadata Accessors ---

    /**
     * Retrieves the namespace defined for this wood block category.
     * @return The namespace string.
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Returns a human-readable name for the block category.
     * @return The formatted name (e.g., "Stripped Log").
     */
    public String getCategoryName() {
        String name = this.name().replace("_", " ").toLowerCase();
        if (name.isEmpty()) return "";
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
package uhc.resource.block;

import uhc.core.DatapackConfig;
import java.util.Objects;

/**
 * 🌳 **Wood-Based Block Template Registry**
 * <p>
 * This enumeration serves as a factory template for blocks that require a {@link WoodType}
 * prefix to form a complete and valid Minecraft resource location.
 * </p>
 * <p>
 * <b>Example:</b> {@code PLANKS.withWoodType(WoodType.CHERRY)} produces a
 * {@link BlockIdentifier} for {@code "minecraft:cherry_planks"}.
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

    /** The namespace utilized for the generated block ID (e.g., "minecraft"). */
    private final String namespace;

    // --- 🏗️ Constructors ---

    /**
     * 🟢 **Default Constructor**
     * <p>Initializes the template with the standard Minecraft namespace
     * defined in the {@link DatapackConfig}.</p>
     */
    WoodBlock() {
        this.namespace = DatapackConfig.MINECRAFT_NAMESPACE;
    }

    /**
     * 🟡 **Custom Namespace Constructor**
     * <p>Initializes the template for custom or modded wood variants.</p>
     * @param namespace The custom namespace component (e.g., "biomesoplenty").
     * @throws NullPointerException if the provided namespace is null.
     */
    WoodBlock(String namespace) {
        this.namespace = Objects.requireNonNull(namespace, "Namespace cannot be null").toLowerCase();
    }

    // --- 🛠️ Logic Methods ---

    /**
     * Constructs a specific, immutable {@link BlockIdentifier} by applying a wood type to this template.
     * <p><b>Error Catching:</b> Strictly validates that the wood type is not null. It relies on
     * {@link DynamicBlock#custom} which performs secondary syntax validation and ensures the
     * result is not a tag.</p>
     * * @param type The {@link WoodType} to apply (e.g., OAK, CHERRY, WARPED).
     * @return A fully qualified and validated {@link BlockIdentifier}.
     * @throws NullPointerException if the provided {@code type} is null.
     */
    public BlockIdentifier withWoodType(WoodType type) {
        Objects.requireNonNull(type, "WoodType cannot be null for a WoodBlock template.");

        // Convert enum constant name (e.g., STRIPPED_LOG) to path part (stripped_log)
        String blockNameComponent = this.name().toLowerCase();

        // Build path: {woodType}_{blockName} -> e.g., "cherry_planks"
        String fullPath = type.toString() + "_" + blockNameComponent;

        // Delegate creation to DynamicBlock to ensure it meets BlockIdentifier constraints
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
     * Returns a formatted, human-readable name for the block category.
     * <p>Example: {@code STRIPPED_WOOD} -> "Stripped Wood"</p>
     * * @return The capitalized display name.
     */
    public String getCategoryName() {
        String rawName = this.name().replace("_", " ").toLowerCase();
        if (rawName.isEmpty()) return "";

        String[] words = rawName.split(" ");
        StringBuilder formatted = new StringBuilder();
        for (String word : words) {
            if (formatted.length() > 0) formatted.append(" ");
            formatted.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return formatted.toString();
    }
}
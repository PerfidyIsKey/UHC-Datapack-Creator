package uhc.resource.item;

/**
 * ⚒️ **Tool Piece Registry**
 * <p>
 * Defines the specific types of tools and weapons available in Minecraft.
 * This enum is used by the {@link DynamicItem} factory in conjunction with
 * {@link ToolMaterial} to generate valid {@link ItemResource} identifiers.
 * </p>
 */
public enum ToolPiece {

    /** High-damage melee weapon also used for stripping logs and breaking wood blocks. */
    AXE,

    /** Essential tool used for mining stone-based blocks and ores. */
    PICKAXE,

    /** Tool used for fast excavation of dirt, sand, gravel, and snow. */
    SHOVEL,

    /** Specialized tool used for tilling soil and harvesting certain plant blocks. */
    HOE,

    /** Primary melee weapon designed for fast attacks and sweeping damage. */
    SWORD;

    // --- 🔍 Core Methods ---

    /**
     * Converts the enum constant into its lowercase Minecraft identifier string.
     * <p><b>Error Catching:</b> Implements a fail-fast check to ensure the name
     * is valid and trims any whitespace to maintain strict ResourceLocation integrity.
     * This prevents issues like "minecraft:iron_ pickaxe".</p>
     * * @return The lowercase identifier (e.g., "pickaxe", "sword").
     * @throws IllegalStateException if the internal enum name is null or empty.
     */
    @Override
    public String toString() {
        String name = this.name();

        // Defensive check: While name() is a built-in, this protects the
        // ResourceLocation system from malformed string concatenation.
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("ToolPiece constant name is missing for ordinal: " + this.ordinal());
        }

        return name.toLowerCase().trim();
    }

    // --- 🛠️ Utility Methods ---

    /**
     * Returns a player-friendly version of the tool name.
     * <p>Example: {@code PICKAXE} -> "Pickaxe"</p>
     * * @return The capitalized name of the tool piece.
     */
    public String getDisplayName() {
        String raw = this.toString();
        if (raw.isEmpty()) return "";
        return raw.substring(0, 1).toUpperCase() + raw.substring(1);
    }
}
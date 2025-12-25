package uhc.resource.item;

/**
 * ⚒️ **Tool Material Registry**
 * <p>
 * Defines the material tiers available for tools and weapons. This enum is utilized
 * by the {@link DynamicItem} factory to construct valid Minecraft resource
 * locations (e.g., {@code "stone_pickaxe"} or {@code "netherite_sword"}).
 * </p>
 */
public enum ToolMaterial {

    /** The starting tier, crafted from wood planks. */
    WOODEN,

    /** Early-game material crafted from cobblestone or similar blocks. */
    STONE,

    /** Material with high mining speed and enchantability, but extremely low durability. */
    GOLDEN,

    /** The standard mid-tier material for general survival and mining. */
    IRON,

    /** High-tier material required for mining obsidian and ancient debris. */
    DIAMOND,

    /** The ultimate tier, created by upgrading diamond tools with netherite ingots. */
    NETHERITE;

    // --- 🔍 Core Methods ---

    /**
     * Converts the enum constant into its lowercase Minecraft identifier string.
     * <p><b>Error Catching:</b> Includes a fail-fast check to ensure the identifier
     * is never null or empty, preventing the creation of malformed resource
     * locations like {@code "minecraft:null_pickaxe"}.</p>
     * * @return The lowercase identifier string (e.g., "wooden", "iron").
     * @throws IllegalStateException if the enum name state is corrupted.
     */
    @Override
    public String toString() {
        String name = this.name();

        // Defensive check: Ensures the registry path part is valid.
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("ToolMaterial constant name is missing for ordinal: " + this.ordinal());
        }

        return name.toLowerCase().trim();
    }

    // --- 🛠️ Utility Methods ---

    /**
     * Returns the name of the tool material formatted for display.
     * <p>Example: {@code WOODEN} -> "Wooden"</p>
     * * @return The capitalized name of the tool material.
     */
    public String getDisplayName() {
        String raw = this.toString();
        if (raw.isEmpty()) return "";
        return raw.substring(0, 1).toUpperCase() + raw.substring(1);
    }
}
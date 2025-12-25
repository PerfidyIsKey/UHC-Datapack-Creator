package uhc.resource.item;

/**
 * 🛡️ **Armor Material Registry**
 * <p>
 * Defines the material types available for armor pieces. This enum is used
 * by the {@link DynamicItem} factory to construct valid Minecraft resource
 * locations (e.g., "diamond_chestplate").
 * </p>
 */
public enum ArmorMaterial {

    /** The lowest tier, craftable from cow hides. */
    LEATHER,

    /** Medium-tier armor found primarily in loot or on mobs; not craftable. */
    CHAINMAIL,

    /** Material with high enchantability but low durability. */
    GOLDEN,

    /** The standard mid-tier material for survival. */
    IRON,

    /** High-tier material offering significant protection and durability. */
    DIAMOND,

    /** The highest tier, obtained by upgrading diamond gear at a smithing table. */
    NETHERITE;

    // --- 🔍 Core Methods ---

    /**
     * Converts the enum constant into its lowercase Minecraft identifier string.
     * <p><b>Error Catching:</b> Includes a null-check on the name result (defensive)
     * and ensures the string is trimmed to prevent whitespace issues in
     * resource concatenation.</p>
     * * @return The lowercase identifier (e.g., "leather", "netherite").
     * @throws IllegalStateException if the enum name is unexpectedly null or empty.
     */
    @Override
    public String toString() {
        String name = this.name();

        // Defensive check: while name() is rarely null, catching it prevents
        // malformed ResourceLocations downstream.
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("ArmorMaterial constant name is missing for: " + this.ordinal());
        }

        return name.toLowerCase().trim();
    }

    // --- 🛠️ Utility Methods ---

    /**
     * Returns the material name as a formatted string for display purposes.
     * <p>Example: {@code DIAMOND} -> "Diamond"</p>
     * * @return The capitalized material name.
     */
    public String getDisplayName() {
        String raw = this.toString();
        return raw.substring(0, 1).toUpperCase() + raw.substring(1);
    }
}
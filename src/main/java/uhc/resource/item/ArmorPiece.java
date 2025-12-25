package uhc.resource.item;

/**
 * 🛡️ **Armor Piece Registry**
 * <p>
 * Defines the specific slots or types of armor equipment. This enum is used
 * by the {@link DynamicItem} factory to construct valid Minecraft resource
 * locations (e.g., "iron_helmet", "diamond_boots").
 * </p>
 */
public enum ArmorPiece {

    /** Equipment for the head slot. */
    HELMET,

    /** Equipment for the torso slot. */
    CHESTPLATE,

    /** Equipment for the legs slot. */
    LEGGINGS,

    /** Equipment for the feet slot. */
    BOOTS;

    // --- 🔍 Core Methods ---

    /**
     * Converts the enum constant into its lowercase Minecraft identifier string.
     * <p><b>Error Catching:</b> Includes a fail-fast check to ensure the enum name
     * is valid and trims any accidental whitespace to maintain strict
     * ResourceLocation syntax.</p>
     * * @return The lowercase identifier (e.g., "helmet", "boots").
     * @throws IllegalStateException if the enum name is null or unexpectedly empty.
     */
    @Override
    public String toString() {
        String name = this.name();

        // Defensive check: Ensures that the string conversion does not return
        // a value that would corrupt a ResourceLocation (e.g., "minecraft:null").
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("ArmorPiece constant name is missing for: " + this.ordinal());
        }

        return name.toLowerCase().trim();
    }

    // --- 🛠️ Utility Methods ---

    /**
     * Returns the name of the armor piece formatted for display.
     * <p>Example: {@code CHESTPLATE} -> "Chestplate"</p>
     * * @return The capitalized name of the armor piece.
     */
    public String getDisplayName() {
        String raw = this.toString();
        return raw.substring(0, 1).toUpperCase() + raw.substring(1);
    }
}
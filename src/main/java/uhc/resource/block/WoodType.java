package uhc.resource.block;

/**
 * 🌳 **Wood Type Registry**
 * <p>
 * An enumeration representing all standard (Overworld) and fungal (Nether) wood types
 * available in Minecraft Java Edition.
 * </p>
 * <p>
 * This class is used as a primary argument for {@link WoodBlock#withWoodType(WoodType)}
 * to dynamically construct namespaced identifiers like {@code minecraft:cherry_planks}.
 * </p>
 */
public enum WoodType {

    // --- 🪵 Overworld Wood Types ---

    /** Standard brown wood found in forests. */
    OAK,

    /** Darker wood found in taiga biomes. */
    SPRUCE,

    /** White-barked wood found in birch forests. */
    BIRCH,

    /** Tropical wood found in jungle biomes. */
    JUNGLE,

    /** Orange-tinted wood found in savanna biomes. */
    ACACIA,

    /** Deep brown wood found in dark forests. */
    DARK_OAK,

    /** Reddish-brown wood found in swampy mangrove forests. */
    MANGROVE,

    /** Pink-blossomed wood found in cherry groves. */
    CHERRY,

    /** Pale, desaturated wood found in the Pale Garden (1.21.2+). */
    PALE_OAK,

    /** Wood-like material derived from giant bamboo. */
    BAMBOO,

    // --- 🍄 Nether "Wood" (Fungi) Types ---

    /** Red-hued fungal material found in Crimson Forests. */
    CRIMSON,

    /** Teal-hued fungal material found in Warped Forests. */
    WARPED;

    // --- 🛠️ Core Methods ---

    /**
     * Converts the enum constant into its lowercase Minecraft path segment.
     * <p><b>Error Catching:</b> Implements a fail-safe check to ensure the enum identity
     * is valid before string manipulation. This prevents the generation of malformed
     * resource locations in command builders.</p>
     * * @return The lowercase identifier name (e.g., "dark_oak", "warped").
     * @throws IllegalStateException if the internal name is missing or empty.
     */
    @Override
    public String toString() {
        String internalName = this.name();

        // Catch rare internal state errors: Ensure the enum name isn't null or blank
        if (internalName == null || internalName.isBlank()) {
            throw new IllegalStateException("WoodType identity is corrupted for ordinal: " + this.ordinal());
        }

        return internalName.toLowerCase();
    }

    // --- 🔍 Metadata Accessors ---

    /**
     * Returns a player-friendly display name for this wood type.
     * <p>Example: {@code DARK_OAK} -> "Dark Oak"</p>
     * * @return The formatted capitalized name.
     */
    public String getDisplayName() {
        String raw = this.name().replace("_", " ").toLowerCase();
        if (raw.isEmpty()) return "";

        // Capitalize each word for the UI
        String[] words = raw.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!result.isEmpty()) result.append(" ");
            result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return result.toString();
    }

    /**
     * Identifies if the wood type originates from the Nether (Fungal).
     * * @return {@code true} if the type is CRIMSON or WARPED.
     */
    public boolean isNetherType() {
        return this == CRIMSON || this == WARPED;
    }
}
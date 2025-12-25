package uhc.resource.color;

/**
 * 🎨 **Minecraft Dye Color Registry**
 * <p>
 * Represents the 16 standard colors used across Minecraft for dyeing blocks,
 * items, and entities. This enum is a core component for building dynamic
 * resource locations such as {@code blue_wool} or {@code red_concrete_powder}.
 * </p>
 */
public enum DyeColor {

    // --- 🏳️ Light & Neutral Colors ---

    /** Standard white dye; hex approx #F9FFFE. */
    WHITE,
    /** Light gray dye; hex approx #9D9D97. */
    LIGHT_GRAY,
    /** Standard gray dye; hex approx #474F52. */
    GRAY,
    /** Standard black dye; hex approx #1D1D21. */
    BLACK,

    // --- 🌈 Vibrant Colors ---

    /** Standard red dye; hex approx #B02E26. */
    RED,
    /** Standard orange dye; hex approx #F9801D. */
    ORANGE,
    /** Standard yellow dye; hex approx #FED83D. */
    YELLOW,
    /** Standard lime dye; hex approx #80C71F. */
    LIME,
    /** Standard green dye; hex approx #5E7C16. */
    GREEN,
    /** Standard cyan dye; hex approx #169C9C. */
    CYAN,
    /** Light blue dye; hex approx #3AB3DA. */
    LIGHT_BLUE,
    /** Standard blue dye; hex approx #3C44AA. */
    BLUE,
    /** Standard purple dye; hex approx #8932B8. */
    PURPLE,
    /** Standard magenta dye; hex approx #C74EBD. */
    MAGENTA,
    /** Standard pink dye; hex approx #F38BAA. */
    PINK,
    /** Standard brown dye; hex approx #835432. */
    BROWN;

    // --- 🛠️ Core Methods ---

    /**
     * Converts the color constant into its lowercase Minecraft identifier segment.
     * <p><b>Error Catching:</b> Validates the internal enum state to ensure no
     * malformed strings are passed into resource location builders.</p>
     * * @return The lowercase color name (e.g., "light_blue").
     * @throws IllegalStateException if the enum constant name is inaccessible or empty.
     */
    @Override
    public String toString() {
        String internalName = this.name();

        // Defensive check to ensure the enum identity is valid
        if (internalName == null || internalName.isBlank()) {
            throw new IllegalStateException("DyeColor identity is corrupted for index: " + this.ordinal());
        }

        return internalName.toLowerCase();
    }

    // --- 🔍 Metadata Accessors ---

    /**
     * Returns a human-readable name formatted for User Interfaces.
     * <p>Example: {@code LIGHT_BLUE} -> "Light Blue"</p>
     * * @return The capitalized display name.
     */
    public String getDisplayName() {
        String raw = this.name().replace("_", " ").toLowerCase();

        // Capitalize each word (e.g., "light gray" -> "Light Gray")
        String[] words = raw.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (result.length() > 0) result.append(" ");
            result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return result.toString();
    }

    /**
     * Helper to retrieve the color based on its standard Minecraft metadata/ID (0-15).
     * * @param id The ordinal ID (e.g., 0 for White, 15 for Black).
     * @return The corresponding DyeColor.
     * @throws IllegalArgumentException if the ID is out of the 0-15 range.
     */
    public static DyeColor fromId(int id) {
        if (id < 0 || id >= values().length) {
            throw new IllegalArgumentException("DyeColor ID must be between 0 and 15. Provided: " + id);
        }
        return values()[id];
    }
}
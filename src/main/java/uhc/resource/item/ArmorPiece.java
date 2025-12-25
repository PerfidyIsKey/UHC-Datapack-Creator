package uhc.resource.item;

import java.util.Objects;

/**
 * 🛡️ **Armor Piece Registry**
 * <p>
 * Defines the specific slots or types of armor equipment. This enum is used
 * by item factories to construct valid Minecraft resource
 * locations (e.g., "iron_helmet", "diamond_horse_armor").
 * </p>
 */
public enum ArmorPiece {

    // --- 👤 Player Armor Pieces ---

    /** Equipment for the head slot. */
    HELMET,

    /** Equipment for the torso slot. */
    CHESTPLATE,

    /** Equipment for the legs slot. */
    LEGGINGS,

    /** Equipment for the feet slot. */
    BOOTS,

    // --- 🐎 Animal Armor Pieces ---

    /** Specialized protection for tamed horses. */
    HORSE_ARMOR;

    // --- 🔍 Core Methods ---

    /**
     * Converts the enum constant into its lowercase Minecraft identifier string.
     * <p><b>Error Catching:</b> Includes a fail-fast check to ensure the enum name
     * is valid and trims any accidental whitespace to maintain strict
     * ResourceLocation syntax.</p>
     * * @return The lowercase identifier (e.g., "helmet", "horse_armor").
     * @throws IllegalStateException if the enum name is null or unexpectedly empty.
     */
    @Override
    public String toString() {
        String name = this.name();

        // Defensive check: Ensures that the string conversion does not return
        // a value that would corrupt a ResourceLocation.
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("ArmorPiece constant name is missing for: " + this.ordinal());
        }

        return name.toLowerCase().trim();
    }

    // --- 🛠️ Utility Methods ---

    /**
     * Returns the name of the armor piece formatted for display.
     * <p>Example: {@code CHESTPLATE} -> "Chestplate", {@code HORSE_ARMOR} -> "Horse Armor"</p>
     * * @return The capitalized name of the armor piece.
     */
    public String getDisplayName() {
        String raw = this.toString().replace("_", " ");

        // Capitalize each word for the UI
        String[] words = raw.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (result.length() > 0) result.append(" ");
            result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return result.toString();
    }

    /**
     * Determines if the armor piece is designed for players.
     * * @return {@code true} if the piece is a Helmet, Chestplate, Leggings, or Boots.
     */
    public boolean isPlayerArmor() {
        return this != HORSE_ARMOR;
    }

    /**
     * Determines if the armor piece is designed for animals.
     * * @return {@code true} for {@code HORSE_ARMOR}.
     */
    public boolean isAnimalArmor() {
        return this == HORSE_ARMOR;
    }
}
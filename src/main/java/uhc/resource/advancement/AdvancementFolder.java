package uhc.resource.advancement;

import java.util.Objects;

/**
 * 🏆 **Advancement Folder Registry**
 * <p>
 * Defines the high-level root categories used by Minecraft for organizing
 * advancement trees. These represent the primary "tabs" seen in the
 * advancements menu.
 * </p>
 * <p>
 * This registry is used to construct resource paths for advancement
 * predicates and granting logic (e.g., {@code minecraft:story/root}).
 * </p>
 */
public enum AdvancementFolder {

    // --- 📂 Folder Definitions ---

    /** The main progression line (The "Minecraft" tab). */
    STORY,

    /** Advancements related to the Nether dimension. */
    NETHER,

    /** Advancements related to The End dimension. */
    END,

    /** Miscellaneous challenges and exploration tasks. */
    ADVENTURE,

    /** Combat and husbandry related tasks. */
    HUSBANDRY;

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the lowercase folder name for path construction.
     * <p><b>Example:</b> {@code AdvancementFolder.NETHER.getFolderName()} returns {@code "nether"}.</p>
     * * @return The lowercase string used in namespaced keys.
     */
    public String getFolderName() {
        return this.name().toLowerCase();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves an AdvancementFolder from a raw string identifier.
     * <p><b>Error Catching:</b> Handles null, blank, or unrecognized strings
     * by returning {@link #STORY} as a fail-safe default. This prevents
     * null pointer exceptions during dynamic path generation.</p>
     * * @param input The raw folder name (e.g., "end" or "Story").
     * @return The matching {@link AdvancementFolder}, or {@link #STORY} if invalid.
     */
    public static AdvancementFolder fromString(String input) {
        if (input == null || input.isBlank()) {
            return STORY;
        }

        String target = input.toUpperCase().trim();
        try {
            return AdvancementFolder.valueOf(target);
        } catch (IllegalArgumentException e) {
            // Logically catch any mistyped folders and fallback to the root story
            return STORY;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the lowercase folder segment for direct string concatenation.
     * <p><b>Implementation:</b> Leverages {@link #getFolderName()}.</p>
     * * @return The lowercase identifier (e.g., "adventure").
     */
    @Override
    public String toString() {
        return getFolderName();
    }
}
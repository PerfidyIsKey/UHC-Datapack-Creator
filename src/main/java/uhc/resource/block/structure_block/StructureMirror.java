package uhc.resource.block.structure_block;

import java.util.Objects;

/**
 * 🪞 **Structure Mirror Registry**
 * <p>
 * Defines the reflection states for a Minecraft Structure Block. These settings
 * determine if a structure should be flipped across a specific horizontal axis
 * when it is loaded into the world.
 * </p>
 */
public enum StructureMirror {

    // --- 🔀 Mirror Axis Definitions ---

    /** * **No Mirroring**
     * <p>The structure is loaded exactly as it was saved, with no reflection.</p>
     */
    NONE,

    /** * **Left-Right Mirroring**
     * <p>Reflects the structure across the North-South axis (Z-axis).</p>
     */
    LEFT_RIGHT,

    /** * **Front-Back Mirroring**
     * <p>Reflects the structure across the East-West axis (X-axis).</p>
     */
    FRONT_BACK;

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the exact string value required for the Structure Block NBT tag.
     * <p><b>Example:</b> {@code StructureMirror.LEFT_RIGHT.getNbtName()} returns {@code "LEFT_RIGHT"}.</p>
     * @return The uppercase identifier used by Minecraft's internal NBT parser.
     */
    public String getNbtName() {
        return this.name();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a StructureMirror mode from a raw string identifier.
     * <p><b>Error Catching:</b> Handles null, blank, or unrecognized strings by
     * returning {@link #NONE} as a fail-safe default. This ensures that
     * structures continue to load even if the NBT data is malformed.</p>
     * @param input The raw input string (e.g., "front_back" or "NONE").
     * @return The matching {@link StructureMirror}, or {@link #NONE} if invalid.
     */
    public static StructureMirror fromString(String input) {
        if (input == null || input.isBlank()) {
            return NONE;
        }

        String target = input.toUpperCase().trim();
        try {
            return StructureMirror.valueOf(target);
        } catch (IllegalArgumentException e) {
            // Logically catch any mistyped mirror modes from external data files
            return NONE;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the uppercase NBT-compatible name of the mirror mode.
     * @return The result of {@link #getNbtName()}.
     */
    @Override
    public String toString() {
        return getNbtName();
    }
}
package uhc.resource.block.structure_block;

import java.util.Objects;

/**
 * 🔄 **Structure Rotation Registry**
 * <p>
 * Defines the orthogonal rotation states for a Minecraft Structure Block.
 * These constants dictate how a structure is oriented around its origin
 * point (the Y-axis) when being loaded or placed into the world.
 * </p>
 */
public enum StructureRotation {

    // --- 📐 Rotation Definitions ---

    /** * **No Rotation**
     * <p>The structure maintains its original orientation as saved (0°).</p>
     */
    NONE,

    /** * **90° Clockwise Rotation**
     * <p>Rotates the structure 90 degrees to the right.</p>
     */
    CLOCKWISE_90,

    /** * **180° Clockwise Rotation**
     * <p>Rotates the structure 180 degrees (flipped horizontally and vertically).</p>
     */
    CLOCKWISE_180,

    /** * **90° Counter-Clockwise Rotation**
     * <p>Rotates the structure 90 degrees to the left (effectively a 270° clockwise rotation).</p>
     */
    COUNTERCLOCKWISE_90;

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the exact string value required for the Structure Block NBT tag.
     * <p><b>Example:</b> {@code StructureRotation.CLOCKWISE_90.getNbtName()} returns {@code "CLOCKWISE_90"}.</p>
     * @return The uppercase identifier used by Minecraft's internal NBT parser.
     */
    public String getNbtName() {
        return this.name();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a StructureRotation mode from a raw string identifier.
     * <p><b>Error Catching:</b> Handles null, blank, or unrecognized strings by
     * returning {@link #NONE} as a fail-safe default. This ensures that
     * structures continue to load even if the NBT data is malformed.</p>
     * @param input The raw input string (e.g., "clockwise_180" or "NONE").
     * @return The matching {@link StructureRotation}, or {@link #NONE} if invalid.
     */
    public static StructureRotation fromString(String input) {
        if (input == null || input.isBlank()) {
            return NONE;
        }

        String target = input.toUpperCase().trim();
        try {
            return StructureRotation.valueOf(target);
        } catch (IllegalArgumentException e) {
            // Logically catch any mistyped rotation modes from external data files
            return NONE;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the uppercase NBT-compatible name of the rotation mode.
     * @return The result of {@link #getNbtName()}.
     */
    @Override
    public String toString() {
        return getNbtName();
    }
}
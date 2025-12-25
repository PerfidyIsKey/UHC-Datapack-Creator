package uhc.resource.block.structure_block;

import java.util.Objects;

/**
 * 🏗️ **Structure Block Mode Registry**
 * <p>
 * Defines the functional modes for a Minecraft Structure Block. These modes
 * dictate how the block interacts with the world and the {@code .nbt} files
 * stored within the world's structure folder.
 * </p>
 */
public enum StructureBlockMode {

    // --- 🧱 Mode Definitions ---

    /**
     * **Save Mode**
     * <p>Used to define the boundaries of a structure and write it to a file.</p>
     */
    SAVE,

    /**
     * **Load Mode**
     * <p>Used to retrieve a structure from a file and place it into the world.</p>
     */
    LOAD,

    /**
     * **Corner Mode**
     * <p>Used to automatically calculate the size of a structure when used in
     * conjunction with Save mode.</p>
     */
    CORNER,

    /**
     * **Data Mode**
     * <p>Used for custom functions or marking specific locations within a structure
     * (e.g., chest placement or entity spawning logic).</p>
     */
    DATA;

    // --- 🛰️ Logic & Accessors ---

    /**
     * Retrieves the capitalized string value required by the Structure Block's NBT tag.
     * <p><b>Example:</b> {@code StructureBlockMode.SAVE.getNbtValue()} returns {@code "SAVE"}.</p>
     * @return The uppercase identifier.
     */
    public String getNbtValue() {
        return this.name();
    }

    /**
     * Retrieves the lowercase string value used in BlockState properties.
     * <p><b>Example:</b> {@code StructureBlockMode.LOAD.getBlockStateValue()} returns {@code "load"}.</p>
     * @return The lowercase identifier for state matching.
     */
    public String getBlockStateValue() {
        return this.name().toLowerCase();
    }

    // --- 🔍 Registry Lookups ---

    /**
     * Safely retrieves a StructureBlockMode from a raw string.
     * <p><b>Error Catching:</b> Performs a case-insensitive check. If the input
     * is null or invalid, it defaults to {@link #SAVE} to prevent TileEntity
     * corruption or crashes.</p>
     * @param input The raw input string (e.g., "save", "LOAD", "Corner").
     * @return The matching {@link StructureBlockMode}, or {@link #SAVE} as a fail-safe.
     */
    public static StructureBlockMode fromString(String input) {
        if (input == null || input.isBlank()) {
            return SAVE;
        }

        String target = input.toUpperCase().trim();
        try {
            return StructureBlockMode.valueOf(target);
        } catch (IllegalArgumentException e) {
            // Logically catch any mistyped modes from external data
            return SAVE;
        }
    }

    // --- 📝 Overrides ---

    /**
     * Returns the BlockState compatible lowercase name by default.
     * @return The result of {@link #getBlockStateValue()}.
     */
    @Override
    public String toString() {
        return getBlockStateValue();
    }
}
package uhc.core;

/**
 * ⚙️ **Datapack Configuration Constants**
 * <p>
 * This static utility class holds all essential, high-level metadata and configuration strings
 * required across the entire datapack generation project. It acts as the single source
 * of truth for global settings, enhancing maintainability and reducing 'magic strings'.
 * </p>
 */
public final class DatapackConfig { // Made 'final' to prevent inheritance

    /**
     * Private constructor to prevent instantiation of this static utility class.
     * All members should be accessed via their static fields (e.g., DatapackConfig.PACK_FORMAT).
     */
    private DatapackConfig() {
        // Enforcing static usage.
    }

    // --- PACK METADATA (Used in pack.mcmeta) ---

    /**
     * The minimum resource pack format version required by Minecraft (e.g., 88 for Minecraft 1.21.x).
     * This value is used for both the {@code min_format} and {@code max_format} fields in {@code pack.mcmeta}.
     */
    public static final int PACK_FORMAT = 88;

    /**
     * The user-friendly description string displayed to players in the Minecraft resource pack list.
     * This is placed in the {@code pack.mcmeta} file.
     */
    public static final String PACK_DESCRIPTION = "Example pack for Minecraft 1.21.9";

    // --- FILE SYSTEM CONFIGURATION (Used for Generator and Component Wiring) ---

    /**
     * The name of the default, **vanilla** namespace: {@code "minecraft"}.
     * This is used when defining mandatory engine components like the {@code load} and {@code tick} function tags.
     */
    public static final String MINECRAFT_NAMESPACE = "minecraft";

    /**
     * The name of the **custom** namespace: {@code "uhc_core_pack"}.
     * This is where all primary project functions, loot tables, and custom resources reside (e.g., {@code data/uhc_core_pack/}).
     */
    public static final String CUSTOM_NAMESPACE = "uhc_core_pack";

    /**
     * The name of the top-level folder generated inside the user's Minecraft datapacks directory.
     * Example: {@code <world>/datapacks/uhc_datapack/}.
     */
    public static final String DATAPACK_FOLDER_NAME = "uhc_datapack";

    /**
     * The root directory path where the final datapack folder will be placed during generation.
     * This path is typically external to the project source code.
     */
    public static final String OUTPUT_DIR_ROOT = "Server/world/datapacks";
}